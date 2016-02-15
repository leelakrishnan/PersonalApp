package com.leela.model;

import java.io.Serializable;

public class OrderProductModel implements Serializable {

    private static final long serialVersionUID = -7998216245985476575L;

    private Long id;

    private Long productId;

    private String product;

    private Long orderSubTypeId;

    private String orderSubType;

    private Double price;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(final Long productId) {
        this.productId = productId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(final String product) {
        this.product = product;
    }

    public Long getOrderSubTypeId() {
        return orderSubTypeId;
    }

    public void setOrderSubTypeId(final Long orderSubTypeId) {
        this.orderSubTypeId = orderSubTypeId;
    }

    public String getOrderSubType() {
        return orderSubType;
    }

    public void setOrderSubType(final String orderSubType) {
        this.orderSubType = orderSubType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(final Double price) {
        this.price = price;
    }

}
