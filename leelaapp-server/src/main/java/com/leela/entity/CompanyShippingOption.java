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
        @NamedQuery(name = "get.all.shipping.option.by.company", query = "SELECT p.id, p.name, p.status FROM CompanyShippingOption pc LEFT JOIN pc.shippingOption p WHERE pc.company.id = :companyId order by p.name"),
        @NamedQuery(name = "get.active.shipping.option.by.company", query = "SELECT p.id, p.name FROM CompanyShippingOption pc LEFT JOIN pc.shippingOption p WHERE pc.company.id = :companyId AND p.status = 1 AND pc.status = 1 order by p.name"),
        @NamedQuery(name = "validate.name.shipping.option.by.company.add", query = "SELECT p.id FROM CompanyShippingOption pc LEFT JOIN pc.shippingOption p WHERE p.name = :name AND pc.company.id = :companyId"),
        @NamedQuery(name = "validate.name.shipping.option.by.company.edit", query = "SELECT p.id FROM CompanyShippingOption pc LEFT JOIN pc.shippingOption p WHERE p.name = :name AND p.id != :shippingOptionId AND pc.company.id = :companyId") })
@Table(name = "company_shipping_options")
public class CompanyShippingOption extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1322560709569322953L;

    public CompanyShippingOption() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_shipping_option_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "company_id")
    private Company company;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "shipping_option_id")
    private ShippingOption shippingOption;

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

    public ShippingOption getShippingOption() {
        return shippingOption;
    }

    public void setShippingOption(final ShippingOption shippingOption) {
        this.shippingOption = shippingOption;
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
