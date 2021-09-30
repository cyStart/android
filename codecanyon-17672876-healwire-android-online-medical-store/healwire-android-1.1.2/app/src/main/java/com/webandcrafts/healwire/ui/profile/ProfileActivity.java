package com.webandcrafts.healwire.ui.profile;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.webandcrafts.healwire.AppController;
import com.webandcrafts.healwire.NewBaseActivity;
import com.webandcrafts.healwire.R;
import com.webandcrafts.healwire.utils.SharedPreferenceHandler;
import com.webandcrafts.healwire.ui.home.HomeActivity;
import com.webandcrafts.healwire.ui.user_address.AddressDetailsActivity;

import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ProfileActivity extends NewBaseActivity {

    RelativeLayout relativeLayoutMain;

    TextView textViewName, textViewEmail, textViewPhone, textViewAddress;
    Button buttonEditProfile, buttonLogout;

    String userDetailsStatus, responseMessage;

    String email;
    String firstName, lastName, address, phone, pincode;
    ImageView mBackImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        CookieHandler.setDefault(new CookieManager());


        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewPhone = (TextView) findViewById(R.id.textViewPhone);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        mBackImage = findViewById(R.id.iv_back);
        relativeLayoutMain = (RelativeLayout) findViewById(R.id.relativeLayoutMain);
        buttonEditProfile = (Button) findViewById(R.id.buttonEditProfile);
        buttonEditProfile.bringToFront();
        buttonLogout = (Button) findViewById(R.id.buttonLogout);

        SharedPreferenceHandler spHandler = new SharedPreferenceHandler();
        spHandler.getUserProfile(getApplicationContext());

        email = spHandler.getUserEmail(getApplicationContext());


        Log.d("RESPONSE-LOG", spHandler.getCookie(getApplicationContext()));


        getUserDetails(email);


        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentAddressDetails = new Intent(getApplicationContext(), AddressDetailsActivity.class);
                intentAddressDetails.putExtra("first_name", firstName);
                intentAddressDetails.putExtra("last_name", lastName);
                intentAddressDetails.putExtra("address", address);
                intentAddressDetails.putExtra("pincode", pincode);
                intentAddressDetails.putExtra("phone", phone);
                startActivity(intentAddressDetails);
                overridePendingTransition(R.anim.right,R.anim.left);
                ProfileActivity.this.finish();


            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.support.v7.app.AlertDialog alertDialog;

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ProfileActivity.this);
                builder.setMessage("Do you want to logout?");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
//        builder.setIcon(R.mipmap.app_icon_blue_and_yellow);
                builder.setCancelable(false);

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setResult(Activity.RESULT_OK);
                        AppController.userStatus = "logout";
                        SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
                        sharedPreferenceHandler.setUserStatus(getApplicationContext(), AppController.userStatus);

                        SharedPreferences.Editor editor = getSharedPreferences(AppController.LOGIN_PREF_NAME, MODE_PRIVATE).edit();
                        editor.clear();
                        editor.commit();

                        ProfileActivity.this.finish();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(Activity.RESULT_CANCELED);
                    }
                });

                alertDialog = builder.create();
                alertDialog.show();
            }
        });
        mBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileActivity.this.finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });


    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        Intent intentHome = new Intent(getApplicationContext(),HomeActivity.class);
//        startActivity(intentHome);
//        ProfileActivity.this.finish();
//
//    }

    // To get the user details

    public void getUserDetails(final String email) {


        final StringRequest getUserDetailsRequest = new StringRequest(Request.Method.POST, AppController.GET_USER_DETAILS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        //  Toast.makeText(getApplicationContext(),"" + response,Toast.LENGTH_LONG).show();


                        try {
                            // JSONArray mainResponse = new JSONArray(response);

                            JSONObject mainResponse = new JSONObject(response);

                            userDetailsStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");

                            JSONObject jsonObjectData = mainResponse.getJSONObject("data");


//                            Toast.makeText(getApplicationContext(),"" + userTypeDataStatus,Toast.LENGTH_LONG).show();


                            if (userDetailsStatus.equals("SUCCESS")) {

//                                Toast.makeText(getApplicationContext(),responseMessage,Toast.LENGTH_LONG).show();

                                firstName = jsonObjectData.getString("first_name");
                                lastName = jsonObjectData.getString("last_name");
                                address = jsonObjectData.getString("address");
                                phone = jsonObjectData.getString("phone");
                                pincode = jsonObjectData.getString("pincode");


                                // registerGCM();

//                                new SnackBar(getActivity(),
//                                        "Successfuly registered").show();


                            } else {//existing user
                                //Toast.makeText(getActivity(),registrationStatus, Toast.LENGTH_SHORT).show();
//                                new SnackBar(getActivity(),
//                                        "Email already registered").show();

                                //  Toast.makeText(getApplicationContext(),responseMessage,Toast.LENGTH_LONG).show();

                                Snackbar.make(relativeLayoutMain, responseMessage, Snackbar.LENGTH_LONG).show();


                            }

                            // messageHandler.sendEmptyMessage(1);

                            //Set the value from server

                            SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
                            sharedPreferenceHandler.setUserProfile(getApplicationContext(), firstName, lastName, address, email, phone, pincode);

                            // Load user details to textViews

                            sharedPreferenceHandler.getUserProfile(getApplicationContext());


                            AppController.userFullName = SharedPreferenceHandler.FIRST_NAME + " " + SharedPreferenceHandler.LAST_NAME;


                            textViewName.setText(AppController.userFullName);
                            String addressStr = SharedPreferenceHandler.ADDRESS.toString();
                            if(!addressStr.isEmpty())
                                textViewAddress.setText(addressStr);
                            else
                                textViewAddress.setText("Address not available");
                            if(!SharedPreferenceHandler.PHONE.isEmpty())
                                textViewPhone.setText(SharedPreferenceHandler.PHONE);
                            else
                                textViewPhone.setText("Phone not available");
                            textViewEmail.setText(SharedPreferenceHandler.EMAIL);


                        } catch (Exception e) {
                            // messageHandler.sendEmptyMessage(99);
                        }


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


//                params.put("phone",phone);
                params.put("email", email);
//                params.put("password",password);
//                params.put("confirm_password",confirmpassword);
//                params.put("user_type",userType);

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map headers = new HashMap();
                SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();

                if (!sharedPreferenceHandler.getCookie(getApplicationContext()).equals(""))
                    headers.put("Cookie", sharedPreferenceHandler.getCookie(getApplicationContext()));

                Log.d("HEADER_LOG", headers.toString());

                // return super.getHeaders();
                return headers;

            }
        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(getUserDetailsRequest);

        getUserDetailsRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    } //Using Volley

    @Override
    public void onBackPressed() {
        ProfileActivity.this.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        super.onBackPressed();
    }
}
