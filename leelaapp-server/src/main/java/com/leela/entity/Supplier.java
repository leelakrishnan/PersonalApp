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
        @NamedQuery(name = "get.all.suppliers.by.company", query = "SELECT s.id, s.name, s.status FROM Supplier s WHERE s.company.id = :companyId order by s.name"),
        @NamedQuery(name = "get.active.suppliers.by.company", query = "SELECT s.id, s.name FROM Supplier s WHERE s.company.id = :companyId AND s.status = 1 order by s.name"),
        @NamedQuery(name = "validate.name.suppliers.by.company.add", query = "SELECT s.id FROM Supplier s WHERE s.name = :name AND s.company.id = :companyId"),
        @NamedQuery(name = "validate.name.suppliers.by.company.edit", query = "SELECT s.id FROM Supplier s WHERE s.name = :name AND s.id != :supplierId AND s.company.id = :companyId"),
        @NamedQuery(name = "get.supplier.by.id", query = "SELECT s.id, s.name, a.address1, a.address2, a.country.id, a.state.id, a.city.id, a.otherCity, a.zipcode, s.company.id, s.status, s.phone, s.url, s.fax FROM Supplier s LEFT JOIN s.address a WHERE s.id = :supplierId") })
@Table(name = "suppliers")
public class Supplier extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -3868854508738803497L;

    public Supplier() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "name")
    public String name;

    @Column(name = "logo")
    public byte[] logo;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "address_id")
    private Address address;

    @NotNull
    @Size(min = 2, max = 20)
    @Column(name = "phone")
    public String phone;

    @Column(name = "url")
    public String url;

    @Column(name = "fax")
    public String fax;

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

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "company_id")
    private Company company;

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

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(final byte[] logo) {
        this.logo = logo;
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(final Company company) {
        this.company = company;
    }

}
