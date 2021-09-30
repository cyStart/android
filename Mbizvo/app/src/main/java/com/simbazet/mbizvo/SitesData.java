package com.simbazet.mbizvo;

public class SitesData {

    private String mapLat;
    private String mapLong;
    private String mapTitle;
    private String techID;
    private String userID;
    private String taskStatus;
    private String faultStatus;

    public SitesData(String mapLat, String mapLong, String mapTitle, String techID, String userID, String taskStatus, String faultStatus) {
        this.mapLat = mapLat;
        this.mapLong = mapLong;
        this.mapTitle = mapTitle;
        this.techID = techID;
        this.userID = userID;
        this.taskStatus = taskStatus;
        this.faultStatus = faultStatus;
    }

    public String getMapLat() {
        return mapLat;
    }

    public String getMapLong() {
        return mapLong;
    }

    public String getMapTitle() {
        return mapTitle;
    }

    public String getTechID() {
        return techID;
    }

    public String getUserID() {
        return userID;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public String getRequestStatus() {
        return faultStatus;
    }
}
