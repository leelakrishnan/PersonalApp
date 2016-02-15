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
@Table(name = "request_url_info")
public class RequestUrlInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 5362789731207057615L;

    public RequestUrlInfo() {
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_url_info_id")
    private Long id;

    @Size(min = 2, max = 100)
    @Column(name = "ip_address")
    private String ipAddress;

    @Size(min = 2, max = 100)
    @Column(name = "operating_system")
    private String operatingSystem;

    @Size(min = 2, max = 100)
    @Column(name = "browser")
    private String browser;

    @Size(min = 2, max = 100)
    @Column(name = "country")
    private String country;

    @Size(min = 2, max = 100)
    @Column(name = "region")
    private String region;

    @Size(min = 2, max = 100)
    @Column(name = "city")
    private String city;

    @Size(min = 2, max = 100)
    @Column(name = "language")
    private String language;

    @Size(min = 2, max = 100)
    @Column(name = "organization")
    private String organization;

    @Size(min = 2, max = 100)
    @Column(name = "host_name")
    private String hostName;

    @NotNull
    @Column(name = "created_date")
    private Date createdDate;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(final String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(final String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(final String browser) {
        this.browser = browser;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(final String organization) {
        this.organization = organization;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(final String hostName) {
        this.hostName = hostName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

}
