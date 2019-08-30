package com.hsenid.frauddetection.solrindexer.repository;

import com.hsenid.frauddetection.solrindexer.entity.MessageHistory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageHistoryRepositoryTest {
    @Autowired
    MessageHistoryRepository messageHistoryRepository;

    @Test
    public void findAllByReceiveDateBetween() {
        Instant parse = Instant.parse("2019-03-03T00:00:00Z");
        Instant parse2 = Instant.parse("2019-03-04T00:00:00Z");
        Page<MessageHistory> allByReceiveDateBetween = messageHistoryRepository.findAllByReceiveDateBetweenOrderByReceiveDate(
                LocalDateTime.ofInstant(parse, ZoneId.of("Z")),
                LocalDateTime.ofInstant(parse2, ZoneId.of("Z")),
                PageRequest.of(0, 1000000));

        System.out.println();
    }
}