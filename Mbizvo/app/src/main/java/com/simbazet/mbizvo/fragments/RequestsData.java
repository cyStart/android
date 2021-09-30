package com.simbazet.mbizvo.fragments;

public class RequestsData {

    private int faultID;
    private String faultTitle;
    private String faultDesc;
    private String faultLat;
    private String faultLong;
    private int userID;
    private String faultStatus;
    private String faultDate;
    private String faultLocation;
    private String imageURL;
    private String techID;
    private int taskID;
    private String taskStart;
    private String taskEnd;
    private String taskStatus;
    private String taskSignOff;
    private String userFname;
    private String userLname;
    private String userPhone;

    public RequestsData(int faultID, String faultTitle, String faultDesc, String faultLat, String faultLong,
                      int userID, String faultStatus, String faultDate, String faultLocation, String imageURL,
                      String techID, int taskID, String taskStart, String taskEnd, String taskStatus,
                      String taskSignOff, String userFname, String userLname, String userPhone) {
        this.faultID = faultID;
        this.faultTitle = faultTitle;
        this.faultDesc = faultDesc;
        this.faultLat = faultLat;
        this.faultLong = faultLong;
        this.userID = userID;
        this.faultStatus = faultStatus;
        this.faultDate = faultDate;
        this.faultLocation = faultLocation;
        this.imageURL = imageURL;
        this.techID = techID;
        this.taskID = taskID;
        this.taskStart = taskStart;
        this.taskEnd = taskEnd;
        this.taskStatus = taskStatus;
        this.taskSignOff = taskSignOff;
        this.userFname = userFname;
        this.userLname = userLname;
        this.userPhone = userPhone;
    }

    public int getRequestID() {
        return faultID;
    }

    public String getRequestTitle() {
        return faultTitle;
    }

    public String getRequestDesc() {
        return faultDesc;
    }

    public String getRequestLat() {
        return faultLat;
    }

    public String getRequestLong() {
        return faultLong;
    }

    public int getUserID() {
        return userID;
    }

    public String getRequestStatus() {
        return faultStatus;
    }

    public String getRequestDate() {
        return faultDate;
    }

    public String getRequestLocation() {
        return faultLocation;
    }

    public Integer getTechID() {
        int id = 0;
        if(!techID.equals("null") || techID != "null"){
            id = Integer.parseInt(techID);
        }
        return id;
    }

    public int getTaskID() {
        return taskID;
    }

    public String getTaskStart() {
        return taskStart;
    }

    public String getTaskEnd() {
        return taskEnd;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public String getTaskSignOff() {
        return taskSignOff;
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

    public String getImageURL() {
        return imageURL;
    }
}
