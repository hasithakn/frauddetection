package com.hsenid.frauddetection.solrindexer.entity;


import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(schema = "SolrIndexDetail")
public class SolrIndexDetail {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "currentDoc")
    private int currentDoc;

    public SolrIndexDetail() {
    }

    public SolrIndexDetail(LocalDate date, Status status, int currentDoc) {
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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
