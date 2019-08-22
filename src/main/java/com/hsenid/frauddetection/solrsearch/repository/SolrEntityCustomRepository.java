package com.hsenid.frauddetection.solrsearch.repository;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.MapSolrParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class SolrEntityCustomRepository {

    @Resource
    private SolrTemplate solrTemplate;

    public QueryResponse reader(String q, String fl, String fq, String start, String rows, String df, String sort, String debugQuery, String core) throws IOException, SolrServerException {

        Map<String, String> queryParamMap = new HashMap();
        if (!q.equals("")) {
            queryParamMap.put("q", q);
        }
        if (!fl.equals("")) {
            queryParamMap.put("fl", fl);
        }
        if (!fq.equals("")) {
            queryParamMap.put("fq", fq);
        }
        if (!rows.equals("")) {
            queryParamMap.put("rows", rows);
        }
        if (!start.equals("")) {
            queryParamMap.put("start", start);
        }
        if (!df.equals("")) {
            queryParamMap.put("df", df);
        }
        if (!sort.equals("")) {
            queryParamMap.put("sort", sort);
        }
        if (!debugQuery.equals("")) {
            queryParamMap.put("debugQuery", debugQuery);
        }
        MapSolrParams queryParams = new MapSolrParams(queryParamMap);
        QueryResponse response = solrTemplate.getSolrClient().query(core, queryParams);
        return response;
    }

    public QueryResponse readerDismax(String q, String fl, String fq, String start, String rows, String df, String sort, String debugQuery, String mm, String core) throws IOException, SolrServerException {

        Map<String, String> queryParamMap = new HashMap();
        queryParamMap.put("defType", "dismax");
        if (!q.equals("")) {
            queryParamMap.put("q", q);
        }
        if (!fl.equals("")) {
            queryParamMap.put("fl", fl);
        }
        if (!mm.equals("")) {
            queryParamMap.put("mm", mm);
        }
        if (!fq.equals("")) {
            queryParamMap.put("fq", fq);
        }
        if (!rows.equals("")) {
            queryParamMap.put("rows", rows);
        }
        if (!start.equals("")) {
            queryParamMap.put("start", start);
        }
        if (!df.equals("")) {
            queryParamMap.put("df", df);
        }
        if (!sort.equals("")) {
            queryParamMap.put("sort", sort);
        }
        if (!debugQuery.equals("")) {
            queryParamMap.put("debugQuery", debugQuery);
        }
        MapSolrParams queryParams = new MapSolrParams(queryParamMap);
        QueryResponse response = solrTemplate.getSolrClient().query(core, queryParams);
        return response;
    }
}
