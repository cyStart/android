package com.webandcrafts.healwire.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.webandcrafts.healwire.AppController;
import com.webandcrafts.healwire.models.MyCartItems;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by vineeth on 8/5/2016.
 */
public class SharedPreferenceHandler {


    public static final String PREFS_NAME = PreferenceManager.MY_PREFS_NAME;
    public static final String CARTITEMS = "cartitems";
    public static int CART_LENGTH = 0;
    public static ArrayList cartList;
    public static ArrayList<String> arrayListMedicineName = new ArrayList<String>();

    public static String FIRST_NAME;
    public static String LAST_NAME;
    public static String ADDRESS;
    public static String EMAIL;
    public static String PHONE;
    public static String PINCODE;

    public static int UNVERIFIED;
    public static int VERIFIED;
    public static int REJECTED;
    public static int PENDING;
    public static int PAID;
    public static int UNPAID;
    public static int CANCELLED;
    public static int SUCCESS;
    public static int FAILURE;
    public static int NOT_SHIPPED;
    public static int SHIPPED;
    public static int RETURNED;
    public static int RECEIVED;




    public SharedPreferenceHandler() {
        super();
    }
    public void storeCartItems(Context context, List cartitems) {

        // used for store arrayList in json format
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonCartItems = gson.toJson(cartitems);
        editor.putString(CARTITEMS, jsonCartItems);
        editor.commit();
    }
    public ArrayList loadCartItems(Context context) {

        // used for retrieving arraylist from json formatted string
        SharedPreferences settings;
        List cartItemsList;
        settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        if (settings.contains(CARTITEMS)) {
            String jsonFavorites = settings.getString(CARTITEMS, null);
            Gson gson = new Gson();
            MyCartItems[] myCartItems = gson.fromJson(jsonFavorites,MyCartItems[].class);
            cartItemsList = Arrays.asList(myCartItems);
            cartItemsList = new ArrayList(cartItemsList);

        } else
            return null;
        SharedPreferenceHandler.CART_LENGTH = cartItemsList.size();

        return (ArrayList) cartItemsList;
    }
    public void addCartItems(Context context, MyCartItems myCartItems) {
        List cartItemsList = loadCartItems(context);
        if (cartItemsList == null)
            cartItemsList = new ArrayList();
        cartItemsList.add(myCartItems);
        storeCartItems(context, cartItemsList);
    }

    public void addCartItemsAtPosition(Context context, MyCartItems myCartItems,int position) {
        List cartItemsList = loadCartItems(context);
        if (cartItemsList == null)
            cartItemsList = new ArrayList();
        //cartItemsList.add(myCartItems);
        cartItemsList.set(position,myCartItems);
        storeCartItems(context, cartItemsList);
    }

    public void checkItemExists(Context context, MyCartItems myCartItems){

        boolean result=false;
        List cartItemsList = loadCartItems(context);
        if (cartItemsList == null) {
            cartItemsList = new ArrayList();
            //cartItemsList.add(myCartItems);
        }
        else{

            cartList = new ArrayList();
            cartList = loadCartItems(context);



        }




    }


    public void getCartArrayList(Context context){

        boolean result=false;
        List cartItemsList = loadCartItems(context);
        if (cartItemsList == null) {
            cartItemsList = new ArrayList();
            //cartItemsList.add(myCartItems);
        }
        else{

            cartList = new ArrayList();
            cartList = loadCartItems(context);

            CART_LENGTH = cartList.size();


        }




    }


