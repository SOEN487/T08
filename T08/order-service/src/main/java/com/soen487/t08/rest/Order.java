package com.soen487.t08.rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="order")
public class Order {
    private int orderId;
    private String customerEmail;
    private String customerAddress;
    private String productName;
    private String productCode;

    public Order(int orderId, String customerEmail, String customerAddress, String productName, String productCode) {
        this.orderId = orderId;
        this.customerEmail = customerEmail;
        this.customerAddress = customerAddress;
        this.productName = productName;
        this.productCode= productCode;
    }

    public Order(Order order) {
        this.orderId = order.getOrderId();
        this.customerEmail = order.getCustomerEmail();
        this.customerAddress = order.getCustomerAddress();
        this.productName = order.getProductName();
        this.productCode= order.getProductCode();
    }
    public Order() { }
    public int getOrderId() {
        return this.orderId;
    }
    public void setOrderId(int newOrderId) {this.orderId=newOrderId;}
    public String getCustomerEmail() {
        return this.customerEmail;
    }
    public void setCustomerEmail(String newCustomerEmail) {this.customerEmail =newCustomerEmail;}
    public String getCustomerAddress() {
        return this.customerAddress;
    }
    public void setCustomerAddress(String newCustomerAddress) {
        this.customerAddress = newCustomerAddress;
    }
    public String getProductName() {
        return this.productName;
    }
    public void setProductName(String newItem) {
        this.customerAddress = newItem;
    }
    public String getProductCode() {
        return this.productCode;
    }
    public void setProductCode(String newProductCode) {
        this.productCode= newProductCode;
    }

    public String toString() {
        return String.format("The order ID is %s, the customer Email is %s, the customer address is %s, the item is %s and the product code is %s",
                this.getOrderId(), this.getCustomerEmail(),this.getCustomerAddress(), this.getProductName(),this.getProductCode());
    }

}
