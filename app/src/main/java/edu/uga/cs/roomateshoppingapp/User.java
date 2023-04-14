package edu.uga.cs.roomateshoppingapp;

/**
 * The User class represents a user of the roommate shopping app. It contains the user's personal information such as
 * email, phone number, first name, last name, user ID, household ID, and purse balance.
 *
 * A User object can be created by passing in the user's email, phone number, first name, and last name to the constructor.
 *
 * The User class provides getter and setter methods for all the user's attributes: email, phone number, first name,
 * last name, user ID, household ID, and purse balance.
 *
 * The email and phone number can be updated using the setEmail() and setPhoneNumber() methods. The first name and last name
 * can be updated using the setFirstName() and setLastName() methods. The user ID can be set using the setUserID() method, and
 * the household ID and purse balance can be set using the setHousehold() and setPurse() methods respectively.
 *
 * The getUserID(), getHousehold(), and getPurse() methods are used to retrieve the corresponding attribute values of a User
 * object.
 */
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
