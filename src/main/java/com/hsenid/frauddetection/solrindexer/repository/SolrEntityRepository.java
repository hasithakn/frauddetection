package com.hsenid.frauddetection.solrindexer.repository;

import com.hsenid.frauddetection.solrindexer.entity.SolrEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolrEntityRepository extends SolrCrudRepository<SolrEntity, String> {
    Page<SolrEntity> findAllByDatetimeBetweenOrderByDatetime(String d1, String d2, Pageable pageable);
}
