package ru.smolyakovroma.reference5.model;

import java.io.Serializable;

public class DocElement implements Serializable{

    private static final long serialVersionUID = -8092278179152856997L;

    private long doc_datetime;
    private int status;
    private Integer id;

    public DocElement() {
    }

    public DocElement(long doc_datetime, int status, Integer id) {

        this.doc_datetime = doc_datetime;
        this.status = status;
        this.id = id;
    }




    public Long getDoc_datetime() {
        return doc_datetime;
    }

    public void setDoc_datetime(long doc_datetime) {
        this.doc_datetime = doc_datetime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


}

