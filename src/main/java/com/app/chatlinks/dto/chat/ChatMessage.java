package com.app.chatlinks.dto.chat;

import java.util.Date;

/**
 * Created by rajeevkumarsingh on 24/07/17.
 */
public class ChatMessage {
    private String sender;
    private MessageType type;
    private String id;
    private String content;
    private String userName;
    private Long userId;
    private String nameColor;
    private String textColor;
    private String font;
    private String bold;
    private String dp;
    private String image;
    private ChatUserDTO user;
    private String userStatus;
    private Long chatRoomId;
    private Date msgDate;

    public enum MessageType {
        CHAT,
        JOIN,
        JOINMSG,
        LEAVE,
        DELETE,
        SPAM,
        SAMEUSER,
        REPORT,
        IMAGE,
        CLEAR
    }

    public Date getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(Date msgDate) {
        this.msgDate = msgDate;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public ChatUserDTO getUser() {
        return user;
    }

    public void setUser(ChatUserDTO user) {
        this.user = user;
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

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getBold() {
        return bold;
    }

    public void setBold(String bold) {
        this.bold = bold;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
