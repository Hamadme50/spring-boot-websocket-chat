package com.app.chatlinks.mysql.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
public class User extends GenericModel{

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_rank")
    private Rank rank;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_password")
    private String password;

    @Column(name = "temp_password")
    private String tempPassword;

    @Column(name = "user_joindate")
    private Date joinDate;

    @Column(name = "user_status")
    private String status;

    @Column(name = "user_activitydate")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date activityDate;

    @Column(name = "user_birthdate")
    private Date birthDate;

    @Column(name = "user_gender")
    private String gender;

    @Column(name = "user_dp")
    private String dp;

    @Column(name = "user_rj")
    private String rj;

    @Column(name = "user_ip")
    private String ip;

    @Column(name = "user_spam")
    private String spam;

    @Column(name = "user_ban")
    private String ban;

    @Column(name = "user_tweet")
    private String tweet;

    @Column(name = "user_about")
    private String about;

    @Column(name = "user_name_color")
    private String nameColor;

    @Column(name = "user_kick")
    private String kick;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_points")
    private Double points;

    @Column(name = "user_font")
    private String font;

    @Column(name = "user_text_color")
    private String textColor;

    @Column(name = "user_bold")
    private String bold;

    @Column(name = "owner")
    private String owner;

    @Column(name = "session")
    private String session;

    @Column(name = "file")
    private String file;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public java.util.Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(java.util.Date activityDate) {
        this.activityDate = activityDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getRj() {
        return rj;
    }

    public void setRj(String rj) {
        this.rj = rj;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSpam() {
        return spam;
    }

    public void setSpam(String spam) {
        this.spam = spam;
    }

    public String getBan() {
        return ban;
    }

    public void setBan(String ban) {
        this.ban = ban;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getNameColor() {
        return nameColor;
    }

    public void setNameColor(String nameColor) {
        this.nameColor = nameColor;
    }

    public String getKick() {
        return kick;
    }

    public void setKick(String kick) {
        this.kick = kick;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTempPassword() {
        return tempPassword;
    }

    public void setTempPassword(String tempPassword) {
        this.tempPassword = tempPassword;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
