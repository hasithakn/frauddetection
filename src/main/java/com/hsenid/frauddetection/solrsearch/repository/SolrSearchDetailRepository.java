package com.hsenid.frauddetection.solrsearch.repository;

import com.hsenid.frauddetection.solrindexer.entity.Status;
import com.hsenid.frauddetection.solrsearch.entity.SolrSearchDetail;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.Date;
import java.util.List;

public interface SolrSearchDetailRepository extends PagingAndSortingRepository<SolrSearchDetail, Integer>, QueryByExampleExecutor<SolrSearchDetail> {
    List<SolrSearchDetail> findAllByDateOrderByIdDesc(Date date);

    List<SolrSearchDetail> findAllByStatusNot(Status status);

    List<SolrSearchDetail> findAllByStatus(Status status);
}

