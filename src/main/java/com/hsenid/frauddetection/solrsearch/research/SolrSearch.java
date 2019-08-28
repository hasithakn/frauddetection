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
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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

    @Value("${solrsearch.noOfThreads}")
    private int noOfThreads;

    private static Subject subject = new Subject();

    private static ArrayList<String> appIdFilter = new ArrayList<>();
    private static ArrayList<DetailedDuplicate> detailedDuplicates = new ArrayList<>();

    @Autowired
    private SearchingMethods SM;

    @Autowired
    private SolrEntityRepository solrEntityRepository;

    private static String D1 = "";
    private static String D7 = "";
    private static String D30 = "";

    public void run(Timestamp timestamp) throws InterruptedException {

        //timestamp = day of msgs to process

        LOGGER.info("solr searching start");
        new DBObserver(subject);

        try {
            appIdFilter = FileIO.getAppids(APP_ID_PATH);
        } catch (FileNotFoundException e) {
            LOGGER.error("File not found");
        }

        Timestamp timestampPlusOne = Timestamp.valueOf(
                timestamp.toLocalDateTime()
                        .toLocalDate()
                        .plusDays(1)
                        .format(DateTimeFormatter.ISO_LOCAL_DATE) + " 00:00:00"
        );

        setDateVariables(timestamp);

        List<SolrEntity> solrEntities = getSolrEntitiesByDateRange(
                TimeFunctions.timestampToISOWithOutUTC(timestamp),
                TimeFunctions.timestampToISOWithOutUTC(timestampPlusOne)
        );

        Thread[] ts = getThreads(solrEntities, noOfThreads);

        for (int i = 0; i < noOfThreads; i++) {
            ts[i].start();
            LOGGER.info("Thread : " + i + " started");
        }

        for (int i = 0; i < noOfThreads; i++) {
            ts[i].join();
        }

        LOGGER.info("finished search day : " + detailedDuplicates.size());
        subject.setDBRows(detailedDuplicates);
    }

    private void setDateVariables(Timestamp timestamp) {
        LocalDate temp = timestamp.toLocalDateTime().toLocalDate();
        Timestamp tempTimestamp = Timestamp.valueOf(temp.format(DateTimeFormatter.ISO_LOCAL_DATE) + " 00:00:00");
        D1 = TimeFunctions.timestampToISOWithOutUTC(tempTimestamp);
        LOGGER.info("D1 is set to : " + D1);

        temp = timestamp.toLocalDateTime().toLocalDate().minusDays(7);
        tempTimestamp = Timestamp.valueOf(temp.format(DateTimeFormatter.ISO_LOCAL_DATE) + " 00:00:00");
        D7 = TimeFunctions.timestampToISOWithOutUTC(tempTimestamp);
        LOGGER.info("D7 is set to : " + D7);

        temp = timestamp.toLocalDateTime().toLocalDate().minusDays(30);
        tempTimestamp = Timestamp.valueOf(temp.format(DateTimeFormatter.ISO_LOCAL_DATE) + " 00:00:00");
        D30 = TimeFunctions.timestampToISOWithOutUTC(tempTimestamp);
        LOGGER.info("D30 is set to : " + D30);
    }

    private Thread[] getThreads(List<SolrEntity> solrEntities, int noOfThreads) {

        LOGGER.info("no of Threads: " + (noOfThreads));
        int numFound = solrEntities.size();
        Thread[] ts = new Thread[noOfThreads];
        int docSlise = numFound / noOfThreads;
        int mod = numFound - (docSlise * noOfThreads);

        for (int i = 0; i < noOfThreads; i++) {
            final int temp = i;
            List<SolrEntity> solrEntities1;
            if (i != noOfThreads - 1) {
                solrEntities1 = solrEntities.subList((temp * docSlise), ((temp + 1) * docSlise) + 1);
            } else {
                solrEntities1 = solrEntities.subList((temp * docSlise), ((temp + 1) * docSlise) + mod);
            }
            System.out.println();
            ts[i] = new Thread(() -> processData(solrEntities1));
            LOGGER.info("Thread : " + temp + " , with list of size : " + (solrEntities1.size()));
        }
        return ts;
    }

    private List<SolrEntity> getSolrEntitiesByDateRange(String dateFrom, String dateTo) {
        int start = 0;
        List<SolrEntity> solrEntities = new LinkedList<>();
        Page<SolrEntity> allByDatetimeBetween;
        do {
            allByDatetimeBetween = solrEntityRepository.findAllByDatetimeBetween(
                    dateFrom,
                    dateTo,
                    PageRequest.of(start, batch));
            LOGGER.info("page " + start + " response done ");
            if (start <= 0) {
                LOGGER.info("total elements found " + allByDatetimeBetween.getTotalElements());
            }
            if (allByDatetimeBetween.getContent().size() > 0) {
                allByDatetimeBetween.getContent().forEach(solrEntity -> solrEntities.add(solrEntity));
                start++;
            } else {
                LOGGER.info("No records on this batch : ");
            }
        } while (allByDatetimeBetween != null && !allByDatetimeBetween.isLast());
        LOGGER.info("solrEntitiesList is updated with solr docs on day : ");
        return solrEntities;
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
                Date date = new Date(a.getDatetime().getTime());
                detailedDuplicate.setDate(date);
                detailedDuplicate.setDetails(details.toString());
                detailedDuplicates.add(detailedDuplicate);
                LOGGER.info(detailedDuplicate.getCorrelationId() + " -- " + d1 + " : " + d7 + " : " + d30);
            } catch (Exception e) {
                LOGGER.error(e.getStackTrace()[0].toString());
            }
        });
    }
}

