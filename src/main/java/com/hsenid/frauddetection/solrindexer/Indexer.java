package com.hsenid.frauddetection.solrindexer;

import com.hsenid.frauddetection.solrindexer.entity.MessageHistory;
import com.hsenid.frauddetection.solrindexer.entity.SolrEntity;
import com.hsenid.frauddetection.solrindexer.entity.SolrIndexDetail;
import com.hsenid.frauddetection.solrindexer.entity.Status;
import com.hsenid.frauddetection.solrindexer.logics.EntitiyConverter;
import com.hsenid.frauddetection.solrindexer.repository.MessageHistoryRepository;
import com.hsenid.frauddetection.solrindexer.repository.SolrEntityRepository;
import com.hsenid.frauddetection.solrindexer.repository.SolrIndexDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Indexer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Indexer.class);

    private final SolrEntityRepository solrEntityRepository;
    private final MessageHistoryRepository messageHistoryRepository;
    private final SolrIndexDetailRepository solrIndexDetailRepository;

    @Value(value = "${solrindexer.batch.size}")
    private int batch;

    @Autowired
    public Indexer(SolrEntityRepository solrEntityRepository, MessageHistoryRepository messageHistoryRepository, SolrIndexDetailRepository solrIndexDetailRepository) {
        this.solrEntityRepository = solrEntityRepository;
        this.messageHistoryRepository = messageHistoryRepository;
        this.solrIndexDetailRepository = solrIndexDetailRepository;
    }

    public void readAndIndex(Timestamp timestamp, SolrIndexDetail solrIndexDetail) {

        LOGGER.info("batch size :" + batch);

        int page = 0;
        int currentDoc = solrIndexDetail.getCurrentDoc();
        page = currentDoc / batch;
        solrIndexDetail.setStatus(Status.STARTED);
        solrIndexDetailRepository.save(solrIndexDetail);
        LOGGER.info("solrIndexDetail row updated to STARTED");

        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Timestamp timestampPlusDay = new Timestamp(cal.getTime().getTime());
        LOGGER.info("Reading and indexing day : " + timestamp.toLocalDateTime() + " to " + timestampPlusDay.toLocalDateTime());

        Page<MessageHistory> allByReceiveDateBetween;
        do {
            LOGGER.info("Started reading page : " + page);
            allByReceiveDateBetween = messageHistoryRepository.findAllByReceiveDateBetween(
                    timestamp,
                    timestampPlusDay,
                    PageRequest.of(page, batch)
            );
            LOGGER.info("received vs no of Total elements : " + allByReceiveDateBetween.getContent().size() + "/" + allByReceiveDateBetween.getTotalElements());

            if (allByReceiveDateBetween.getContent().size() > 0) {
                List<SolrEntity> solrEntities = allByReceiveDateBetween.stream()
                        .map(EntitiyConverter::sqlToSolrEntity)
                        .collect(Collectors.toList());

                LOGGER.info("started saving to solr : " + solrEntities.size());
                solrEntityRepository.saveAll(solrEntities);
                LOGGER.info("saved to solr : " + solrEntities.size());

                solrIndexDetail.setCurrentDoc(((page + 1) * batch));
                SolrIndexDetail save = solrIndexDetailRepository.save(solrIndexDetail);
                LOGGER.info("solrIndexDetail row updated : " + solrIndexDetail.getCurrentDoc());

                page++;
            } else {
                LOGGER.info("No records on this batch : ");
            }
        } while (allByReceiveDateBetween != null && !allByReceiveDateBetween.isLast());
        LOGGER.info("finished indexing to solr : ");

        solrIndexDetail.setStatus(Status.FINISHED);
        solrIndexDetailRepository.save(solrIndexDetail);
        LOGGER.info("solrIndexDetail row updated : FINISHED " + solrIndexDetail.getCurrentDoc());
    }
}
