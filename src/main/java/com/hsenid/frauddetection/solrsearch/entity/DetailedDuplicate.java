package com.hsenid.frauddetection.solrsearch.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "DetailedDuplicate")
public class DetailedDuplicate {

    @Column(name = "appId")
    private String appID;

    @Id
    @Column(name = "correlationId")
    private long correlationId;

    @Column(name = "d1")
    private int D1;

    @Column(name = "d7")
    private int D7;

    @Column(name = "d30")
    private int D30;

    @Column(name = "details")
    private String Details;

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public long getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(long correlationId) {
        this.correlationId = correlationId;
    }

    public int getD1() {
        return D1;
    }

    public void setD1(int d1) {
        D1 = d1;
    }

    public int getD7() {
        return D7;
    }

    public void setD7(int d7) {
        D7 = d7;
    }

    public int getD30() {
        return D30;
    }

    public void setD30(int d30) {
        D30 = d30;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }
}
