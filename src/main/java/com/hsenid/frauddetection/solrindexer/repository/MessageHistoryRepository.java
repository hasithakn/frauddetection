package com.hsenid.frauddetection.solrindexer.repository;

import com.hsenid.frauddetection.solrindexer.entity.MessageHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository
public interface MessageHistoryRepository extends PagingAndSortingRepository<MessageHistory, String> {
    Page<MessageHistory> findAllByMessage(String appID, Pageable pageable);

    Page<MessageHistory> findAllByAppId(String appID, Pageable pageable);

    Page<MessageHistory> findAllByReceiveDate(Timestamp time, Pageable pageable);

    Page<MessageHistory> findAllByReceiveDateBetween(Timestamp time1, Timestamp time2, Pageable pageable);
}
