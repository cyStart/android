package com.simbazet.mbizvo.fragments;

public class UsersData {

    private int userID;
    private String userFname;
    private String userLname;
    private String userPhone;
    private String userType;

    public UsersData(int userID, String userFname, String userLname, String userPhone, String userType) {
        this.userID = userID;
        this.userFname = userFname;
        this.userLname = userLname;
        this.userPhone = userPhone;
        this.userType = userType;
    }

    public int getUserID() {
        return userID;
    }

    public String getUserFname() {
        return userFname;
    }

    public String getUserLname() {
        return userLname;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserType() {
        return userType;
    }
}
