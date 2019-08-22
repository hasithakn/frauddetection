package com.hsenid.frauddetection.solrsearch.observer;


import com.hsenid.frauddetection.solrsearch.subject.Subject;

public abstract class Observer {
    protected Subject subject;
    public abstract void update();
}
