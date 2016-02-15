package com.leela.entity;

import java.io.Serializable;

public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 3535363418579851155L;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }
}
