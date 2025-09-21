package com.app.chatlinks.dto.chat;

import com.app.chatlinks.dto.GenericDTO;

import java.util.Date;

public class PrivateMessage  extends GenericDTO<PrivateMessage> {
    private Long fromUserId;
    private String fromUserName;
    private String fromNameColor;
    private String fromTextColor;
    private String fromTextFont;
    private String fromDp;
    private Long toUserId;
    private String content;
    private String seen;
    private Date msgDate;
    private MessageType msgType;
    private Long chatRoomId;
    private String image;

    public enum MessageType {
        CHAT,
        KICK,
        BAN,
        MUTE,
        UNMUTE,
        RECONNECT,
        SPAMMER,
        UNSPAMMER,
        UPDATERJ,
        RELOAD,
        JOIN,
        IMAGE,
        GETUSERS
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public MessageType getMsgType() {
        return msgType;
    }

    public void setMsgType(MessageType msgType) {
        this.msgType = msgType;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getFromNameColor() {
        return fromNameColor;
    }

    public void setFromNameColor(String fromNameColor) {
        this.fromNameColor = fromNameColor;
    }

    public String getFromTextColor() {
        return fromTextColor;
    }

    public void setFromTextColor(String fromTextColor) {
        this.fromTextColor = fromTextColor;
    }

    public String getFromTextFont() {
        return fromTextFont;
    }

    public void setFromTextFont(String fromTextFont) {
        this.fromTextFont = fromTextFont;
    }

    public String getFromDp() {
        return fromDp;
    }

    public void setFromDp(String fromDp) {
        this.fromDp = fromDp;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public Date getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(Date msgDate) {
        this.msgDate = msgDate;
    }
}
