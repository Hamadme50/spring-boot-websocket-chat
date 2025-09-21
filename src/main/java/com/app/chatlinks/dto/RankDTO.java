package com.app.chatlinks.dto;


public class RankDTO extends GenericDTO<RankDTO>{

    private Long chatRoomId;
    private String name;
    private String code;
    private String icon;
    private String kick;
    private String ban;
    private String spam;
    private String mute;
    private String changeNick;
    private String deleteMsg;

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getChangeNick() {
        return changeNick;
    }

    public void setChangeNick(String changeNick) {
        this.changeNick = changeNick;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getKick() {
        return kick;
    }

    public void setKick(String kick) {
        this.kick = kick;
    }

    public String getBan() {
        return ban;
    }

    public void setBan(String ban) {
        this.ban = ban;
    }

    public String getSpam() {
        return spam;
    }

    public void setSpam(String spam) {
        this.spam = spam;
    }

    public String getMute() {
        return mute;
    }

    public void setMute(String mute) {
        this.mute = mute;
    }

    public String getDeleteMsg() {
        return deleteMsg;
    }

    public void setDeleteMsg(String deleteMsg) {
        this.deleteMsg = deleteMsg;
    }
}
