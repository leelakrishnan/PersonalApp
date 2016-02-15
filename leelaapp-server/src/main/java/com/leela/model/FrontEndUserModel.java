package com.leela.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class FrontEndUserModel implements Serializable {

    private static final long serialVersionUID = 6914510921333094718L;

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
    @Length(min = 6, max = 30)
    private String password;

    @NotBlank
    private Long userTypeId;

    @NotBlank
    private Long companyId;

    private String lat;

    private String lang;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Long getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(final Long userTypeId) {
        this.userTypeId = userTypeId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(final Long companyId) {
        this.companyId = companyId;
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

}
