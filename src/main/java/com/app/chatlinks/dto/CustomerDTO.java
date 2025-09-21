package com.app.chatlinks.dto;

import java.sql.Date;

public class CustomerDTO extends GenericDTO<CustomerDTO>{

    private String name;
    private String email;
    private String password;
    private String oldPassword;
    private String paid;
    private Date joinDadte;
    private String owner;
    private Integer requestStatus = 0;

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getJoinDadte() {
        return joinDadte;
    }

    public void setJoinDadte(Date joinDadte) {
        this.joinDadte = joinDadte;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(Integer requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
