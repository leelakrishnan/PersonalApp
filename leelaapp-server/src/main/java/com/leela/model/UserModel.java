package com.leela.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class UserModel implements Serializable {

    private static final long serialVersionUID = 2734682406471749219L;

    private Long id;

    @NotBlank
    @Length(min = 2, max = 50)
    private String firstName;

    @NotBlank
    @Length(min = 2, max = 50)
    private String lastName;

    @NotBlank
    @Length(min = 2, max = 200)
    private String email;

    @NotBlank
    @Length(min = 2, max = 20)
    private String phone;

    @NotBlank
    private Short gender;

    @NotBlank
    private String birthDate;

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

    @NotBlank
    private String city;

    @NotBlank
    private String zipcode;

    @NotBlank
    private Long companyId;

    @NotBlank
    private Long userStatusId;

    @NotBlank
    private Long userTypeId;

    @NotBlank
    private Short status;

    private String userName;

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public Short getGender() {
        return gender;
    }

    public void setGender(final Short gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(final String birthDate) {
        this.birthDate = birthDate;
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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(final Long companyId) {
        this.companyId = companyId;
    }

    public Long getUserStatusId() {
        return userStatusId;
    }

    public void setUserStatusId(final Long userStatusId) {
        this.userStatusId = userStatusId;
    }

    public Long getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(final Long userTypeId) {
        this.userTypeId = userTypeId;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(final Short status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

}
