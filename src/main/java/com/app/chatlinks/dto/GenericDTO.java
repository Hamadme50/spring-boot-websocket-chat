package com.app.chatlinks.dto;

import java.io.Serializable;

public class GenericDTO<T> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    protected Long id;
    private String response;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}