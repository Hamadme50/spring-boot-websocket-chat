package com.app.chatlinks.dto.chat;

import java.io.Serializable;

public class GetChatDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String theme;
    private String topic;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
