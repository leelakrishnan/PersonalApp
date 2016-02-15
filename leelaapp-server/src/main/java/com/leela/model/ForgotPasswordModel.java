package com.leela.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class ForgotPasswordModel implements Serializable {

    private static final long serialVersionUID = -739554232061511372L;

    @NotBlank
    private Long forgotPasswordRequestLogId;

    @NotBlank
    @Length(min = 6)
    private String password;

    public Long getForgotPasswordRequestLogId() {
        return forgotPasswordRequestLogId;
    }

    public void setForgotPasswordRequestLogId(
            final Long forgotPasswordRequestLogId) {
        this.forgotPasswordRequestLogId = forgotPasswordRequestLogId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

}
