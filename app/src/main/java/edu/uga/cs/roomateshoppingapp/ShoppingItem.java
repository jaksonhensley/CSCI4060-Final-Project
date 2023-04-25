package edu.uga.cs.roomateshoppingapp;

/**
 * This class represents a single Item
 */
public class ShoppingItem {
    private String key;
    private String itemName;
    private int itemQuantity;
    private String itemComments;


    public ShoppingItem()
    {
        this.key = null;
        this.itemName = null;
        this.itemQuantity = 0;
        this.itemComments = null;
    }

    public ShoppingItem( String itemName, int itemQuantity, String itemComments) {
        this.key = null;
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

    public void setItemName(String companyName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return itemQuantity;
    }

    public void setQuantity(int quantity) {
        this.itemQuantity = quantity;
    }

    public String getComments() {
        return itemComments;
    }

    public void setComments(String comments) {
        this.itemComments = comments;
    }



}
