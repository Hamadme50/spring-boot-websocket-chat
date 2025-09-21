package com.app.chatlinks.mysql.model;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "chatroom")
public class ChatRoom extends GenericModel{

    @Id
    @Column(name = "chatroom_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "chatroom_Name")
    private String name;

    @Column(name = "chatroom_domain")
    private String domain;

    @Column(name = "chatroom_theme")
    private String theme;

    @Column(name = "chatroom_paid")
    private String paid;

    @Column(name = "chatroom_date")
    private Date date;

    @Column(name = "chatroom_status")
    private String status;

    @Column(name = "chatroom_topic")
    private String topic;

    @Column(name = "chatroom_antispam")
    private String antiSpam;

    @Column(name = "radio")
    private String radio;

    @Column(name = "guest")
    private String guest;

    @Column(name = "register")
    private String register;

    @Column(name = "design")
    private String design;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getAntiSpam() {
        return antiSpam;
    }

    public void setAntiSpam(String antiSpam) {
        this.antiSpam = antiSpam;
    }

    public String getRadio() {
        return radio;
    }

    public void setRadio(String radio) {
        this.radio = radio;
    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public String getDesign() {
        return design;
    }

    public void setDesign(String design) {
        this.design = design;
    }
}
