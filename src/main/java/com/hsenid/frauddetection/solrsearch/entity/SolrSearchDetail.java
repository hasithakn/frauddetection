package com.hsenid.frauddetection.solrsearch.entity;


import com.hsenid.frauddetection.solrindexer.entity.Status;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(schema = "SolrSearchDetail")
public class SolrSearchDetail {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "date")
    private Date date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "currentDoc")
    private int currentDoc;

    public SolrSearchDetail() {
    }

    public SolrSearchDetail(Date date, Status status, int currentDoc) {
        this.date = date;
        this.status = status;
        this.currentDoc = currentDoc;
    }


    @Override
    public String toString() {
        return "SolrIndexDetail{" +
                "id=" + id +
                ", date=" + date +
                ", status=" + status +
                ", currentDoc=" + currentDoc +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getCurrentDoc() {
        return currentDoc;
    }

    public void setCurrentDoc(int currentDoc) {
        this.currentDoc = currentDoc;
    }
}
