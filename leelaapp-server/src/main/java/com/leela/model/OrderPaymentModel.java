package com.leela.model;

import java.io.Serializable;

public class OrderPaymentModel implements Serializable {

    private static final long serialVersionUID = 2361558417161845546L;

    private Long id;

    private Long paymentTypeId;

    private String paymentType;

    private Double payment;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(final Long paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(final String paymentType) {
        this.paymentType = paymentType;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment(final Double payment) {
        this.payment = payment;
    }

}
