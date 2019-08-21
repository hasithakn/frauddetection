package com.hsenid.frauddetection.solrindexer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class App {

    @Autowired
    private Indexer indexer;

    @Scheduled(fixedDelay = 5000000)
    public void run() {
        System.out.println("yo yo");
        indexer.readAndIndex(Timestamp.valueOf("2019-03-01 00:00:00"));
    }

}
