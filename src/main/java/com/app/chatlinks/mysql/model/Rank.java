package com.app.chatlinks.mysql.model;

import javax.persistence.*;

@Entity
@Table(name = "rank")
public class Rank extends GenericModel{

    @Id
    @Column(name = "rank_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    @Column(name = "rank_name")
    private String name;

    @Column(name = "rank_code")
    private String code;

    @Column(name = "rank_icon")
    private String icon;

    @Column(name = "rank_kick")
    private String kick;

    @Column(name = "rank_ban")
    private String ban;

    @Column(name = "rank_spam")
    private String spam;

    @Column(name = "rank_mute")
    private String mute;

    @Column(name = "rank_changenick")
    private String changeNick;

    @Column(name = "rank_del_msg")
    private String deleteMsg;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
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

    public String getChangeNick() {
        return changeNick;
    }

    public void setChangeNick(String changeNick) {
        this.changeNick = changeNick;
    }
}
