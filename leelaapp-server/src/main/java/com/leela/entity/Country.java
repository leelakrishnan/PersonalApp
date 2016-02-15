package com.leela.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@NamedQueries({
        @NamedQuery(name = "get.all.countries", query = "SELECT c.id, c.countryName FROM Country c") })
@Table(name = "countries")
public class Country extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 684030620466734335L;

    public Country() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "country_name")
    private String countryName;

    @NotNull
    @Column(name = "status")
    private Short status;

    @Column(name = "country_flag")
    private byte[] countryFlag;

    @Size(min = 2, max = 2)
    @Column(name = "iso2_code")
    private String iso2Code;

    @Size(min = 3, max = 3)
    @Column(name = "iso3_code")
    private String iso3Code;

    @Size(min = 2, max = 6)
    @Column(name = "dial_code")
    private String dialCode;

    @Size(min = 2, max = 10)
    @Column(name = "Currency_code")
    private String currencyCode;

    @Size(min = 2, max = 100)
    @Column(name = "Currency_desc")
    private String currencyDesc;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(final String countryName) {
        this.countryName = countryName;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(final Short status) {
        this.status = status;
    }

    public byte[] getCountryFlag() {
        return countryFlag;
    }

    public void setCountryFlag(final byte[] countryFlag) {
        this.countryFlag = countryFlag;
    }

    public String getIso2Code() {
        return iso2Code;
    }

    public void setIso2Code(final String iso2Code) {
        this.iso2Code = iso2Code;
    }

    public String getIso3Code() {
        return iso3Code;
    }

    public void setIso3Code(final String iso3Code) {
        this.iso3Code = iso3Code;
    }

    public String getDialCode() {
        return dialCode;
    }

    public void setDialCode(final String dialCode) {
        this.dialCode = dialCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(final String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyDesc() {
        return currencyDesc;
    }

    public void setCurrencyDesc(final String currencyDesc) {
        this.currencyDesc = currencyDesc;
    }

}
