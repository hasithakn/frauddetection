package com.hsenid.frauddetection.solrsearch.functions;

import com.hsenid.frauddetection.solrindexer.entity.SolrEntity;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class SearchingMethods {

    private SearchDuplicatesAdvance SDA;

    @Autowired
    public SearchingMethods(SearchDuplicatesAdvance SDA) {
        this.SDA = SDA;
    }

    public QueryResponse searchForDuplicates(SolrEntity a, String datetime, String core) {

        QueryResponse solrDocuments = SDA.searchD101(a, datetime, core);
        if (solrDocuments == null | solrDocuments.getResults().isEmpty()) {
            solrDocuments = SDA.searchD102(a, datetime, core);
            if (solrDocuments == null | solrDocuments.getResults().isEmpty()) {
                solrDocuments = SDA.searchD103(a, datetime, core);
                if (solrDocuments == null | solrDocuments.getResults().isEmpty()) {
                    solrDocuments = SDA.searchD104(a, datetime, core);
                }
            }
        }
        return solrDocuments;
    }

    public QueryResponse searchForDuplicatesD101D102(SolrEntity a, String datetime, String core) {

        QueryResponse solrDocuments = SDA.searchD101(a, datetime, core);
        if (solrDocuments == null | solrDocuments.getResults().isEmpty()) {
            solrDocuments = SDA.searchD102(a, datetime, core);
        }
        return solrDocuments;
    }

}
