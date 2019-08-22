package com.hsenid.frauddetection.solrsearch.subject;


import com.hsenid.frauddetection.solrsearch.entity.DetailedDuplicate;
import com.hsenid.frauddetection.solrsearch.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    private ArrayList<Observer> observers = new ArrayList<>();
    private List<DetailedDuplicate> dbRows = new ArrayList<>();

    public List<DetailedDuplicate> getDBRows() {
        return dbRows;
    }

    public void setDBRows(List<DetailedDuplicate> dbRows) {
        this.dbRows = dbRows;
        notifyAllObservers();
    }

    public void attach(Observer o) {
        observers.add(o);
    }

    public void notifyAllObservers() {
        observers.forEach(Observer::update);
    }

}
