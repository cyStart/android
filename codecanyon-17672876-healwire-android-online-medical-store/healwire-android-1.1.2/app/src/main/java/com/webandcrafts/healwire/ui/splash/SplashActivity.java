package com.webandcrafts.healwire.ui.splash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.webandcrafts.healwire.AppController;
import com.webandcrafts.healwire.R;
import com.webandcrafts.healwire.ui.activate_account.ActivateAccountActivity;
import com.webandcrafts.healwire.ui.home.HomeActivity;
import com.webandcrafts.healwire.ui.login.LoginActivity;
import com.webandcrafts.healwire.utils.SharedPreferenceHandler;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SplashActivity extends AppCompatActivity {

    static final int MY_REQUEST_CODE = 1;
    public  String mEamil;
    public String mPassword;
    ProgressDialog progressDialog;
    String loginStatus,responseMessage;
    String userAccountStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isUserLoggedIn();
            }
        }, 4000); // wait for 5 seconds
    }

    private void isUserLoggedIn() {

        SharedPreferences prefs = getSharedPreferences(AppController.LOGIN_PREF_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("email", null);
        if (restoredText != null) {
            Toast.makeText(this, "Welcome back", Toast.LENGTH_SHORT).show();
            mEamil= prefs.getString("email",null);//"No name defined" is the default value.
            mPassword = prefs.getString("password",null); //0 is the default value.
            loginUser(mEamil,mPassword);
        }else{   // new user
             Intent intentHome = new Intent(SplashActivity.this, HomeActivity.class);
             startActivity(intentHome);
             overridePendingTransition(R.anim.right, R.anim.left);
             finish();
            Toast.makeText(this, "Welcome to Healwire", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginUser(final String mEamil, final String mPassword) {

        StringRequest loginUserRequest = new StringRequest(Request.Method.POST,AppController.LOGIN_USER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       try{
                           JSONObject mainResponse = new JSONObject(response);
                           loginStatus = mainResponse.getString("status");
                           responseMessage = mainResponse.getString("msg");
                           JSONObject jsonObjectData = mainResponse.getJSONObject("data");
                           userAccountStatus = jsonObjectData.getString("status");

                           if(loginStatus.equals("SUCCESS")) {

                               if(userAccountStatus.equals("ACTIVE")) {

                                   Intent intentHome = new Intent(SplashActivity.this, HomeActivity.class);
                                                        startActivity(intentHome);
                                                        overridePendingTransition(R.anim.right,R.anim.left);
                                                        finish();
                               }else{
                                   Toast.makeText(SplashActivity.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
                               }
                           }

                       }catch(Exception e){

                       }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    if(networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED){
                        responseMessage = "Invalid login details";
                    }
                    else if(networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR){
                        responseMessage = "Some techincal failure.";
                    }
                }
            }

        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email",mEamil);
                params.put("password",mPassword);
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                //Response header
                Object setCookie;
                Map headers = response.headers;
                setCookie = headers.get("Set-Cookie");
                AppController.cookies = setCookie.toString();

                SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
                sharedPreferenceHandler.saveCookie(AppController.cookies,getApplicationContext());
                return super.parseNetworkResponse(response);
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(loginUserRequest);
        loginUserRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

}
