package com.hsenid.frauddetection.solrindexer.repository;

import com.hsenid.frauddetection.solrindexer.entity.SolrIndexDetail;
import com.hsenid.frauddetection.solrindexer.entity.Status;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SolrIndexDetailRepository extends PagingAndSortingRepository<SolrIndexDetail, Integer>, QueryByExampleExecutor<SolrIndexDetail> {
    List<SolrIndexDetail> findAllByDateOrderByIdDesc(LocalDate date);

    List<SolrIndexDetail> findAllByStatusNot(Status status);

    List<SolrIndexDetail> findAllByStatus(Status status);
}
