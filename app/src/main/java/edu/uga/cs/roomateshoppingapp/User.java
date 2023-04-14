package edu.uga.cs.roomateshoppingapp;

public class User {

    private String email;
    private String phoneNumber;
    private int userID;
    private String firstName;
    private String lastName;
    private int household;
    private int purse;

    public User(String email, String phoneNumber, String firstName, String lastName) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    } // User Constructor

    public void setEmail(String email) {
        this.email = email;
    } // updateEmail()

    public String getEmail() {
        return this.email;
    } // getEmail()

    public void setHousehold(int household) {
        this.household = household;
    } // setHousehold()

    public int getHousehold() {
        return household;
    } // getHousehold()

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    } // setFirstName()

    public String getFirstName() {
        return firstName;
    } // getFirstName()

    public void setLastName(String lastName) {
        this.lastName = lastName;
    } // setLastName()

    public String getLastName() {
        return lastName;
    } // getLastName()

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    } // setPhoneNumber()

    public String getPhoneNumber() {
        return phoneNumber;
    } // getPhoneNumber()

    public void setUserID(int userID) {
        this.userID = userID;
    } // setUserID()

    public int getUserID() {
        return userID;
    } // getUserID()

    public void setPurse(int purse) {
        this.purse = purse;
    } // setPurse()

    public int getPurse() {
        return purse;
    } // getPurse()

} // User Class
