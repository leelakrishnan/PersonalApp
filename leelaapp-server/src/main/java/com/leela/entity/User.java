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
        @NamedQuery(name = "get.all.users.by.company.and.user.type", query = "SELECT u.id, CONCAT(u.lastName, ', ', u.firstName) AS name, u.status FROM User u WHERE u.company.id = :companyId AND u.userType.id = :userTypeId order by name"),
        @NamedQuery(name = "get.active.users.by.company.and.user.type", query = "SELECT u.id, CONCAT(u.lastName, ', ', u.firstName) AS name FROM User u WHERE u.company.id = :companyId AND u.userType.id = :userTypeId AND u.status = 1 order by name"),
        @NamedQuery(name = "validate.phone.user.by.company.add", query = "SELECT u.id FROM User u WHERE u.phone = :phone AND u.company.id = :companyId"),
        @NamedQuery(name = "validate.phone.user.by.company.edit", query = "SELECT u.id FROM User u WHERE u.phone = :phone AND u.id != :userId AND u.company.id = :companyId"),
        @NamedQuery(name = "validate.email.user.by.company.add", query = "SELECT u.id FROM User u WHERE u.email = :email AND u.company.id = :companyId"),
        @NamedQuery(name = "validate.email.user.by.company.edit", query = "SELECT u.id FROM User u WHERE u.email = :email AND u.id != :userId AND u.company.id = :companyId"),
        @NamedQuery(name = "get.user.by.id", query = "SELECT u.id, u.firstName, u.lastName, u.email, u.phone, u.gender, u.birthDate, a.address1, a.address2, a.country.id, a.state.id, a.city.id, a.otherCity, a.zipcode, u.company.id, u.userStatus.id, u.userType.id, u.status, u.userName FROM User u LEFT JOIN u.address a WHERE u.id = :userId"),
        @NamedQuery(name = "get.users.by.phone.email.and.company", query = "SELECT u.id FROM User u WHERE (u.phone = :phone AND u.company.id = :companyId) OR (u.email = :email AND u.company.id = :companyId)"),
        @NamedQuery(name = "get.users.by.email.and.company", query = "SELECT u.id FROM User u WHERE u.email = :email AND u.company.id = :companyId") })
@Table(name = "users")
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -8360491794147539538L;

    public User() {
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Size(min = 2, max = 200)
    @Column(name = "email")
    private String email;

    @NotNull
    @Size(min = 2, max = 20)
    @Column(name = "phone")
    private String phone;

    @NotNull
    @Column(name = "gender")
    private Short gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "address_id")
    private Address address;

    @Size(max = 50)
    @Column(name = "user_name")
    private String userName;

    @Size(max = 500)
    @Column(name = "password")
    private String password;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "user_type_id")
    private UserType userType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "user_status_id")
    private UserStatus userStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "photo_id")
    private UserPhoto userPhoto;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "company_id")
    private Company company;

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

    @Column(name = "birth_date")
    private Date birthDate;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(final UserType userType) {
        this.userType = userType;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(final UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public UserPhoto getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(final UserPhoto userPhoto) {
        this.userPhoto = userPhoto;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(final Company company) {
        this.company = company;
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(final Date birthDate) {
        this.birthDate = birthDate;
    }

}
