package com.soen487.t08.rest;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="inventory")
public class InventoryItem {
    /**
     * Class that will be used to hold the data for the Rest API Example
     */
    private int inventoryItemId;
    private String productCode;
    private int availableQuantity;
    private static int currentId = 1;

    public InventoryItem(String productCode, int availableQuantity) {
        this.inventoryItemId = currentId++;
        this.productCode = productCode;
        this.availableQuantity = availableQuantity;
    }
    public InventoryItem(InventoryItem inventoryItem) {
        this.inventoryItemId=inventoryItem.getInventoryItemId();
        this.productCode=inventoryItem.getProductCode();
        this.availableQuantity=inventoryItem.getAvailableQuantity();
    }
    public InventoryItem() { }
    public int getInventoryItemId() {
        return this.inventoryItemId;
    }
    public void setInventoryItemId(int newInventoryItemId) {this.inventoryItemId=newInventoryItemId;}
    public String getProductCode() {
        return this.productCode;
    }
    public void setProductCode(String newProductCode) {this.productCode =newProductCode;}
    public int getAvailableQuantity() {
        return this.availableQuantity;
    }
    public void setAvailableQuantity(int newAvailableQuantity) {this.availableQuantity=newAvailableQuantity;}

    public String toString() {
        return String.format("The inventory item ID is %s, the product code is %s and the available quantity is %s ",
                this.getInventoryItemId(), this.getProductCode(),this.getAvailableQuantity());
    }

}