    public  void setUserStatus(Context context,String status){

        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE).edit();
        editor.putString("userStatus", AppController.userStatus);
        editor.commit();


    }

    public String getUserStatus(Context context){

        String status;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        status = prefs.getString("userStatus", AppController.userStatus);



        return status;


    }


    public void removeCartItems(Context context, MyCartItems myCartItems,int position) {
        ArrayList cartItemsList = loadCartItems(context);
        if (cartItemsList != null) {
            cartItemsList.remove(position);
            storeCartItems(context, cartItemsList);
        }
        else{

            // Cart is empty

        }
    }

    public void removeAllCart(Context context){

        ArrayList cartItemsList = loadCartItems(context);
        if (cartItemsList != null) {
            cartItemsList.removeAll(cartItemsList);
            storeCartItems(context, cartItemsList);
        }
        else{

            // Cart is empty

        }

    }

    public int getCartSize(Context context){

        List cartItemsList;

        cartItemsList = loadCartItems(context);


        return SharedPreferenceHandler.CART_LENGTH;

    }


    public void setUserProfile(Context context,String firstName,String lastName,String address,String email,String phone,String pincode){

        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE).edit();
        editor.putString("first_name",firstName);
        editor.putString("last_name",lastName);
        editor.putString("address",address);
        editor.putString("email",email);
        editor.putString("phone",phone);
        editor.putString("pincode",pincode);
        editor.commit();


    }

    public void getUserProfile(Context context){


        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);

        SharedPreferenceHandler.FIRST_NAME = prefs.getString("first_name", null);
        SharedPreferenceHandler.LAST_NAME = prefs.getString("last_name", null);
        SharedPreferenceHandler.ADDRESS = prefs.getString("address", null);
        SharedPreferenceHandler.EMAIL = prefs.getString("email", null);
        SharedPreferenceHandler.PHONE = prefs.getString("phone", null);
        SharedPreferenceHandler.PINCODE = prefs.getString("pincode", null);



    }


    public  void setUserEmail(Context context,String email){

        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE).edit();
        editor.putString("email", email);
        editor.commit();


    }

    public String getUserEmail(Context context){


        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        SharedPreferenceHandler.EMAIL = prefs.getString("email", null);



        return SharedPreferenceHandler.EMAIL;


    }


    // User preference for storing different staus fields and their values uses for this app

    public void getPrescriptionStatus(Context context){


        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        SharedPreferenceHandler.UNVERIFIED = prefs.getInt("UNVERIFIED", 0);
        SharedPreferenceHandler.VERIFIED = prefs.getInt("VERIFIED", 0);
        SharedPreferenceHandler.REJECTED = prefs.getInt("REJECTED", 0);


    }

    public void setPrescriptionStatus(Context context, JSONObject jsonObjectPrescriptionStatus){

        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE).edit();
        try {
            editor.putInt("UNVERIFIED",jsonObjectPrescriptionStatus.getInt("UNVERIFIED"));
            editor.putInt("VERIFIED",jsonObjectPrescriptionStatus.getInt("VERIFIED"));
            editor.putInt("REJECTED",jsonObjectPrescriptionStatus.getInt("REJECTED"));
            editor.commit();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void getInvoiceStatus(Context context){


        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        SharedPreferenceHandler.PENDING = prefs.getInt("PENDING", 0);
        SharedPreferenceHandler.PAID = prefs.getInt("PAID", 0);
        SharedPreferenceHandler.UNPAID = prefs.getInt("UNPAID", 0);
        SharedPreferenceHandler.CANCELLED = prefs.getInt("CANCELLED", 0);

    }

    public void setInvoiceStatus(Context context,JSONObject jsonObjectInvoiceStatus){

        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE).edit();
        try {
            editor.putInt("PENDING",jsonObjectInvoiceStatus.getInt("PENDING"));
            editor.putInt("PAID",jsonObjectInvoiceStatus.getInt("PAID"));
            editor.putInt("UNPAID",jsonObjectInvoiceStatus.getInt("UNPAID"));
            editor.putInt("CANCELLED",jsonObjectInvoiceStatus.getInt("CANCELLED"));
            editor.commit();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void getPaymentStatus(Context context){

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        SharedPreferenceHandler.PENDING = prefs.getInt("PENDING", 0);
        SharedPreferenceHandler.SUCCESS = prefs.getInt("SUCCESS", 0);
        SharedPreferenceHandler.FAILURE = prefs.getInt("FAILURE", 0);




    }

    public void setPaymentStatus(Context context, JSONObject jsonObjectPaymentStatus){



        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE).edit();
        try {
            editor.putInt("PENDING",jsonObjectPaymentStatus.getInt("PENDING"));
            editor.putInt("SUCCESS",jsonObjectPaymentStatus.getInt("SUCCESS"));
            editor.putInt("FAILURE",jsonObjectPaymentStatus.getInt("FAILURE"));
            editor.commit();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void getShippingStatus(Context context){

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        SharedPreferenceHandler.NOT_SHIPPED = prefs.getInt("NOT SHIPPED", 0);
        SharedPreferenceHandler.SHIPPED = prefs.getInt("SHIPPED", 0);
        SharedPreferenceHandler.RETURNED = prefs.getInt("RETURNED", 0);
        SharedPreferenceHandler.RECEIVED = prefs.getInt("RECEIVED", 0);


    }

    public void setShippingStatus(Context context, JSONObject jsonObjectShippingStatus){

        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE).edit();
        try {
            editor.putInt("NOT SHIPPED",jsonObjectShippingStatus.getInt("NOT SHIPPED"));
            editor.putInt("SHIPPED",jsonObjectShippingStatus.getInt("SHIPPED"));
            editor.putInt("RETURNED",jsonObjectShippingStatus.getInt("RETURNED"));
            editor.putInt("RECEIVED",jsonObjectShippingStatus.getInt("RECEIVED"));
            editor.commit();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



    public void saveCookie(String cookie,Context context){

        if (cookie == null) {
            //the server did not return a cookie so we wont have anything to save
            return;
        }
        // Save in the preferences
        SharedPreferences prefs =  context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        if (null == prefs) {
            return;
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("cookie", cookie);
        editor.commit();


    }


    public String getCookie(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        String cookie = prefs.getString("cookie", "");
//        if (cookie.contains("expires")) {
///** you might need to make sure that your cookie returns expires when its expired. I also noted that cokephp returns deleted */
//            removeCookie(context);
//            return "";
//        }
        return cookie;
    }

    public void removeCookie(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("cookie");
        editor.commit();
    }



}
