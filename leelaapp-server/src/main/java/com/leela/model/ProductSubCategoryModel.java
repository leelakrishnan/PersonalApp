package com.leela.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class ProductSubCategoryModel implements Serializable {

    private static final long serialVersionUID = 4667487460698574437L;

    @NotBlank
    @Length(min = 2, max = 100)
    private String name;

    @NotBlank
    private Short status;

    @NotBlank
    private Long productCategoryId;

    @NotBlank
    private Long companyId;

    @NotBlank
    private Long userId;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(final Short status) {
        this.status = status;
    }

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(final Long productCategoryId) {
        this.productCategoryId = productCategoryId;
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
