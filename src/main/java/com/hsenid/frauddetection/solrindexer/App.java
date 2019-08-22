package com.hsenid.frauddetection.solrindexer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

@Service
public class App {

    private static Logger LOGGER = LoggerFactory.getLogger(App.class);

    @Autowired
    private Indexer indexer;

//    @Scheduled(fixedDelay = 1000 * 60)
    public void run() {

        //todo
        // check another db and if ok then index  day.

        LOGGER.info("start scheduled app");
        java.util.Date uDate = new java.util.Date();
        Date sDate = new Date(uDate.getTime());
        Timestamp timestamp = Timestamp.valueOf(sDate.toString() + " 00:00:00");
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        cal.add(Calendar.DAY_OF_WEEK, -1);
        Timestamp timestampMinusDay = new Timestamp(cal.getTime().getTime());
        LOGGER.info("Date : "+timestamp.toLocalDateTime()+" , minus 1day date : " + timestampMinusDay.toLocalDateTime());
        indexer.readAndIndex(timestampMinusDay);
    }

}
