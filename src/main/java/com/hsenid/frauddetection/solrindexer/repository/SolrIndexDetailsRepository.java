package com.hsenid.frauddetection.solrindexer.repository;

import com.hsenid.frauddetection.solrindexer.entity.SolrIndexDetails;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

public interface SolrIndexDetailsRepository extends PagingAndSortingRepository<SolrIndexDetails, Integer> {
    List<SolrIndexDetails> findAllByDateOrderByIdDesc(Date date);
}
