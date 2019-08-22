package com.hsenid.frauddetection.solrindexer.repository;

import com.hsenid.frauddetection.solrindexer.entity.SolrIndexDetails;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SolrIndexDetailsRepository extends PagingAndSortingRepository<SolrIndexDetails, Integer> {
    List<SolrIndexDetails> findAllByDateOrderByIdDesc(Date date);
}
