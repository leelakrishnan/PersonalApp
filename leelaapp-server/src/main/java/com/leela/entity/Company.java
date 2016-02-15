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
        @NamedQuery(name = "get.all.companies", query = "SELECT c.id, c.name, c.status FROM Company c order by c.name"),
        @NamedQuery(name = "get.active.companies", query = "SELECT c.id, c.name, c.status FROM Company c WHERE c.status = 1 order by c.name"),
        @NamedQuery(name = "validate.name.company.add", query = "SELECT c.id FROM Company c WHERE c.name = :name"),
        @NamedQuery(name = "validate.name.company.edit", query = "SELECT c.id FROM Company c WHERE c.name = :name AND c.id != :companyId"),
        @NamedQuery(name = "get.company.by.id", query = "SELECT c.id, c.name, a.address1, a.address2, a.country.id, a.state.id, a.city.id, a.otherCity, a.zipcode, c.phone, c.url, c.fax, c.owner.id, c.businessType.id, c.status FROM Company c LEFT JOIN c.address a WHERE c.id = :companyId") })
@Table(name = "companies")
public class Company extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3421980707485373081L;

    public Company() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 500)
    @Column(name = "name")
    public String name;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "address_id")
    private Address address;

    @NotNull
    @Size(min = 2, max = 20)
    @Column(name = "phone")
    public String phone;

    @NotNull
    @Size(min = 2, max = 200)
    @Column(name = "url")
    public String url;

    @Column(name = "fax")
    public String fax;

    @Column(name = "logo")
    public byte[] logo;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "owner")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "business_type_id")
    private BusinessType businessType;

    @NotNull
    @Column(name = "status")
    private Short status;

    @NotNull
    @Column(name = "created_date")
    private Date createdDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "updated_date")
    private Date updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(final Address address) {
        this.address = address;
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

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(final byte[] logo) {
        this.logo = logo;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(final User owner) {
        this.owner = owner;
    }

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(final BusinessType businessType) {
        this.businessType = businessType;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(final Short status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final User createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(final Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public User getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(final User updatedBy) {
        this.updatedBy = updatedBy;
    }

}
