package com.hsenid.frauddetection.solrsearch.functions;

import com.hsenid.frauddetection.solrindexer.entity.SolrEntity;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SearchDuplicatesAdvance {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchDuplicatesAdvance.class);

    private SearchLogics searchLogics;

    @Autowired
    public SearchDuplicatesAdvance(SearchLogics searchLogics) {
        this.searchLogics = searchLogics;
    }

    public QueryResponse searchD101(SolrEntity a, String datetime, String core) {
        datetime = datetimeAndAppIdFilter(a, datetime);
        String q = queryFromSolrDoc(a);
        try {
//            LOGGER.info(q);
            return searchLogics.searchDateRangeGetAll(q, datetime, "sms", core);

        } catch (IOException | SolrServerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public QueryResponse searchD102(SolrEntity a, String datetime, String core) {
        datetime = datetimeAndAppIdFilter(a, datetime);
        String q = queryFromSolrDoc(a);
        try {
            return searchLogics.searchDateRangeGetAll(q, datetime, "smsNoPunctuations", core);
        } catch (IOException | SolrServerException e) {
            LOGGER.info(e.toString());
        }
        return null;
    }

    public QueryResponse searchD103(SolrEntity a, String datetime, String core) {
        String q = queryFromSolrDocD103(a);
        datetime = datetimeAndAppIdTermCountFilter(a, datetime);
        String mm = "100%";
        try {
            return searchLogics.searchDateRangeGetAllDismax(q, datetime, "smsEN", mm, core);
        } catch (IOException | SolrServerException e) {
            LOGGER.info(e.toString());
        }
        return null;
    }

    public QueryResponse searchD104(SolrEntity a, String datetime, String core) {
        String q = queryFromSolrDocD103(a);
        datetime = datetimeAndAppIdTermCountFilter(a, datetime);
        String mm = "-1";
        try {
            return searchLogics.searchDateRangeGetAllDismax(q, datetime, "smsEN", mm, core);
        } catch (IOException | SolrServerException e) {
            LOGGER.info(e.toString());
        }
        return null;
    }

    private String datetimeAndAppIdFilter(SolrEntity a, String datetime) {
        String appId = a.getApp_id();
        String correlationId = a.getCorrelationId();
        datetime = datetime + " AND app_id:" + appId + " AND NOT correlationId:" + correlationId;
        return datetime;
    }

    private String datetimeAndAppIdTermCountFilter(SolrEntity a, String datetime) {
        StringBuilder temp = new StringBuilder(datetimeAndAppIdFilter(a, datetime));
        temp.append(" AND termCount: [ * TO " + a.getTermCount() + " ] ");
        return temp.toString();

    }

    private String queryFromSolrDoc(SolrEntity a) {
        String sms = ClientUtils.escapeQueryChars(a.getSms());
        return "\"" + sms + "\"";
    }

    private String queryFromSolrDocD103(SolrEntity a) {
        return "( " + a.getSms() + " )";
    }

}
