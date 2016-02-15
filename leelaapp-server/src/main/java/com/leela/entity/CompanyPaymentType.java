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
        @NamedQuery(name = "get.all.payment.type.by.company", query = "SELECT o.id, o.name, o.status FROM CompanyPaymentType ot LEFT JOIN ot.paymentType o WHERE ot.company.id = :companyId order by o.name"),
        @NamedQuery(name = "get.active.payment.type.by.company", query = "SELECT o.id, o.name FROM CompanyPaymentType ot LEFT JOIN ot.paymentType o WHERE ot.company.id = :companyId AND o.status = 1 AND ot.status = 1 order by o.name"),
        @NamedQuery(name = "validate.name.payment.type.by.company.add", query = "SELECT o.id FROM CompanyPaymentType ot LEFT JOIN ot.paymentType o WHERE o.name = :name AND ot.company.id = :companyId"),
        @NamedQuery(name = "validate.name.payment.type.by.company.edit", query = "SELECT o.id FROM CompanyPaymentType ot LEFT JOIN ot.paymentType o WHERE o.name = :name AND o.id != :paymentTypeId AND ot.company.id = :companyId") })
@Table(name = "company_payment_types")
public class CompanyPaymentType extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -2015485908306503224L;

    public CompanyPaymentType() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_payment_type_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "company_id")
    private Company company;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "payment_type_id")
    private PaymentType paymentType;

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

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(final PaymentType paymentType) {
        this.paymentType = paymentType;
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
