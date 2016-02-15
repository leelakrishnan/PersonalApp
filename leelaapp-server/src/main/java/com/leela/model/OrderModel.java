package com.leela.model;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class OrderModel implements Serializable {

    private static final long serialVersionUID = 3168800951474646986L;

    private Long orderId;

    private String orderType;

    private String orderStatus;

    private String custName;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    private String orderDate;

    private String deliveryDate;

    private String notes;

    @NotBlank
    private Long orderTypeId;

    @NotBlank
    private Long orderStatusId;

    @NotBlank
    private Long customerId;

    @NotBlank
    @Length(min = 2, max = 100)
    private String address1Shipping;

    @Length(min = 2, max = 100)
    private String address2Shipping;

    @NotBlank
    private Long countryIdShipping;

    @NotBlank
    private Long stateIdShipping;

    private Long cityIdShipping;

    @NotBlank
    private String cityShipping;

    @NotBlank
    private String zipcodeShipping;

    @NotBlank
    @Length(min = 2, max = 100)
    private String address1Billing;

    @Length(min = 2, max = 100)
    private String address2Billing;

    @NotBlank
    private Long countryIdBilling;

    @NotBlank
    private Long stateIdBilling;

    private Long cityIdBilling;

    @NotBlank
    private String cityBilling;

    @NotBlank
    private String zipcodeBilling;

    @NotBlank
    private Double subTotal;

    @NotBlank
    private Double shipping;

    @NotBlank
    private Double discount;

    @NotBlank
    private Double total;

    private Double paid;

    private Double due;

    private List<OrderPaymentModel> orderPayments;

    private List<OrderProductModel> orderProducts;

    @NotBlank
    private Short status;

    @NotBlank
    private Long companyId;

    @NotBlank
    private Long userId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(final Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(final String orderType) {
        this.orderType = orderType;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(final String custName) {
        this.custName = custName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(final String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(final String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(final String notes) {
        this.notes = notes;
    }

    public Long getOrderTypeId() {
        return orderTypeId;
    }

    public void setOrderTypeId(final Long orderTypeId) {
        this.orderTypeId = orderTypeId;
    }

    public Long getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(final Long orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(final Long customerId) {
        this.customerId = customerId;
    }

    public String getAddress1Shipping() {
        return address1Shipping;
    }

    public void setAddress1Shipping(final String address1Shipping) {
        this.address1Shipping = address1Shipping;
    }

    public String getAddress2Shipping() {
        return address2Shipping;
    }

    public void setAddress2Shipping(final String address2Shipping) {
        this.address2Shipping = address2Shipping;
    }

    public Long getCountryIdShipping() {
        return countryIdShipping;
    }

    public void setCountryIdShipping(final Long countryIdShipping) {
        this.countryIdShipping = countryIdShipping;
    }

    public Long getStateIdShipping() {
        return stateIdShipping;
    }

    public void setStateIdShipping(final Long stateIdShipping) {
        this.stateIdShipping = stateIdShipping;
    }

    public Long getCityIdShipping() {
        return cityIdShipping;
    }

    public void setCityIdShipping(final Long cityIdShipping) {
        this.cityIdShipping = cityIdShipping;
    }

    public String getCityShipping() {
        return cityShipping;
    }

    public void setCityShipping(final String cityShipping) {
        this.cityShipping = cityShipping;
    }

    public String getZipcodeShipping() {
        return zipcodeShipping;
    }

    public void setZipcodeShipping(final String zipcodeShipping) {
        this.zipcodeShipping = zipcodeShipping;
    }

    public String getAddress1Billing() {
        return address1Billing;
    }

    public void setAddress1Billing(final String address1Billing) {
        this.address1Billing = address1Billing;
    }

    public String getAddress2Billing() {
        return address2Billing;
    }

    public void setAddress2Billing(final String address2Billing) {
        this.address2Billing = address2Billing;
    }

    public Long getCountryIdBilling() {
        return countryIdBilling;
    }

    public void setCountryIdBilling(final Long countryIdBilling) {
        this.countryIdBilling = countryIdBilling;
    }

    public Long getStateIdBilling() {
        return stateIdBilling;
    }

    public void setStateIdBilling(final Long stateIdBilling) {
        this.stateIdBilling = stateIdBilling;
    }

    public Long getCityIdBilling() {
        return cityIdBilling;
    }

    public void setCityIdBilling(final Long cityIdBilling) {
        this.cityIdBilling = cityIdBilling;
    }

    public String getCityBilling() {
        return cityBilling;
    }

    public void setCityBilling(final String cityBilling) {
        this.cityBilling = cityBilling;
    }

    public String getZipcodeBilling() {
        return zipcodeBilling;
    }

    public void setZipcodeBilling(final String zipcodeBilling) {
        this.zipcodeBilling = zipcodeBilling;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(final Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getShipping() {
        return shipping;
    }

    public void setShipping(final Double shipping) {
        this.shipping = shipping;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(final Double discount) {
        this.discount = discount;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(final Double total) {
        this.total = total;
    }

    public Double getPaid() {
        return paid;
    }

    public void setPaid(final Double paid) {
        this.paid = paid;
    }

    public Double getDue() {
        return due;
    }

    public void setDue(final Double due) {
        this.due = due;
    }

    public List<OrderPaymentModel> getOrderPayments() {
        return orderPayments;
    }

    public void setOrderPayments(final List<OrderPaymentModel> orderPayments) {
        this.orderPayments = orderPayments;
    }

    public List<OrderProductModel> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(final List<OrderProductModel> orderProducts) {
        this.orderProducts = orderProducts;
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
