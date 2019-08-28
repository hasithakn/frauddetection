package com.hsenid.frauddetection.solrindexer;

import com.hsenid.frauddetection.solrindexer.entity.SolrIndexDetail;
import com.hsenid.frauddetection.solrindexer.entity.Status;
import com.hsenid.frauddetection.solrindexer.repository.SolrIndexDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;

@Service
public class App {

    private static Logger LOGGER = LoggerFactory.getLogger(App.class);

    @Autowired
    private SolrIndexDetailRepository solrIndexDetailRepository;

    @Autowired
    private Indexer indexer;

    @Scheduled(fixedDelay = 1000 * 60)
    public void run() {

        //todo check another db and if ok then index  day.

        LOGGER.info("start scheduled app");
        java.util.Date uDate = new java.util.Date();
        Date sDate = new Date(uDate.getTime());
//        Timestamp timestamp = Timestamp.valueOf(sDate.toString() + " 00:00:00");
        Timestamp timestamp = Timestamp.valueOf("2019-03-04" + " 00:00:00");
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        cal.add(Calendar.DAY_OF_WEEK, -1);
        Timestamp timestampMinusDay = new Timestamp(cal.getTime().getTime());
        LOGGER.info("Date : " + timestamp.toLocalDateTime() + " , minus 1day date : " + timestampMinusDay.toLocalDateTime());

        Iterator<SolrIndexDetail> iterator = solrIndexDetailRepository.findAllByDateOrderByIdDesc(new Date(timestampMinusDay.getTime())).iterator();
        SolrIndexDetail solrIndexDetail;
        if (iterator.hasNext()) {
            solrIndexDetail = iterator.next();
            LOGGER.info("found solrIndexDetail : previously processed " + solrIndexDetail.getStatus() + " : currentDoc : " + solrIndexDetail.getCurrentDoc());
            if (solrIndexDetail.getStatus() == Status.FINISHED) {
                LOGGER.info("Reading and indexing day : " + timestampMinusDay.toLocalDateTime() + " Already finished ");
                return;
            }
            indexer.readAndIndex(timestampMinusDay, solrIndexDetail);
        } else {
            solrIndexDetail = solrIndexDetailRepository.save(new SolrIndexDetail(new Date(timestampMinusDay.getTime()), Status.STARTED, 0));
            LOGGER.info("solrIndexDetail could not found, thus created new with status STARTED , docCount 0");
            indexer.readAndIndex(timestampMinusDay, solrIndexDetail);
        }
    }

}
