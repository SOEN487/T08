package com.soen487.t08.rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="catalog")
public class Catalog {
    /**
     * Class that will be used to hold the data for the Rest API Example
     */
    private int id;
    private String productCode;
    private String productName;
    private String description;
    private double price;
    private static int currentId = 1;

    public Catalog(String productCode, String productName, String description, double price) {
        this.id = currentId++;
        this.productCode = productCode;
        this.productName = productName;
        this.description=description;
        this.price=price;
    }
    public Catalog(Catalog catalog) {
        this.id = catalog.getId();
        this.productCode = catalog.getProductCode();
        this.productName = catalog.getProductName();
        this.description=catalog.getDescription();
        this.price=catalog.getPrice();
      }
    public Catalog(){}
    public int getId() {
        return this.id;
    }
    public void setId(int newId){this.id=newId;}
    public String getProductCode() {
        return this.productCode;
    }
    public void setProductCode(String newCode) {this.productCode =newCode;}
    public String getProductName() {
        return this.productName;
    }
    public void setProductName(String newName) {
        this.productName = newName;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String newDescription) {
        this.productName = newDescription;
    }
    public double getPrice() { return this.price; }
    public void setPrice(double price) {
        this.price = price;
    }

    public String toString() {
        return String.format("The product ID is %s, the code is %s, the name is %s, the description is %s and the price is %f",
                this.getId(), this.getProductCode(),this.getProductName(), this.getDescription(), this.getPrice());
    }
}
