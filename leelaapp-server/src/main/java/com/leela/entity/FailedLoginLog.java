package com.leela.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "failed_login_log")
public class FailedLoginLog extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -7603501628334705066L;

    public FailedLoginLog() {
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "failed_login_log_id")
    private Long id;

    @Size(min = 2, max = 100)
    @Column(name = "login_user_name")
    private String loginUserName;

    @Size(min = 2, max = 100)
    @Column(name = "login_password")
    private String loginPassword;

    @NotNull
    @Column(name = "login_date_time")
    private Date loginDateTime;

    @Size(min = 2, max = 20)
    @Column(name = "ip_address")
    private String ipAddress;

    @Size(min = 2, max = 500)
    @Column(name = "reason")
    private String reason;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(final String loginUserName) {
        this.loginUserName = loginUserName;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(final String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public Date getLoginDateTime() {
        return loginDateTime;
    }

    public void setLoginDateTime(final Date loginDateTime) {
        this.loginDateTime = loginDateTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(final String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(final String reason) {
        this.reason = reason;
    }

}
