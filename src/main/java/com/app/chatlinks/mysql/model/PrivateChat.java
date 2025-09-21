package com.app.chatlinks.mysql.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "priavte_chat")
public class PrivateChat extends GenericModel{

    @Id
    @Column(name = "pm_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user")
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user")
    private User toUser;

    @Column(name = "pm_content")
    private String chat;

    @Column(name = "pm_date")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date date;

    @Column(name = "pm_seen")
    private String seen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ChatRoom_id")
    private ChatRoom chatRoom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }
}
