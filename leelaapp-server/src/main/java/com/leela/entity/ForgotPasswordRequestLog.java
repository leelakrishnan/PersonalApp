package com.leela.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@NamedQueries({
        @NamedQuery(name = "get.inactive.forgot.password.request.by.code", query = "SELECT f.id FROM ForgotPasswordRequestLog f WHERE f.code = :code AND f.status = 0") })
@Table(name = "forgot_password_request_log")
public class ForgotPasswordRequestLog extends BaseEntity
        implements Serializable {

    private static final long serialVersionUID = 4170042703898118900L;

    public ForgotPasswordRequestLog() {
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "forgot_password_request_log_id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 200)
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "created_date")
    private Date createdDate;

    @Size(min = 2, max = 50)
    @Column(name = "lat")
    private String lat;

    @Size(min = 2, max = 50)
    @Column(name = "lang")
    private String lang;

    @Size(min = 2, max = 20)
    @Column(name = "ip_address")
    private String ipAddress;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "company_id")
    private Company company;

    @Size(min = 2, max = 100)
    @Column(name = "code")
    private String code;

    @NotNull
    @Column(name = "status")
    private Short status;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(final String lat) {
        this.lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(final String lang) {
        this.lang = lang;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(final String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(final Company company) {
        this.company = company;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(final Short status) {
        this.status = status;
    }

}
