package edu.uga.cs.roomateshoppingapp;

/**
 * This class represents a single Item
 */
public class ShoppingItem {
    private String key;
    private String itemName;
    private String itemQuantity;
    private String itemComments;


    public ShoppingItem()
    {
        this.key = null;
        this.itemName = null;
        this.itemQuantity = null;
        this.itemComments = null;
    }

    public ShoppingItem( String itemName, String itemQuantity, String itemComments) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemComments = itemComments;
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

    public String getComments() {
        return itemComments;
    }

    public void setComments(String comments) {
        this.itemComments = comments;
    }



}
