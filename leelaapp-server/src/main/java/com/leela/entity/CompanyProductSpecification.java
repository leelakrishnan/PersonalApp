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
        @NamedQuery(name = "get.all.product.specification.by.product.specification.group", query = "SELECT p.id, p.name, p.status FROM CompanyProductSpecification ps LEFT JOIN ps.productSpecification p WHERE p.productSpecificationGroup.id = :productSpecificationGroupId AND ps.company.id = :companyId order by p.name"),
        @NamedQuery(name = "get.active.product.specification.by.product.specification.group", query = "SELECT p.id, p.name FROM CompanyProductSpecification ps LEFT JOIN ps.productSpecification p WHERE p.productSpecificationGroup.id = :productSpecificationGroupId AND ps.company.id = :companyId AND p.status = 1 AND ps.status = 1 order by p.name"),
        @NamedQuery(name = "validate.name.product.specification.by.company.add", query = "SELECT p.id FROM CompanyProductSpecification ps LEFT JOIN ps.productSpecification p WHERE p.name = :name AND ps.company.id = :companyId"),
        @NamedQuery(name = "validate.name.product.specification.by.company.edit", query = "SELECT p.id FROM CompanyProductSpecification ps LEFT JOIN ps.productSpecification p WHERE p.name = :name AND p.id != :productSpecificationId AND ps.company.id = :companyId") })
@Table(name = "company_product_specifications")
public class CompanyProductSpecification extends BaseEntity
        implements Serializable {

    private static final long serialVersionUID = 210429835278694846L;

    public CompanyProductSpecification() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_product_specification_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "company_id")
    private Company company;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "product_specification_id")
    private ProductSpecification productSpecification;

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

    public ProductSpecification getProductSpecification() {
        return productSpecification;
    }

    public void setProductSpecification(
            final ProductSpecification productSpecification) {
        this.productSpecification = productSpecification;
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
