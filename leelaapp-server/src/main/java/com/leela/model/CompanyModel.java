package com.leela.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class CompanyModel implements Serializable {

    private static final long serialVersionUID = 922858806010224082L;

    private Long id;

    @NotBlank
    @Length(min = 2, max = 100)
    private String name;

    @NotBlank
    @Length(min = 2, max = 100)
    private String address1;

    @Length(min = 2, max = 100)
    private String address2;

    @NotBlank
    private Long countryId;

    @NotBlank
    private Long stateId;

    private Long cityId;

    @Size(min = 2, max = 100)
    private String city;

    @NotBlank
    @Size(min = 2, max = 10)
    private String zipcode;

    @NotNull
    @Length(min = 2, max = 20)
    public String phone;

    @NotNull
    @Length(min = 2, max = 200)
    public String url;

    @Length(min = 2, max = 20)
    public String fax;

    @NotBlank
    private Short status;

    private Long ownerId;

    private Long businessTypeId;

    @NotBlank
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(final String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(final String address2) {
        this.address2 = address2;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(final Long countryId) {
        this.countryId = countryId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(final Long stateId) {
        this.stateId = stateId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(final Long cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(final String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(final String fax) {
        this.fax = fax;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(final Short status) {
        this.status = status;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(final Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(final Long businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

}
