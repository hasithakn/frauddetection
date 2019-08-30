package com.hsenid.frauddetection.solrindexer;

import com.hsenid.frauddetection.solrindexer.entity.SolrIndexDetail;
import com.hsenid.frauddetection.solrindexer.entity.Status;
import com.hsenid.frauddetection.solrindexer.repository.SolrIndexDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Iterator;

@Service
public class App {

    private static Logger LOGGER = LoggerFactory.getLogger(App.class);

    @Autowired
    private SolrIndexDetailRepository solrIndexDetailRepository;

    @Autowired
    private Indexer indexer;

//    @Scheduled(fixedDelay = 1000 * 60)
    public void run() {

        //todo check another db and if ok then index  day.

        LOGGER.info("start scheduled app");

        LocalDateTime localDateTime = LocalDateTime.of(2019, 03, 07, 00, 00, 00);
        LocalDateTime localDateTimeMinus1 = localDateTime.minusDays(1);
        LOGGER.info("Date : " + localDateTime + " , minus 1day date : " + localDateTimeMinus1);

        Iterator<SolrIndexDetail> iterator = solrIndexDetailRepository.findAllByDateOrderByIdDesc(localDateTimeMinus1.toLocalDate()).iterator();
        SolrIndexDetail solrIndexDetail;
        if (iterator.hasNext()) {
            solrIndexDetail = iterator.next();
            LOGGER.info("found solrIndexDetail : previously processed " + solrIndexDetail.getStatus() + " : currentDoc : " + solrIndexDetail.getCurrentDoc());
            if (solrIndexDetail.getStatus() == Status.FINISHED) {
                LOGGER.info("Reading and indexing day : " + localDateTimeMinus1 + " Already finished ");
                return;
            }
            indexer.readAndIndex(localDateTimeMinus1, solrIndexDetail);
        } else {
            solrIndexDetail = solrIndexDetailRepository.save(new SolrIndexDetail(localDateTimeMinus1.toLocalDate(), Status.STARTED, 0));
            LOGGER.info("solrIndexDetail could not found, thus created new with status STARTED , docCount 0");
            indexer.readAndIndex(localDateTimeMinus1, solrIndexDetail);
        }
    }

}
