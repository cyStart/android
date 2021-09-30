package com.webandcrafts.healwire;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.webandcrafts.healwire.utils.LruBitmapCache;

import java.util.ArrayList;


/**
 * Created by vineeth on 7/21/2016.
 */
public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    public static String WEB_SERVICE_URL = "http://store.webandcrafts.com/demo/healwire/";  //Change the web server URL of your product
    public static String BASE_URL = WEB_SERVICE_URL + "user/";
    public static String CREATE_USER_URL = BASE_URL + "create-user";
    public static String GET_USER_TYPE_URL = BASE_URL + "obtain-user-type";
    public static String LOGIN_USER_URL = BASE_URL + "user-login";
    public static String ACTIVATE_ACCOUNT_URL = BASE_URL + "activate-account";
    public static String RESET_PASSWORD_URL = BASE_URL + "reset-password";
    public static String VALIDATE_EMAIL_URL = BASE_URL + "check-user-name";
    public static String UPDATE_PROFILE_URL = BASE_URL + "update-details-user";
    public static String GET_USER_DETAILS_URL = BASE_URL + "user-details";
    public static String MEDICINE_URL = WEB_SERVICE_URL + "medicine/";
    public static String LOAD_MEDICINE_URL = MEDICINE_URL + "load-medicine-web";
    public static String LOAD_SUBSTITUTE_MEDICINE_URL = MEDICINE_URL + "load-sub-medicine";
    public static String STORE_PRESCRIPTION_URL = MEDICINE_URL + "store-prescription";
    public static String GET_PRESCRIPTION_THUMB_URL = MEDICINE_URL + "get-prescription-thumb";
    public static String BASE_PAYMENT_URL = MEDICINE_URL + "make-payment/";
    public static String PAYMENT_SUCCESS_URL = WEB_SERVICE_URL + "payment/success";
    public static String PAYMENT_FAILED_URL = WEB_SERVICE_URL + "payment/failure";
    public static String PAYMENT_STATUS;
    public static String LOGIN_PREF_NAME = "LOGIN";
    public static boolean ITEM_IN_CART = false;
    public static String MY_CART_TO_CHOOSE_FILE_PATH = "false";
    public static String updated_MedicineName; //  This is the updated Medicine name when changing the Quantity
    public static String updated_Quantity; //  This is the updated quantity when changing the Quantity
    public static int updated_ItemPosition; //  This is the updated item position when changing the Quantity
    public static String userStatus = "logout"; // or logout
    public static String cookies;
    public static ArrayList<Integer> invoiceIdList = new ArrayList<Integer>();
    public static int invoice_Id_position;
    public static String userEmail;
    public static String userPassword;
    public static String userPhone;
    public static String userType;
    public static int INT_USER_TYPE;
    public static String userFullName;
    public static int INVOICE_ID;
    public static int DEVICE_CAMERA;
    public static int MIN_IMAGE_WIDTH = 1440;
    public static int cartLength = 0; //  Denotes the length of the cart.
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;


    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }


    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


}
