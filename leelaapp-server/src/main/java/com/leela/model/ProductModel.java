package com.leela.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class ProductModel implements Serializable {

    private static final long serialVersionUID = -3824187067172961479L;

    private Long id;

    @NotBlank
    @Length(min = 2, max = 100)
    private String name;

    @Length(min = 2, max = 100)
    public String description;

    @Length(min = 2, max = 100)
    public String shortDescription;

    public Double listPrice;

    @NotBlank
    private Long productCategoryId;

    @NotBlank
    private Long productSubCategoryId;

    @NotBlank
    private Long productTypeId;

    @NotBlank
    private Long productSubTypeId;

    @NotBlank
    private Long brandId;

    @NotBlank
    private Short status;

    @NotBlank
    private Long companyId;

    @NotBlank
    private Long userId;

    public Long getId() {
        return id;
    }

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

    public Double getListPrice() {
        return listPrice;
    }

    public void setListPrice(final Double listPrice) {
        this.listPrice = listPrice;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(final String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(final Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public Long getProductSubCategoryId() {
        return productSubCategoryId;
    }

    public void setProductSubCategoryId(final Long productSubCategoryId) {
        this.productSubCategoryId = productSubCategoryId;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(final Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Long getProductSubTypeId() {
        return productSubTypeId;
    }

    public void setProductSubTypeId(final Long productSubTypeId) {
        this.productSubTypeId = productSubTypeId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(final Long brandId) {
        this.brandId = brandId;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(final Short status) {
        this.status = status;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(final Long companyId) {
        this.companyId = companyId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

}
