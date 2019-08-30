package com.hsenid.frauddetection.solrindexer.repository;

import com.hsenid.frauddetection.solrindexer.entity.MessageHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

@Repository
public interface MessageHistoryRepository extends PagingAndSortingRepository<MessageHistory, String> {
    Page<MessageHistory> findAllByReceiveDateBetweenOrderByReceiveDate(LocalDateTime time1, LocalDateTime time2, Pageable pageable);
}
