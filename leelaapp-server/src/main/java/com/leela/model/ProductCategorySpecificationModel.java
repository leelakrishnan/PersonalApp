package com.leela.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

public class ProductCategorySpecificationModel implements Serializable {

    private static final long serialVersionUID = 5613497659990990842L;

    private Long id;

    private String name;

    @NotBlank
    private Long productCategoryId;

    @NotBlank
    private Long productSpecificationId;

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

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(final Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public Long getProductSpecificationId() {
        return productSpecificationId;
    }

    public void setProductSpecificationId(final Long productSpecificationId) {
        this.productSpecificationId = productSpecificationId;
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
