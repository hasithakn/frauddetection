package com.hsenid.frauddetection.solrindexer.entity;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Date;


@SolrDocument(solrCoreName = "experiment4")
public class SolrEntity {

    @Field
    private String app_id;

    @Field
    private String sms;

    @Id
    @Field
    private String correlationId;

    @Field
    private Date datetime;

    @Field
    private int termCount;

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public int getTermCount() {
        return termCount;
    }

    public void setTermCount(int termCount) {
        this.termCount = termCount;
    }

    @Override
    public String toString() {
        return "SolrEntity{" +
                "app_id='" + app_id + '\'' +
                ", sms='" + sms + '\'' +
                ", correlationId='" + correlationId + '\'' +
                ", datetime='" + datetime + '\'' +
                ", termCount=" + termCount +
                '}';
    }
}
