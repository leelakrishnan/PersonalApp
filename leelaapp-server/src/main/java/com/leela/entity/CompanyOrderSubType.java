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
        @NamedQuery(name = "get.all.order.sub.type.by.order.type", query = "SELECT p.id, p.name, p.status FROM CompanyOrderSubType ps LEFT JOIN ps.orderSubType p WHERE p.orderType.id = :orderTypeId AND ps.company.id = :companyId order by p.name"),
        @NamedQuery(name = "get.active.order.sub.type.by.order.type", query = "SELECT p.id, p.name FROM CompanyOrderSubType ps LEFT JOIN ps.orderSubType p WHERE p.orderType.id = :orderTypeId AND ps.company.id = :companyId AND p.status = 1 AND ps.status = 1 order by p.name"),
        @NamedQuery(name = "validate.name.order.sub.type.by.company.add", query = "SELECT p.id FROM CompanyOrderSubType ps LEFT JOIN ps.orderSubType p WHERE p.name = :name AND ps.company.id = :companyId"),
        @NamedQuery(name = "validate.name.order.sub.type.by.company.edit", query = "SELECT p.id FROM CompanyOrderSubType ps LEFT JOIN ps.orderSubType p WHERE p.name = :name AND p.id != :orderSubTypeId AND ps.company.id = :companyId") })
@Table(name = "company_order_sub_types")
public class CompanyOrderSubType extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -3733103447508565325L;

    public CompanyOrderSubType() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_order_sub_type_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "company_id")
    private Company company;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "order_sub_type_id")
    private OrderSubType orderSubType;

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

    public OrderSubType getOrderSubType() {
        return orderSubType;
    }

    public void setOrderSubType(final OrderSubType orderSubType) {
        this.orderSubType = orderSubType;
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
