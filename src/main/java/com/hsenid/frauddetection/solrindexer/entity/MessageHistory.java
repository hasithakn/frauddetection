package com.hsenid.frauddetection.solrindexer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table
public class MessageHistory implements Serializable {
    @Column(
            name = "appId"
    )
    private String appId;
    @Column(
            name = "message", length = 2500
    )
    private String message;

    @Id
    @Column(
            name = "correlationId"
    )
    private String correlationId;
    @Column(
            name = "receiveDate"
    )
    private Timestamp receiveDate;

    public MessageHistory() {
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCorrelationId() {
        return this.correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public Timestamp getReceiveDate() {
        return this.receiveDate;
    }

    public void setReceiveDate(Timestamp receiveDate) {
        this.receiveDate = receiveDate;
    }
}
