package com.app.chatlinks.mysql.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "mainchat")
public class MainChat extends GenericModel{

    @Id
    @Column(name = "mainchat_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id")
    private String chatId;

    @Column(name = "chat_content")
    private String content;

    @Column(name = "chat_username")
    private String userName;

    @Column(name = "chat_userid")
    private Long userId;

    @Column(name = "chatroom_id")
    private Long chatRoomId;

    @Column(name = "chat_namecolor")
    private String nameColor;

    @Column(name = "chat_dp")
    private String dp;

    @Column(name = "chat_textcolor")
    private String textColor;

    @Column(name = "chat_bold")
    private String bold;

    @Column(name = "chat_font")
    private String font;

    @Column(name = "chat_userstatus")
    private String userStatus;

    @Column(name = "chat_type")
    private String type;

    @Column(name = "chat_msgdate")
    private Date msgDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNameColor() {
        return nameColor;
    }

    public void setNameColor(String nameColor) {
        this.nameColor = nameColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getBold() {
        return bold;
    }

    public void setBold(String bold) {
        this.bold = bold;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public Date getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(Date msgDate) {
        this.msgDate = msgDate;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }
}
