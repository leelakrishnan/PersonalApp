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
        @NamedQuery(name = "get.all.products.by.company", query = "SELECT p.id, p.name, p.status FROM Product p WHERE p.company.id = :companyId order by p.name"),
        @NamedQuery(name = "get.active.products.by.company", query = "SELECT p.id, p.name FROM Product p WHERE p.company.id = :companyId AND p.status = 1 order by p.name"),
        @NamedQuery(name = "get.active.products.by.product.category.and.brand", query = "SELECT p.id, p.name FROM Product p WHERE p.company.id = :companyId AND p.status = 1 AND p.productCategory.id = :productCategoryId AND p.brand.id = :brandId order by p.name"),
        @NamedQuery(name = "validate.name.products.by.company.add", query = "SELECT p.id FROM Product p WHERE p.name = :name AND p.company.id = :companyId"),
        @NamedQuery(name = "validate.name.products.by.company.edit", query = "SELECT p.id FROM Product p WHERE p.name = :name AND p.id != :productId AND p.company.id = :companyId"),
        @NamedQuery(name = "get.product.by.id", query = "SELECT p.id, p.name, p.description, p.shortDescription, p.productCategory.id, p.productSubCategory.id, p.productType.id, p.productSubType.id, p.brand.id, p.company.id, p.status, p.listPrice FROM Product p WHERE p.id = :productId") })
@Table(name = "products")
public class Product extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -6311244722718706121L;

    public Product() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "name")
    public String name;

    @Size(min = 2, max = 100)
    @Column(name = "description")
    public String description;

    @Size(min = 2, max = 100)
    @Column(name = "short_description")
    public String shortDescription;

    @Column(name = "list_price")
    public Double listPrice;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "product_category_id")
    private ProductCategory productCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "product_sub_category_id")
    private ProductSubCategory productSubCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "product_type_id")
    private ProductType productType;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "product_sub_type_id")
    private ProductSubType productSubType;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(final String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Double getListPrice() {
        return listPrice;
    }

    public void setListPrice(final Double listPrice) {
        this.listPrice = listPrice;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(final ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public ProductSubCategory getProductSubCategory() {
        return productSubCategory;
    }

    public void setProductSubCategory(
            final ProductSubCategory productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(final ProductType productType) {
        this.productType = productType;
    }

    public ProductSubType getProductSubType() {
        return productSubType;
    }

    public void setProductSubType(final ProductSubType productSubType) {
        this.productSubType = productSubType;
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(final Company company) {
        this.company = company;
    }

}
