package com.simbazet.mbizvo;

import android.content.Context;

public class Constants {
    // SharedPreferences to be implemented to set n store server IP
    //public static final  String Server_IP = "10.0.2.1";
    //public static final String ip = Login.ipAdd;
    //public static final  String Server_IP = "192.168.23.1";
    //public static final  String Server_IP = "192.168.55.10";
    public static String Server_IP = Login.ipAdd;
    //if connection fails, check the Server IP
    public static final  String Root_URL = "http://"+Server_IP+"/Android/mbizvo/api/";
    private static final String ROOT_URL = "http://"+Server_IP+"/Android/mbizvo/api/upload.php?apicall=";
    //User account creation and login
    public static final  String register_URL = Root_URL+"createMother.php";
    public static final  String refnum_URL = Root_URL+"refNum.php";
    //******************IN USE************************************
    public static final  String login_URL = Root_URL+"loginUser.php";
    public static final  String signup_URL = Root_URL+"addUser.php";
    public static final  String faults = Root_URL+"allRequests.php";
    public static final  String surburbs = Root_URL+"surburbs.php";
    public static final  String assignedRequests = Root_URL+"assignedRequests.php";
    public static final  String users = Root_URL+"allUsers.php";
    public static final  String assignTask = Root_URL+"assignTask.php";
    public static final  String signoffTask = Root_URL+"signoffTask.php";
    public static final  String insertRequest = Root_URL+"upload.php?apicall=uploadpic";
    //******************************************************************
    public static final  String getMyBabies = Root_URL+"getMyBabies.php";
    public static final  String BCdetails = Root_URL+"BCdetails.php";

    public static final  String MAIN_REPORT = Root_URL+"reportMain.php";
    public static final  String GET_APPLICATIONS = Root_URL+"getBC.php";
}
