package com.xioawu.studentsys;

public class User {
    private String username;
    private String password;
    private String personID;
    private String phtoneNumber;

    public User() {
    }

    public User(String username, String password, String personID, String phtoneNumber) {
        this.username = username;
        this.password = password;
        this.personID = personID;
        this.phtoneNumber = phtoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPersonID() {
        return personID;
    }

    public String getPhtoneNumber() {
        return phtoneNumber;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setPhtoneNumber(String phtoneNumber) {
        this.phtoneNumber = phtoneNumber;
    }
}
