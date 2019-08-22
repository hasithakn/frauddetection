package com.hsenid.frauddetection.solrsearch.repository;

import com.hsenid.frauddetection.solrsearch.entity.DetailedDuplicate;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailedDuplicateRepository extends PagingAndSortingRepository<DetailedDuplicate, Long> {

}
