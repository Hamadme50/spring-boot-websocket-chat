package com.app.chatlinks.dto;

public class DataDTO extends GenericDTO<DataDTO>{
    private String token;
    private String value;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
