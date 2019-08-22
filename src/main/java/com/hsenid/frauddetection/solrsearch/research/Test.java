package com.hsenid.frauddetection.solrsearch.research;

import com.hsenid.frauddetection.solrindexer.entity.SolrEntity;
import com.hsenid.frauddetection.solrindexer.repository.SolrEntityRepository;
import com.hsenid.frauddetection.solrsearch.functions.SearchLogics;
import com.hsenid.frauddetection.solrsearch.functions.SearchingMethods;
import com.hsenid.frauddetection.solrsearch.functions.TimeFunctions;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;

@Service
public class Test {

    private static final String D1 = "2019-03-01T00:00:00Z";
    private static final String D7 = "2019-02-23T00:00:00Z";
    private static final String D30 = "2019-01-31T00:00:00Z";

    @Autowired
    public SolrEntityRepository solrEntityRepository;

    @Autowired
    public SearchingMethods searchingMethods;

//    @PostConstruct
    public void run() {

        Page<SolrEntity> allByDatetimeBetween = solrEntityRepository.findAllByDatetimeBetween(
                "2019-03-02T23:59:40Z",
                "NOW",
                PageRequest.of(0, 1));

        SolrEntity next = allByDatetimeBetween.getContent().iterator().next();
        String s = TimeFunctions.addTimeFilterDateXTillDocTime(next, D1);


        QueryResponse experiment4 = searchingMethods.searchForDuplicatesD101D102(next, s, "experiment4");
        System.out.println();
    }
}
