package com.webandcrafts.healwire.ui.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.webandcrafts.healwire.ui.activate_account.ActivateAccountActivity;
import com.webandcrafts.healwire.AppController;
import com.webandcrafts.healwire.NewBaseActivity;
import com.webandcrafts.healwire.R;
import com.webandcrafts.healwire.ui.login.LoginActivity;
import com.webandcrafts.healwire.utils.Utils;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends NewBaseActivity {

    RelativeLayout relativeLayoutMain;
    EditText editTextEmail, editTextPhone, editTextPassword;
    Spinner spinnerUserType;
    Button buttonRegister;
    ProgressDialog progressDialog;
    String registrationStatus, responseMessage, userTypeDataStatus, validateEmailStatus;
    String email, password, confirmPassword, phone, userType;
    List listUserType = new ArrayList();
    List listUserTypeId = new ArrayList();
    ArrayAdapter arrayAdapterUserType;
    ImageView mBackIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        relativeLayoutMain =  findViewById(R.id.relativeLayoutMain);
        mBackIcon = findViewById(R.id.iv_back);
        editTextEmail =  findViewById(R.id.editTextEmail);
        editTextPassword =  findViewById(R.id.editTextPassword);
        editTextPhone =  findViewById(R.id.editTextPhone);
        buttonRegister =  findViewById(R.id.buttonRegister);
        spinnerUserType =  findViewById(R.id.spinnerUserType);
        progressDialog = new ProgressDialog(this);
        getUserType();
        arrayAdapterUserType = new ArrayAdapter(this, R.layout.custom_spinner, listUserType);
        arrayAdapterUserType.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                confirmPassword = password;
                phone = editTextPhone.getText().toString();

                validateUser(phone, email, password, confirmPassword, AppController.userType);

            }


        });


        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                //Validate the email field


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        spinnerUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userType = listUserTypeId.get(i).toString();
                AppController.userType = userType;
                Log.d(" SELECTED ITEM = ", "" + listUserTypeId.get(i).toString());

                // listUserTypeId.get(i).toString();


