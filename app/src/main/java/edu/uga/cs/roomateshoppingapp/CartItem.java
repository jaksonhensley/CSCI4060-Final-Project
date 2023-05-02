package edu.uga.cs.roomateshoppingapp;

/**
 * This class represents a single Item
 */
public class CartItem {
    private String key;
    private String itemName;
    private String itemQuantity;
    private String itemPrice;


    public CartItem()
    {
        this.key = null;
        this.itemName = null;
        this.itemQuantity = null;
        this.itemPrice = null;
    }

    public CartItem(String itemName, String itemQuantity, String itemPrice) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemPrice = itemPrice;
    }

    public CartItem(String itemKey, String itemName, String itemQuantity, String itemPrice) {
        this.key = itemKey;
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemPrice = itemPrice;
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



}
