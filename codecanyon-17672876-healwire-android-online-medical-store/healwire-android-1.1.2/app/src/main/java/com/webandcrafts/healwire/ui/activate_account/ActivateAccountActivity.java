package com.webandcrafts.healwire.ui.activate_account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.webandcrafts.healwire.AppController;
import com.webandcrafts.healwire.NewBaseActivity;
import com.webandcrafts.healwire.R;
import com.webandcrafts.healwire.ui.user_address.AddressDetailsActivity;
import com.webandcrafts.healwire.utils.SharedPreferenceHandler;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ActivateAccountActivity extends NewBaseActivity {

    EditText editTextSecurityCode;
    Button buttonSubmit;
    String securitycode, email;
    ProgressDialog progressDialog;
    String activationStatus, responseMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_account);
        editTextSecurityCode = (EditText) findViewById(R.id.editTextSecurityCode);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        progressDialog = new ProgressDialog(this);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                securitycode = editTextSecurityCode.getText().toString();
                if (securitycode.isEmpty()) editTextSecurityCode.setError("Cannot be empty");
                else activateUser(AppController.userEmail, securitycode);
            }
        });

    }

    public void activateUser(final String email, final String securitycode) {
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest loginUserRequest = new StringRequest(Request.Method.POST, AppController.ACTIVATE_ACCOUNT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject mainResponse = new JSONObject(response);
                            activationStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");

                            if (activationStatus.equals("SUCCESS")) {
                                Toast.makeText(ActivateAccountActivity.this, responseMessage, Toast.LENGTH_SHORT).show();
                                SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
                                sharedPreferenceHandler.setUserEmail(getApplicationContext(), AppController.userEmail);
                                loginUser(email, AppController.userPassword);
                                Intent intentAddressDetials = new Intent(getApplicationContext(), AddressDetailsActivity.class);
                                startActivity(intentAddressDetials);
                                overridePendingTransition(R.anim.right, R.anim.left);
                                ActivateAccountActivity.this.finish();

                            } else {
                                Toast.makeText(ActivateAccountActivity.this, responseMessage, Toast.LENGTH_LONG).show();
                            }

                            // messageHandler.sendEmptyMessage(1);
                        } catch (Exception e) {
                            // messageHandler.sendEmptyMessage(99);
                        }


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(ActivateAccountActivity.this, "Failed. Please try again.", Toast.LENGTH_LONG).show();
                ActivateAccountActivity.this.finish();
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("security_code", securitycode);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(loginUserRequest);

        loginUserRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    } //Usinf Volley


    public void loginUser(final String email, final String password) {

        AppController.userEmail = email;


        StringRequest loginUserRequest = new StringRequest(Request.Method.POST, AppController.LOGIN_USER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        try {


                            JSONObject mainResponse = new JSONObject(response);
                            String loginStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");
                            JSONObject jsonObjectData = mainResponse.getJSONObject("data");
                            String userAccountStatus = jsonObjectData.getString("status");

                            JSONObject jsonObjectPrescriptionStatus = jsonObjectData.getJSONObject("pres_status");
                            JSONObject jsonObjectInvoiceStatus = jsonObjectData.getJSONObject("invoice_status");
                            JSONObject jsonObjectPaymentStatus = jsonObjectData.getJSONObject("payment_status");
                            JSONObject jsonObjectShippingStatus = jsonObjectData.getJSONObject("shipping_status");

                            Log.d("RES_PRES", jsonObjectPrescriptionStatus.toString());
                            Log.d("RES_PRES", jsonObjectInvoiceStatus.toString());
                            Log.d("RES_PRES", jsonObjectPaymentStatus.toString());
                            Log.d("RES_PRES", jsonObjectShippingStatus.toString());

                            SharedPreferenceHandler spHandlerStatus = new SharedPreferenceHandler();
                            spHandlerStatus.setPrescriptionStatus(getApplicationContext(), jsonObjectPrescriptionStatus);
                            spHandlerStatus.setInvoiceStatus(getApplicationContext(), jsonObjectInvoiceStatus);
                            spHandlerStatus.setPaymentStatus(getApplicationContext(), jsonObjectPaymentStatus);
                            spHandlerStatus.setShippingStatus(getApplicationContext(), jsonObjectShippingStatus);

                            Log.d("RES_PRES", "STATUSES SET");

                            if (loginStatus.equals("SUCCESS")) {

                                Toast.makeText(ActivateAccountActivity.this, responseMessage, Toast.LENGTH_SHORT).show();

                                if (userAccountStatus.equals("ACTIVE")) {

                                    // User account is active ..goto Home
                                    AppController.userStatus = "login";
                                    SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
                                    sharedPreferenceHandler.setUserStatus(getApplicationContext(), AppController.userStatus);
                                    sharedPreferenceHandler.setUserEmail(getApplicationContext(), AppController.userEmail);
                                    sharedPreferenceHandler.getUserProfile(getApplicationContext());
                                    AppController.userFullName = SharedPreferenceHandler.FIRST_NAME + " " + SharedPreferenceHandler.LAST_NAME;
                                    Toast.makeText(ActivateAccountActivity.this, "Account is active", Toast.LENGTH_SHORT).show();

                                    //   Intent intentUserProfile = new Intent(getApplicationContext(), HomeActivity.class);
                                    //   startActivity(intentUserProfile);
                                    //   overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                                    //    ActivateAccountActivity.this.finish();

                                } else {

                                }

                            } else if (loginStatus.equals("FAILURE")) {//existing user

                                Toast.makeText(ActivateAccountActivity.this, responseMessage, Toast.LENGTH_LONG).show();

                            } else {

                            }

                            // messageHandler.sendEmptyMessage(1);
                        } catch (Exception e) {
                            // messageHandler.sendEmptyMessage(99);
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {

                    if (networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                        // Unauthorised 401
                        responseMessage = "Invalid login details";
                    } else if (networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                        // Unauthorised 500
                        responseMessage = "Some techincal failure.";

                    }
                }
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
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
                sharedPreferenceHandler.saveCookie(AppController.cookies, getApplicationContext());
                return super.parseNetworkResponse(response);

            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(loginUserRequest);
        loginUserRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    } //Usinf Volley

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {

            progressDialog.dismiss();
        }
    }
}
