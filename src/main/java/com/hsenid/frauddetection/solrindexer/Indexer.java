package com.hsenid.frauddetection.solrindexer;

import com.hsenid.frauddetection.solrindexer.entity.MessageHistory;
import com.hsenid.frauddetection.solrindexer.entity.SolrEntity;
import com.hsenid.frauddetection.solrindexer.logics.EntitiyConverter;
import com.hsenid.frauddetection.solrindexer.repository.MessageHistoryRepository;
import com.hsenid.frauddetection.solrindexer.repository.SolrEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
public class Indexer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Indexer.class);

    private final SolrEntityRepository solrEntityRepository;
    private final MessageHistoryRepository messageHistoryRepository;

    @Value(value = "${solrindexer.batch.size}")
    private int batch;

    @Autowired
    public Indexer(SolrEntityRepository solrEntityRepository, MessageHistoryRepository messageHistoryRepository) {
        this.solrEntityRepository = solrEntityRepository;
        this.messageHistoryRepository = messageHistoryRepository;
    }

    public void readAndIndex(Timestamp timestamp) {
        LOGGER.info("batch size :" + batch);

        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Timestamp timestampPlusDay = new Timestamp(cal.getTime().getTime());
        LOGGER.info("Reading and indexing day : " + timestamp.toLocalDateTime() + " to " + timestampPlusDay.toLocalDateTime());

        int page = 0;
        Page<MessageHistory> allByReceiveDateBetween;
        do {
            LOGGER.info("Started reading page : " + page);
            allByReceiveDateBetween = messageHistoryRepository.findAllByReceiveDateBetween(
                    timestamp,
                    timestampPlusDay,
                    PageRequest.of(page, batch)
            );
            LOGGER.info("received vs no of Total elements : " + allByReceiveDateBetween.getContent().size() + "/" + allByReceiveDateBetween.getTotalElements());
            page++;
            List<SolrEntity> solrEntities = allByReceiveDateBetween.stream()
                    .map(EntitiyConverter::sqlToSolrEntity)
                    .collect(Collectors.toList());
            LOGGER.info("started saving to solr : " + solrEntities.size());
            solrEntityRepository.saveAll(solrEntities);
            LOGGER.info("saved to solr : " + solrEntities.size());
        } while (allByReceiveDateBetween != null && !allByReceiveDateBetween.isLast());
        LOGGER.info("finished indexing to solr : ");
    }
}
