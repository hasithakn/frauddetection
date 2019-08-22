package com.hsenid.frauddetection.solrsearch.functions;

import com.hsenid.frauddetection.solrsearch.repository.SolrEntityCustomRepository;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SearchLogics {

    private SolrEntityCustomRepository solrEntityCustomRepository;

    @Autowired
    public SearchLogics(SolrEntityCustomRepository solrEntityCustomRepository) {
        this.solrEntityCustomRepository = solrEntityCustomRepository;
    }

    public QueryResponse searchDateRangeGetAll(
            String q,
            String dateRangeAS_fq,
            String df,
            String core
    ) throws IOException, SolrServerException {
        int start = 0;
        final int batch = 10;
        QueryResponse reader = searchDateRange(q, dateRangeAS_fq, df, 0, batch, core);
        return reader;
    }

    public QueryResponse searchDateRangeGetAllDismax(
            String q,
            String dateRangeAS_fq,
            String df,
            String mm,
            String core
    ) throws IOException, SolrServerException {
        int start = 0;
        final int batch = 10;
        QueryResponse reader = searchDateRangeDismax(q, dateRangeAS_fq, df, 0, batch, mm, core);
        return reader;
    }

    public QueryResponse searchDateRange(String q, String dateRangeAS_fq, String df, int start, int rows, String core) throws IOException, SolrServerException {
        return solrEntityCustomRepository.reader(q, "", dateRangeAS_fq, Integer.toString(start), Integer.toString(rows), df, "", "off", core);
    }

    public QueryResponse searchDateRangeDismax(String q, String dateRangeAS_fq, String df, int start, int rows, String mm, String core) throws IOException, SolrServerException {
        return solrEntityCustomRepository.readerDismax(q, "", dateRangeAS_fq, Integer.toString(start), Integer.toString(rows), df, "", "on", mm, core);

    }
}
