package com.hsenid.frauddetection.solrsearch.research;

import com.hsenid.frauddetection.solrindexer.entity.SolrEntity;
import com.hsenid.frauddetection.solrsearch.functions.TimeFunctions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SolrSearchTest {
    @Autowired
    SolrSearch solrSearch;

    @Test
    public void run() throws InterruptedException {

        LocalDateTime localDateTime = LocalDateTime.of(2019,03,06,00,00,00);
        solrSearch.run(localDateTime);

//        LocalDateTime localDateTimePlusOne = localDateTime.plusDays(1);
//        String strDay = DateTimeFormatter.ISO_INSTANT.format(localDateTime.toInstant(ZoneOffset.UTC));
//        String strDayPlusOne = DateTimeFormatter.ISO_INSTANT.format(localDateTimePlusOne.toInstant(ZoneOffset.UTC));
//        List<SolrEntity> solrEntities = solrSearch.getSolrEntitiesByDateRange(strDay, strDayPlusOne);
//
//        TimeFunctions.addTimeFilterDateXTillDocTime(solrEntities.get(0),"ds");
        System.out.println();
    }
}