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

import java.time.LocalDateTime;
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

    public void readAndIndex(LocalDateTime localDateTime, SolrIndexDetail solrIndexDetail) {

        LOGGER.info("batch size :" + batch);

        int page = 0;
        int currentDoc = solrIndexDetail.getCurrentDoc();
        page = currentDoc / batch;
        solrIndexDetail.setStatus(Status.STARTED);
        solrIndexDetailRepository.save(solrIndexDetail);
        LOGGER.info("solrIndexDetail row updated to STARTED");

        LocalDateTime localDateTimePlus1 = localDateTime.plusDays(1);
        LOGGER.info("Reading and indexing day : " + localDateTime + " to " + localDateTimePlus1);

        Page<MessageHistory> allByReceiveDateBetween;
        do {
            LOGGER.info("Started reading page : " + page);
            allByReceiveDateBetween = messageHistoryRepository.findAllByReceiveDateBetweenOrderByReceiveDate(
                    localDateTime,
                    localDateTimePlus1,
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
                solrIndexDetail = solrIndexDetailRepository.save(solrIndexDetail);
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
