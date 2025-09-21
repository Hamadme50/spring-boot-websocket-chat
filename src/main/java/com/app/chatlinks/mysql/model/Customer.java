package com.app.chatlinks.mysql.model;
import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "customer")
public class Customer extends GenericModel{

    @Id
    @Column(name = "customer_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name")
    private String name;

    @Column(name = "customer_email")
    private String email;

    @Column(name = "customer_password")
    private String password;

    @Column(name = "temp_password")
    private String tempPassword;

    @Column(name = "customer_date")
    private Date joinDadte;

    @Column(name = "owner")
    private String owner;

    @Column(name = "paid")
    private String paid;

    public Customer(Long customerId) {
        id = customerId;
    }

    public Customer() {
    }

    public String getTempPassword() {
        return tempPassword;
    }

    public void setTempPassword(String tempPassword) {
        this.tempPassword = tempPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }
}
