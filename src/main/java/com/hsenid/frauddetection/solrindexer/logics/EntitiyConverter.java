package com.hsenid.frauddetection.solrindexer.logics;

import com.hsenid.frauddetection.solrindexer.entity.MessageHistory;
import com.hsenid.frauddetection.solrindexer.entity.SolrEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.StringTokenizer;

public class EntitiyConverter {

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
            solrEntity.setDatetime(filterLogic.timestampToISO(receive_date));
            solrEntity.setTermCount(getTermsCount(messageHistory.getMessage()));
            return solrEntity;
        } else {
            return null;
        }
    }

    private static int getTermsCount(String message) {
        return new StringTokenizer(message, " ").countTokens();
    }
}
