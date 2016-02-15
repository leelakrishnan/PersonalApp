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
        @NamedQuery(name = "get.all.user.status.by.company", query = "SELECT s.id, s.name, s.status FROM CompanyUserStatus cs LEFT JOIN cs.userStatus s WHERE cs.company.id = :companyId order by s.name"),
        @NamedQuery(name = "get.active.user.status.by.company", query = "SELECT s.id, s.name FROM CompanyUserStatus cs LEFT JOIN cs.userStatus s WHERE cs.company.id = :companyId AND s.status = 1 AND cs.status = 1 order by s.name"),
        @NamedQuery(name = "validate.name.user.status.by.company.add", query = "SELECT s.id FROM CompanyUserStatus cs LEFT JOIN cs.userStatus s WHERE s.name = :name AND cs.company.id = :companyId"),
        @NamedQuery(name = "validate.name.user.status.by.company.edit", query = "SELECT s.id FROM CompanyUserStatus cs LEFT JOIN cs.userStatus s WHERE s.name = :name AND s.id != :userStatusId AND cs.company.id = :companyId") })
@Table(name = "company_user_statuses")
public class CompanyUserStatus extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -7048450392574977621L;

    public CompanyUserStatus() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_user_status_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "company_id")
    private Company company;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "user_status_id")
    private UserStatus userStatus;

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

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(final UserStatus userStatus) {
        this.userStatus = userStatus;
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