//                if(userType.equals("MEDICAL_PROFESSIONAL")){
//
//                    Log.d("MED", "" + i );
//
//                  AppController.INT_USER_TYPE = 2;
//
//                }
//                else if(userType.equals("CUSTOMER")){
//
//                    Log.d("CUS", "" + i );
//
//                    AppController.INT_USER_TYPE = 3;
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               RegisterActivity.this.finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });

    }

    private void validateUser(String phone, String email, String password, String confirmPassword, String userType) {
        if(email.isEmpty()){
            editTextEmail.setError("Field cannot be empty");
        }else if(!Utils.isValidEmailId(email)){
            editTextEmail.setError("Not a valid email");
        }else if(password.isEmpty()){
            editTextPassword.requestFocus();
            editTextPassword.setError("Field cannot be empty");
        }else if(phone.isEmpty()){
            editTextPhone.requestFocus();
            editTextPhone.setError("Field cannot be empty");
        }else if(phone.length() < 6 ||phone.length() > 13){
            editTextPhone.setError("Not a valid mobile number");
        }else {
         //   Log.d("SELECTED TYPE = ", AppController.userType);
            Utils.hideKeyboard(RegisterActivity.this);
            registerUser(phone, email, password, confirmPassword, AppController.userType);
            buttonRegister.setEnabled(false);
        }
    }


    public void inputValidData() {

        if (editTextEmail.length() < 1) {

            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();


        }


    }

    // To Register the user

    public void registerUser(final String phone, final String email, final String password, final String confirmpassword, final String userType) {


        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final StringRequest createUserRequest = new StringRequest(Request.Method.POST, AppController.CREATE_USER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        clearFields();


                        try {
                            // JSONArray mainResponse = new JSONArray(response);

                            JSONObject mainResponse = new JSONObject(response);
                            registrationStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");

                            //  Toast.makeText(getApplicationContext(),"" + registrationStatus,Toast.LENGTH_LONG).show();


                            if (registrationStatus.equals("SUCCESS")) {

                                // Toast.makeText(getApplicationContext(),responseMessage,Toast.LENGTH_LONG).show();

                                Snackbar.make(relativeLayoutMain, responseMessage, Snackbar.LENGTH_LONG).show();


                                AppController.userEmail = email;
                                AppController.userPassword = password;
                                AppController.userPhone = phone;
                                AppController.userType = userType;

                                SharedPreferences.Editor editor = getSharedPreferences(AppController.LOGIN_PREF_NAME, MODE_PRIVATE).edit();
                                editor.putString("phone", phone);
                                editor.apply();

                                buttonRegister.setEnabled(true);
                                Intent intentActivateAccount = new Intent(getApplicationContext(), ActivateAccountActivity.class);
                                startActivity(intentActivateAccount);
                                overridePendingTransition(R.anim.right,R.anim.left);
                                RegisterActivity.this.finish();

                            } else {//existing user

                                Snackbar.make(relativeLayoutMain, responseMessage, Snackbar.LENGTH_LONG).show();


                            }

                        } catch (Exception e) {

                        }


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                NetworkResponse networkResponse = error.networkResponse;


                if (networkResponse.statusCode == HttpStatus.SC_CONFLICT) {
                    responseMessage = "Account already exists";

                } else if (networkResponse.statusCode == HttpStatus.SC_BAD_REQUEST) {
                    responseMessage = "Empty Parameters or Invalid Email Format";

                } else if (networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                    responseMessage = "Some techincal failure.";

                }

                Snackbar.make(relativeLayoutMain, responseMessage, Snackbar.LENGTH_INDEFINITE)
                        .setAction("DONE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                RegisterActivity.this.finish();
                                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);

                            }
                        })
                        .show();


            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("phone", phone);
                params.put("email", email);
                params.put("password", password);
                params.put("confirm_password", confirmpassword);
                params.put("user_type", userType);

                return params;
            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(createUserRequest);

        createUserRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    } //Using Volley


    // To Validate the email field

    public void validateUserEmail() {


        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final StringRequest validateEmailRequest = new StringRequest(Request.Method.GET, "http://192.168.1.216/p/product/healwire/user/check-user-name?u_name=vineeth@webandcrafts.com",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();


                        try {
                            // JSONArray mainResponse = new JSONArray(response);

                            JSONObject mainResponse = new JSONObject(response);
                            validateEmailStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");

                            //  Toast.makeText(getApplicationContext(),"" + registrationStatus,Toast.LENGTH_LONG).show();


                            if (validateEmailStatus.equals("SUCCESS")) {

                                Toast.makeText(getApplicationContext(), responseMessage, Toast.LENGTH_LONG).show();

                                // Snackbar.make(relativeLayoutMain, responseMessage, Snackbar.LENGTH_LONG).show();


                                // registerGCM();

//                                new SnackBar(getActivity(),
//                                        "Successfuly registered").show();


                            } else {//existing user
                                //Toast.makeText(getActivity(),registrationStatus, Toast.LENGTH_SHORT).show();
//                                new SnackBar(getActivity(),
//                                        "Email already registered").show();

                                Toast.makeText(getApplicationContext(), responseMessage, Toast.LENGTH_LONG).show();

                                //Snackbar.make(relativeLayoutMain, responseMessage, Snackbar.LENGTH_LONG).show();


                            }

                            // messageHandler.sendEmptyMessage(1);
                        } catch (Exception e) {
                            // messageHandler.sendEmptyMessage(99);
                        }


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();


            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("u_name", email);

                return params;
            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(validateEmailRequest);

        validateEmailRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    } //Using Volley

    public void getUserType() {

        final StringRequest getUserTypeRequest = new StringRequest(Request.Method.GET, AppController.GET_USER_TYPE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject mainResponse = new JSONObject(response);
                            userTypeDataStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");
                            JSONArray jsonArrayData = mainResponse.getJSONArray("data");
                            if (userTypeDataStatus.equals("SUCCESS")) {

                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    JSONObject jsonObjectInner = jsonArrayData.getJSONObject(i);
                                    String userType = jsonObjectInner.getString("type");
                                    int userTypeId = jsonObjectInner.getInt("id");
                                    listUserType.add(userType);
                                    listUserTypeId.add(userTypeId);
                                    Log.d("USER TYPES ", "" + listUserType);
                                    Log.d("USER TYPES IDS ", "" + listUserTypeId);
                                }
                            } else {
                                Snackbar.make(relativeLayoutMain, responseMessage, Snackbar.LENGTH_LONG).show();
                            }

                            spinnerUserType.setAdapter(arrayAdapterUserType);

                        } catch (Exception e) {

                        }


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();


            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(getUserTypeRequest);

        getUserTypeRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    public void clearFields() {

        editTextEmail.getText().clear();
        editTextPhone.getText().clear();
        editTextPassword.getText().clear();


    }

    @Override
    public void onBackPressed() {
       RegisterActivity.this.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        super.onBackPressed();
    }
}
