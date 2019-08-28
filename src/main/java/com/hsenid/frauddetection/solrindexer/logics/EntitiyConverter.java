package com.hsenid.frauddetection.solrindexer.logics;

import com.hsenid.frauddetection.solrindexer.entity.MessageHistory;
import com.hsenid.frauddetection.solrindexer.entity.SolrEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.StringTokenizer;

public class EntitiyConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntitiyConverter.class);

    public EntitiyConverter() {
    }

    public static SolrEntity sqlToSolrEntity(MessageHistory messageHistory) {
        Timestamp receive_date = messageHistory.getReceiveDate();
        FilterLogic filterLogic = new FilterLogic();
        if (receive_date != null) {
            SolrEntity solrEntity = new SolrEntity();
            solrEntity.setApp_id(messageHistory.getAppId());

            //clean
            String sms = messageHistory.getMessage();
            sms = filterLogic.removeNewline(sms);
            sms = filterLogic.removeBackSlash(sms);
            sms = filterLogic.removeBackSlashR(sms);
            sms = filterLogic.removeTab(sms);
            solrEntity.setSms(sms);

            solrEntity.setCorrelationId(messageHistory.getCorrelationId());
            solrEntity.setDatetime(receive_date);
            solrEntity.setTermCount(getTermsCount(messageHistory.getMessage()));
            return solrEntity;
        } else {
            LOGGER.info("null return on sql to solr entity");
            return null;
        }
    }

    private static int getTermsCount(String message) {
        return new StringTokenizer(message, " ").countTokens();
    }
}
