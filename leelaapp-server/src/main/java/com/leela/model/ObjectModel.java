package com.leela.model;

import java.io.Serializable;

public class ObjectModel implements Serializable {

    private static final long serialVersionUID = 6049845938107535856L;

    private Long objectId;

    private String objectName;

    private Short status;

    private Long subObjectId;

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(final Long objectId) {
        this.objectId = objectId;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(final String objectName) {
        this.objectName = objectName;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(final Short status) {
        this.status = status;
    }

    public Long getSubObjectId() {
        return subObjectId;
    }

    public void setSubObjectId(final Long subObjectId) {
        this.subObjectId = subObjectId;
    }

}
