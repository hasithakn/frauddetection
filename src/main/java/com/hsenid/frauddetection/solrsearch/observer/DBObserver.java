package com.hsenid.frauddetection.solrsearch.observer;

import com.hsenid.frauddetection.solrsearch.subject.Subject;
import com.hsenid.frauddetection.solrsearch.repository.DetailedDuplicateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class DBObserver extends Observer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBObserver.class);

//    @Autowired
    public DetailedDuplicateRepository detailedDuplicateRepository;

    public DBObserver(Subject subject) {
        this.subject = subject;
        subject.attach(this);
    }

    @Override
    public void update() {
        LOGGER.info("Updated detailed duplicates" + subject.getDBRows().size());
        LOGGER.info("Adding to DB detailed duplicates");
        detailedDuplicateRepository.saveAll(subject.getDBRows());
        LOGGER.info("saved detailed duplicates");
    }

}
