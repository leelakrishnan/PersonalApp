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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@NamedQueries({
        @NamedQuery(name = "get.all.brand.by.company", query = "SELECT p.id, p.name, p.status FROM CompanyBrand pc LEFT JOIN pc.brand p WHERE pc.company.id = :companyId order by p.name"),
        @NamedQuery(name = "get.active.brand.by.company", query = "SELECT p.id, p.name FROM CompanyBrand pc LEFT JOIN pc.brand p WHERE pc.company.id = :companyId AND p.status = 1 AND pc.status = 1 order by p.name"),
        @NamedQuery(name = "validate.name.brand.by.company.add", query = "SELECT p.id FROM CompanyBrand pc LEFT JOIN pc.brand p WHERE p.name = :name AND pc.company.id = :companyId"),
        @NamedQuery(name = "validate.name.brand.by.company.edit", query = "SELECT p.id FROM CompanyBrand pc LEFT JOIN pc.brand p WHERE p.name = :name AND p.id != :brandId AND pc.company.id = :companyId") })
@Table(name = "company_brands")
public class CompanyBrand extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 7933883296440671905L;

    public CompanyBrand() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_brand_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "company_id")
    private Company company;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "brand_id")
    private Brand brand;

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

    public Company getCompany() {
        return company;
    }

    public void setCompany(final Company company) {
        this.company = company;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(final Brand brand) {
        this.brand = brand;
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
