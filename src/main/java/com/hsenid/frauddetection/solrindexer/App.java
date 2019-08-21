package com.hsenid.frauddetection.solrindexer;

import com.hsenid.frauddetection.solrindexer.entity.SolrIndexDetails;
import com.hsenid.frauddetection.solrindexer.repository.SolrIndexDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;

@Service
public class App {

    @Autowired
    private Indexer indexer;

    @Autowired
    SolrIndexDetailsRepository solrIndexDetailsRepository;

    @Scheduled(fixedDelay = 5000000)
    public void run() {
        indexer.readAndIndex(Timestamp.valueOf("2019-03-02 00:00:00"));
    }

}
