package com.hsenid.frauddetection.solrsearch.research;

import com.hsenid.frauddetection.solrindexer.entity.SolrEntity;
import com.hsenid.frauddetection.solrindexer.repository.SolrEntityRepository;
import com.hsenid.frauddetection.solrsearch.entity.DetailedDuplicate;
import com.hsenid.frauddetection.solrsearch.fileio.FileIO;
import com.hsenid.frauddetection.solrsearch.functions.SearchingMethods;
import com.hsenid.frauddetection.solrsearch.functions.TimeFunctions;
import com.hsenid.frauddetection.solrsearch.observer.DBObserver;
import com.hsenid.frauddetection.solrsearch.subject.Subject;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * This is to find duplicates on D101 and if not d101 then D102, (ignoring D103 and D104)
 */
@Service
public class SolrSearch {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolrSearch.class);

    @Value("${solrsearch.filter.appIdList}")
    private String APP_ID_PATH;

    @Value("${solr.core}")
    private String CORE;

    @Value("${solrsearch.batch.size}")
    private int batch;


    private static Subject subject = new Subject();
    private static ArrayList<String> appIdFilter = new ArrayList<>();
    private static ArrayList<DetailedDuplicate> detailedDuplicates = new ArrayList<>();

    @Autowired
    private SearchingMethods SM;

    @Autowired
    private SolrEntityRepository solrEntityRepository;

    private static final String DATE_RANGE_TO_SEARCH =
            "datetime: [2019-03-01T00:00:00Z TO 2019-03-02T00:00:00Z]";

    private static final String D1 = "2019-03-01T00:00:00Z";
    private static final String D7 = "2019-02-23T00:00:00Z";
    private static final String D30 = "2019-01-31T00:00:00Z";


    @PostConstruct
    public void run() throws InterruptedException {

        LOGGER.info("solr searching start");
        new DBObserver(subject);
        try {
            appIdFilter = FileIO.getAppids(APP_ID_PATH);
        } catch (FileNotFoundException e) {
            LOGGER.error("File not found");
        }

        long numFound = 0;
        int start = 0;

        List<List<SolrEntity>> solrEntities = new ArrayList<List<SolrEntity>>();

        Page<SolrEntity> allByDatetimeBetween;
        do {
            allByDatetimeBetween = solrEntityRepository.findAllByDatetimeBetween(
                    "2019-03-02T20:00:00Z",
                    "NOW",
                    PageRequest.of(start, batch));
            LOGGER.info("page " + start + " response done ");
            if (start <= 0) {
                numFound = allByDatetimeBetween.getTotalElements();
                LOGGER.info("total elements found " + allByDatetimeBetween.getTotalElements());
            }
            if (allByDatetimeBetween.getContent().size() > 0) {
                solrEntities.add(allByDatetimeBetween.getContent());
                start++;
            } else {
                LOGGER.info("No records on this batch : ");
            }
        } while (allByDatetimeBetween != null && !allByDatetimeBetween.isLast());
        LOGGER.info("solrEntitiesList is updated with solr docs on day : ");

        int noOfTimes = (int) (numFound / batch);
        LOGGER.info("no of Threads: " + (noOfTimes + 1));

        Thread[] ts = new Thread[noOfTimes + 1];

        for (int i = 0; i < noOfTimes + 1; i++) {
            final int temp = i;
            ts[i] = new Thread(() -> processData(solrEntities.get(temp)));
        }

        for (int i = 0; i < noOfTimes + 1; i++) {
            ts[i].start();
            LOGGER.info("Thread : " + i + " started");
        }

        for (int i = 0; i < noOfTimes + 1; i++) {
            ts[i].join();
        }
        LOGGER.info("finished search day : ");
//        subject.setDBRows(detailedDuplicates);
    }


    private void processData(List<SolrEntity> results) {

        List<SolrEntity> filteredList = results.stream().parallel()
                .filter(solrEntity -> appIdFilter.contains(solrEntity.getApp_id()))
                .collect(Collectors.toList());
        LOGGER.info("SubList is filtered : " + filteredList.size() + " / " + results.size());

        filteredList.forEach(a -> {
            try {
                int d1 = 0;
                int d7 = 0;
                int d30 = 0;
                JSONObject details = new JSONObject();

                String s = TimeFunctions.addTimeFilterDateXTillDocTime(a, D1);
                QueryResponse response = SM.searchForDuplicatesD101D102(a, s, CORE);
                List<String> d1List = response.getResults().stream()
                        .map(e -> e.getFieldValue("correlationId").toString())
                        .collect(Collectors.toList());
                d1 = (int) response.getResults().getNumFound();
                details.put("d1", d1List);

                s = TimeFunctions.getDateTimeWithXY(D7, D1);
                response = SM.searchForDuplicatesD101D102(a, s, CORE);
                List<String> d7List = response.getResults().stream()
                        .map(e -> e.getFieldValue("correlationId").toString())
                        .collect(Collectors.toList());
                d7 = (int) response.getResults().getNumFound();
                details.put("d7", d7List);

                s = TimeFunctions.getDateTimeWithXY(D30, D7);
                response = SM.searchForDuplicatesD101D102(a, s, CORE);
                List<String> d30List = response.getResults().stream()
                        .map(e -> e.getFieldValue("correlationId").toString())
                        .collect(Collectors.toList());
                d30 = (int) response.getResults().getNumFound();
                details.put("d30", d30List);

                DetailedDuplicate detailedDuplicate = new DetailedDuplicate();
                detailedDuplicate.setAppID(a.getApp_id());
                detailedDuplicate.setCorrelationId(Long.valueOf(a.getCorrelationId()));
                detailedDuplicate.setD1(d1);
                detailedDuplicate.setD7(d7);
                detailedDuplicate.setD30(d30);
                detailedDuplicate.setDetails(details.toString());
                detailedDuplicates.add(detailedDuplicate);
                LOGGER.info(detailedDuplicate.getCorrelationId() + " -- " + d1 + " : " + d7 + " : " + d30);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        });
    }
}

