package com.hsenid.frauddetection.solrsearch;

import com.hsenid.frauddetection.solrindexer.entity.SolrIndexDetail;
import com.hsenid.frauddetection.solrindexer.entity.Status;
import com.hsenid.frauddetection.solrindexer.repository.SolrIndexDetailRepository;
import com.hsenid.frauddetection.solrsearch.entity.SolrSearchDetail;
import com.hsenid.frauddetection.solrsearch.repository.SolrSearchDetailRepository;
import com.hsenid.frauddetection.solrsearch.research.SolrSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Searcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(Searcher.class);

    @Autowired
    SolrSearch solrSearch;
    @Autowired
    SolrSearchDetailRepository solrSearchDetailRepository;
    @Autowired
    SolrIndexDetailRepository solrIndexDetailRepository;

//    @PostConstruct
    public void search() {
        // todo check another db and if ok then run for that day

        List<SolrIndexDetail> unfinishedListToSearch = getUnfinishedListToSearch();

        LOGGER.info("no of dates in  filterd list to search : " + unfinishedListToSearch.size());

        unfinishedListToSearch.stream()
                .forEach(e -> {
                    try {
                        Iterator<SolrSearchDetail> solrSearchDetails = solrSearchDetailRepository.findAllByDateOrderByIdDesc(e.getDate()).iterator();
                        if (solrSearchDetails.hasNext()) {
                            LOGGER.info("search record found on solr search details day : " + e.getDate().toString());
                            SolrSearchDetail next = solrSearchDetails.next();

                            LOGGER.info("processing day : " + next.getDate().toString());
                            LOGGER.info("current doc : " + next.getCurrentDoc());
                            LocalDateTime localDateTime = e.getDate().atStartOfDay();
                            solrSearch.run(localDateTime);

                            next.setStatus(Status.FINISHED);
                            solrSearchDetailRepository.save(next);
                            LOGGER.info("Finished status saved ");
                        } else {
                            LOGGER.info("NO record found on solr search details day : " + e.getDate().toString());
                            SolrSearchDetail next = new SolrSearchDetail();
                            next.setStatus(Status.STARTED);
                            next.setDate(e.getDate());
                            next.setCurrentDoc(0);
                            next = solrSearchDetailRepository.save(next);
                            LOGGER.info("created new record on solr search details day : " + e.getDate().toString() + " status Started");

                            LOGGER.info("processing day : " + next.getDate().toString());
                            LOGGER.info("current doc : " + next.getCurrentDoc());
                            LocalDateTime localDateTime = e.getDate().atStartOfDay();
                            solrSearch.run(localDateTime);

                            next.setStatus(Status.FINISHED);
                            solrSearchDetailRepository.save(next);
                            LOGGER.info("Finished status saved ");
                        }
                    } catch (InterruptedException e1) {
                        LOGGER.error(e1.getMessage());
                        LOGGER.error(e1.getStackTrace()[0].toString());
                    }
                });
    }

    private List<SolrIndexDetail> getUnfinishedListToSearch() {
        List<SolrIndexDetail> finishedIndexList = solrIndexDetailRepository.findAllByStatus(Status.FINISHED);
        List<LocalDate> finishedSearchDates = solrSearchDetailRepository.findAllByStatus(Status.FINISHED)
                .stream()
                .map(e -> e.getDate())
                .collect(Collectors.toList());

        return finishedIndexList.stream()
                .filter(e -> !finishedSearchDates.contains(e.getDate()))
                .collect(Collectors.toList());
    }
}
