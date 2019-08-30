package com.hsenid.frauddetection.solrindexer;

import com.hsenid.frauddetection.solrindexer.entity.SolrIndexDetail;
import com.hsenid.frauddetection.solrindexer.entity.Status;
import com.hsenid.frauddetection.solrindexer.repository.SolrEntityRepository;
import com.hsenid.frauddetection.solrindexer.repository.SolrIndexDetailRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexerTest {

    @Autowired
    Indexer indexer;

    @Autowired
    SolrEntityRepository solrEntityRepository;

    @Autowired
    SolrIndexDetailRepository solrIndexDetailRepository;

    @Test
    public void readAndIndex() {
        Instant localDateTime = LocalDateTime.now().toInstant(ZoneOffset.UTC);

        String format1 = DateTimeFormatter.ISO_INSTANT.format(localDateTime);

//        Page<SolrEntity> allByDatetimeBetween = solrEntityRepository.findAllByDatetimeBetweenOrderByDatetime("2019-03-01T00:00:00Z", "2019-03-02T00:00:00Z", PageRequest.of(0, 100));

        LocalDateTime now = LocalDateTime.of(2019, 03, 05, 00, 00, 00);
        LocalDate localDate1 = now.toLocalDate();
        List<SolrIndexDetail> allByDateOrderByIdDesc = solrIndexDetailRepository.findAllByDateOrderByIdDesc(localDate1);
        List<SolrIndexDetail> allByDateOrderByIdDesc2 = solrIndexDetailRepository.findAll(PageRequest.of(0, 10000)).getContent();
        System.out.println();


        //
//        Instant instant = now.toInstant(ZoneOffset.UTC);
//        String format1 = DateTimeFormatter.ISO_INSTANT.format(instant);

        System.out.println();
        SolrIndexDetail solrIndexDetail = new SolrIndexDetail();
        solrIndexDetail.setStatus(Status.STARTED);
        solrIndexDetail.setCurrentDoc(0);
        LocalDate localDate = now.toLocalDate();
        solrIndexDetail.setDate(localDate);

        indexer.readAndIndex(now, solrIndexDetail);
    }
}