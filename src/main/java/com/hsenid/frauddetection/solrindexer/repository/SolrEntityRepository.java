package com.hsenid.frauddetection.solrindexer.repository;

import com.hsenid.frauddetection.solrindexer.entity.SolrEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface SolrEntityRepository extends SolrCrudRepository<SolrEntity, String> {
    Page<SolrEntity> findAllByDatetimeBetween(String d1, String d2, Pageable pageable);
}
