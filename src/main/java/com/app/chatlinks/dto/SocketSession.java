package com.app.chatlinks.dto;

public class SocketSession extends GenericDTO<SocketSession>{
    Long userId;
    String token;
    int logins;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getLogins() {
        return logins;
    }

    public void setLogins(int logins) {
        this.logins = logins;
    }
}
