package com.webandcrafts.healwire.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.webandcrafts.healwire.ui.activate_account.ActivateAccountActivity;
import com.webandcrafts.healwire.AppController;
import com.webandcrafts.healwire.ui.fogot_passwod.ForgotActivity;
import com.webandcrafts.healwire.ui.home.HomeActivity;
import com.webandcrafts.healwire.NewBaseActivity;
import com.webandcrafts.healwire.R;
import com.webandcrafts.healwire.ui.register.RegisterActivity;
import com.webandcrafts.healwire.utils.SharedPreferenceHandler;
import com.webandcrafts.healwire.utils.Utils;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends NewBaseActivity {

    RelativeLayout relativeLayoutMain;
    EditText editTextEmail,editTextPassword;
    Button buttonLogin;
    TextView textViewClickHere,textViewForgotPassword;
    ProgressDialog progressDialog;
    String loginStatus,responseMessage;
    String email,password;
    String userAccountStatus;
    Snackbar snackbar;
    ImageView mBackIcon;

    String firstName,lastName,address,pincode,phone,userDetailsStatus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CookieHandler.setDefault(new CookieManager());
        initUi();
    }

    private void initUi() {

        relativeLayoutMain =  findViewById(R.id.relativeLayoutMain);
        editTextEmail =  findViewById(R.id.editTextEmail);
        editTextPassword =  findViewById(R.id.editTextPassword);
        buttonLogin =  findViewById(R.id.buttonLogin);
        textViewClickHere=  findViewById(R.id.textViewClickHere);
        textViewForgotPassword =  findViewById(R.id.textViewForgotPassword);
        progressDialog = new ProgressDialog(this);
        mBackIcon = findViewById(R.id.iv_back);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                validateUserDetails(email,password);
            }
        });

        textViewClickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegisterActivity =  new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intentRegisterActivity);
                overridePendingTransition(R.anim.right,R.anim.left);
                LoginActivity.this.finish();
            }
        });
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentForgotActivity =  new Intent(getApplicationContext(),ForgotActivity.class);
                startActivity(intentForgotActivity);
                overridePendingTransition(R.anim.right,R.anim.left);
                LoginActivity.this.finish();
            }
        });
        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.enter_from_left,R.anim.exit_to_right);

            }
        });
    }

    private void validateUserDetails(String email, String password) {

        if(email.isEmpty())
        {
            editTextEmail.setError("Field cannot be empty");
        }else if(!Utils.isValidEmailId(email)){
            editTextEmail.setError("Not a Valid email");
        }else if(password.isEmpty()){
            editTextPassword.requestFocus();
            editTextPassword.setError("Field cannot be empty");
        }else {
            AppController.userEmail = email;
            loginUser(email, password);
            Utils.hideKeyboard(LoginActivity.this);
        }

    }

    public void clearFields(){
        editTextEmail.getText().clear();
        editTextPassword.getText().clear();
    }

    public void loginUser(final String email,final String password){

        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest loginUserRequest = new StringRequest(Request.Method.POST,AppController.LOGIN_USER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try{
                            JSONObject mainResponse = new JSONObject(response);
                            loginStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");
                            JSONObject jsonObjectData = mainResponse.getJSONObject("data");
                            userAccountStatus = jsonObjectData.getString("status");

                            JSONObject jsonObjectPrescriptionStatus = jsonObjectData.getJSONObject("pres_status");
                            JSONObject jsonObjectInvoiceStatus = jsonObjectData.getJSONObject("invoice_status");
                            JSONObject jsonObjectPaymentStatus = jsonObjectData.getJSONObject("payment_status");
                            JSONObject jsonObjectShippingStatus = jsonObjectData.getJSONObject("shipping_status");

                            Log.d("RES_PRES",jsonObjectPrescriptionStatus.toString());
                            Log.d("RES_PRES",jsonObjectInvoiceStatus.toString());
                            Log.d("RES_PRES",jsonObjectPaymentStatus.toString());
                            Log.d("RES_PRES",jsonObjectShippingStatus.toString());


                            SharedPreferenceHandler spHandlerStatus = new SharedPreferenceHandler();
                            spHandlerStatus.setPrescriptionStatus(getApplicationContext(),jsonObjectPrescriptionStatus);
                            spHandlerStatus.setInvoiceStatus(getApplicationContext(),jsonObjectInvoiceStatus);
                            spHandlerStatus.setPaymentStatus(getApplicationContext(),jsonObjectPaymentStatus);
                            spHandlerStatus.setShippingStatus(getApplicationContext(),jsonObjectShippingStatus);

                            Log.d("RES_PRES","STATUSES SET");

                            if(loginStatus.equals("SUCCESS")){

                                Toast.makeText(getApplicationContext(),responseMessage,Toast.LENGTH_SHORT).show();
                                 Snackbar.make(relativeLayoutMain,responseMessage,Snackbar.LENGTH_LONG).show();

                                if(userAccountStatus.equals("PENDING")){
                                    // User account is not active ..pending for approval..Goto Activate account screen or dialog
                                    Toast.makeText(getApplicationContext(),"Activation is pending",Toast.LENGTH_SHORT).show();
                                    Intent intentActivateAccount = new Intent(getApplicationContext(),ActivateAccountActivity.class);
                                    startActivity(intentActivateAccount);
                                    LoginActivity.this.finish();

                                }
                                else if(userAccountStatus.equals("ACTIVE")){

                                    AppController.userStatus = "login";
                                    SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
                                    sharedPreferenceHandler.setUserStatus(getApplicationContext(),AppController.userStatus);
                                    sharedPreferenceHandler.setUserEmail(getApplicationContext(),AppController.userEmail);
                                    sharedPreferenceHandler.getUserProfile(getApplicationContext());
                                    AppController.userFullName = SharedPreferenceHandler.FIRST_NAME + " " + SharedPreferenceHandler.LAST_NAME;
                                    getUserDetails(email);
                                    SharedPreferences.Editor editor = getSharedPreferences(AppController.LOGIN_PREF_NAME, MODE_PRIVATE).edit();
                                    editor.putString("email", email);
                                    editor.putString("password",password );
                                    editor.apply();
                                    Toast.makeText(getApplicationContext(),"Account is active",Toast.LENGTH_SHORT).show();
                                     LoginActivity.this.finish();

                                }
                                else{

                                }

                            }
                            else if (loginStatus.equals("FAILURE")){//existing user
                                Toast.makeText(getApplicationContext(),responseMessage,Toast.LENGTH_LONG).show();
                            }
                            else{

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
                    Snackbar.make(relativeLayoutMain,responseMessage, Snackbar.LENGTH_INDEFINITE)
                            .setAction("DONE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    clearFields();
                                    editTextEmail.setFocusableInTouchMode(true);
                                    editTextEmail.requestFocus();
                                }
                            })
                            .show();
                }
            }

        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email",email);
                params.put("password",password);
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

    public void getUserDetails(final String email){

        final StringRequest getUserDetailsRequest = new StringRequest(Request.Method.POST,AppController.GET_USER_DETAILS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject mainResponse = new JSONObject(response);
                            userDetailsStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");
                            JSONObject jsonObjectData = mainResponse.getJSONObject("data");

                            if(userDetailsStatus.equals("SUCCESS")){
                                firstName = jsonObjectData.getString("first_name");
                                lastName = jsonObjectData.getString("last_name");
                                address = jsonObjectData.getString("address");
                                phone = jsonObjectData.getString("phone");
                                pincode = jsonObjectData.getString("pincode");
                            }
                            else{
                                Snackbar.make(relativeLayoutMain, responseMessage, Snackbar.LENGTH_LONG).show();
                            }
                            SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
                            sharedPreferenceHandler.setUserProfile(getApplicationContext(),firstName,lastName,address,email,phone,pincode);
                            sharedPreferenceHandler.getUserProfile(getApplicationContext());
                            AppController.userFullName = SharedPreferenceHandler.FIRST_NAME + " " + SharedPreferenceHandler.LAST_NAME;

                        }catch(Exception e){

                        }


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email",email);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map headers = new HashMap();
                SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();

                if(!sharedPreferenceHandler.getCookie(getApplicationContext()).equals(""))
                    headers.put("Cookie", sharedPreferenceHandler.getCookie(getApplicationContext()));

                Log.d("HEADER_LOG",headers.toString());

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
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_to_right);
        super.onBackPressed();
    }
}
