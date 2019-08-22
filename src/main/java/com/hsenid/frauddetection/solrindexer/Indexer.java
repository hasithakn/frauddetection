package com.hsenid.frauddetection.solrindexer;

import com.hsenid.frauddetection.solrindexer.entity.MessageHistory;
import com.hsenid.frauddetection.solrindexer.entity.SolrEntity;
import com.hsenid.frauddetection.solrindexer.entity.SolrIndexDetails;
import com.hsenid.frauddetection.solrindexer.logics.EntitiyConverter;
import com.hsenid.frauddetection.solrindexer.repository.MessageHistoryRepository;
import com.hsenid.frauddetection.solrindexer.repository.SolrEntityRepository;
import com.hsenid.frauddetection.solrindexer.repository.SolrIndexDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Indexer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Indexer.class);

    private final SolrEntityRepository solrEntityRepository;
    private final MessageHistoryRepository messageHistoryRepository;
    private final SolrIndexDetailsRepository solrIndexDetailsRepository;

    @Value(value = "${solrindexer.batch.size}")
    private int batch;

    @Autowired
    public Indexer(SolrEntityRepository solrEntityRepository, MessageHistoryRepository messageHistoryRepository, SolrIndexDetailsRepository solrIndexDetailsRepository) {
        this.solrEntityRepository = solrEntityRepository;
        this.messageHistoryRepository = messageHistoryRepository;
        this.solrIndexDetailsRepository = solrIndexDetailsRepository;
    }

    public void readAndIndex(Timestamp timestamp) {
        LOGGER.info("batch size :" + batch);

        int page = 0;

        Iterator<SolrIndexDetails> iterator = solrIndexDetailsRepository.findAllByDateOrderByIdDesc(new Date(timestamp.getTime())).iterator();
        SolrIndexDetails solrIndexDetails;
        if (iterator.hasNext()) {
            solrIndexDetails = iterator.next();
            LOGGER.info("found solrIndexDetails : previously processed " + solrIndexDetails.getStatus() + " : currentDoc : " + solrIndexDetails.getCurrentDoc());
            if (solrIndexDetails.getStatus() == SolrIndexDetails.Status.FINISHED) {
                LOGGER.info("Reading and indexing day : " + timestamp.toLocalDateTime() + " Already finished ");
                return;
            }
            int currentDoc = solrIndexDetails.getCurrentDoc();
            page = currentDoc / batch;
            solrIndexDetails.setStatus(SolrIndexDetails.Status.STARTED);
            solrIndexDetailsRepository.save(solrIndexDetails);
            LOGGER.info("solrIndexDetails row updated to STARTED");
        } else {
            solrIndexDetails = solrIndexDetailsRepository.save(new SolrIndexDetails(new Date(timestamp.getTime()), SolrIndexDetails.Status.STARTED, 0));
            LOGGER.info("solrIndexDetails could not found, thus created new with status STARTED , docCount 0");
        }

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

                solrIndexDetails.setCurrentDoc(((page + 1) * batch));
                solrIndexDetailsRepository.save(solrIndexDetails);
                LOGGER.info("solrIndexDetails row updated : " + solrIndexDetails.getCurrentDoc());

                page++;
            }else {
                LOGGER.info("No records on this batch : ");
            }
        } while (allByReceiveDateBetween != null && !allByReceiveDateBetween.isLast());
        LOGGER.info("finished indexing to solr : ");

        solrIndexDetails.setStatus(SolrIndexDetails.Status.FINISHED);
        solrIndexDetailsRepository.save(solrIndexDetails);
        LOGGER.info("solrIndexDetails row updated : FINISHED " + solrIndexDetails.getCurrentDoc());
    }
}
