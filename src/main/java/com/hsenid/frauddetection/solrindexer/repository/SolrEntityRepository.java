package com.hsenid.frauddetection.solrindexer.repository;

import com.hsenid.frauddetection.solrindexer.entity.SolrEntity;
import org.springframework.data.solr.repository.SolrCrudRepository;

public interface SolrEntityRepository extends SolrCrudRepository<SolrEntity, String> {

}
