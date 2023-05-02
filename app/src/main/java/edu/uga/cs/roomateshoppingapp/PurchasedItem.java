package edu.uga.cs.roomateshoppingapp;

/**
 * This class represents a single Item
 */
public class PurchasedItem {
    private String key;
    private String itemName;
    private String itemQuantity;
    private String itemPrice;

    private String person;


    public PurchasedItem()
    {
        this.key = null;
        this.itemName = null;
        this.itemQuantity = null;
        this.itemPrice = null;
        this.person = null;

    }

    public PurchasedItem(String itemName, String itemQuantity, String itemPrice, String person) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemPrice = itemPrice;
        this.person = person;
    }

    public PurchasedItem(String itemKey, String itemName, String itemQuantity, String itemPrice, String person) {
        this.key = itemKey;
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemPrice = itemPrice;
        this.person = person;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String name) {
        this.itemName = name;
    }

    public String getQuantity() {
        return itemQuantity;
    }

    public void setQuantity(String quantity) {
        this.itemQuantity = quantity;
    }

    public String getPrice() {
        return itemPrice;
    }

    public void setPrice(String price) {
        this.itemPrice = price;
    }

    public String getPerson() {return person;}

    public void setPerson(String person) {this.person = person;}



}
