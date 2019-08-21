package com.hsenid.frauddetection.solrindexer.entity;


import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(schema = "SolrIndexDetails")
public class SolrIndexDetails {

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

    public SolrIndexDetails() {
    }

    public SolrIndexDetails(Date date, Status status, int currentDoc) {
        this.date = date;
        this.status = status;
        this.currentDoc = currentDoc;
    }

    public enum Status {
        PENDING,
        STARTED,
        FINISHED,
    }


    @Override
    public String toString() {
        return "SolrIndexDetails{" +
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
