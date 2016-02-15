package com.leela.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

public class UpdateStatusModel implements Serializable {

    private static final long serialVersionUID = 1706739768212401162L;

    @NotBlank
    private Long objectId;

    @NotBlank
    private Short status;

    private Long subObjectId;

    private Long companyId;

    @NotBlank
    private Long userId;

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(final Long objectId) {
        this.objectId = objectId;
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

    public Long getSubObjectId() {
        return subObjectId;
    }

    public void setSubObjectId(final Long subObjectId) {
        this.subObjectId = subObjectId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

}
