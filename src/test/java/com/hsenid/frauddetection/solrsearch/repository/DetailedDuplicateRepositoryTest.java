package com.hsenid.frauddetection.solrsearch.repository;

import com.hsenid.frauddetection.solrsearch.entity.DetailedDuplicate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DetailedDuplicateRepositoryTest {

    @Autowired
    private DetailedDuplicateRepository detailedDuplicateRepository;


    @Test
    public void test(){
        DetailedDuplicate detailedDuplicate= new DetailedDuplicate();
        detailedDuplicate.setCorrelationId(14545455);
        detailedDuplicate.setAppID("APP_4545");
        detailedDuplicate.setD1(12);
        detailedDuplicate.setD7(45);
        detailedDuplicate.setD30(12);
        LocalDate now = LocalDate.now();
        detailedDuplicate.setDate(now);
        detailedDuplicate.setDetails("");
        detailedDuplicateRepository.save(detailedDuplicate);
    }

}