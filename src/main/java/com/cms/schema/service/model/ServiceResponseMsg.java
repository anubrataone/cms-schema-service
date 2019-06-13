package com.cms.schema.service.model;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class ServiceResponseMsg<T> implements Serializable {
    private HttpStatus status;
    private final String msg;
    private T data;


    public ServiceResponseMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CollectionRecordData{" +
                "priority="  +
                ", msg='" + msg + '\'' +
                ", status=" + status +
                '}';
    }
}
