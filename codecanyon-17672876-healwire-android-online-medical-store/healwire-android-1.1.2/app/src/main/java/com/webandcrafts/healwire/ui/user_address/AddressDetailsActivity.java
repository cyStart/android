package com.webandcrafts.healwire.ui.user_address;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.webandcrafts.healwire.utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AddressDetailsActivity extends NewBaseActivity {

    EditText editTextFirstName, editTextLastName, editTextAddress, editTextPincode;
    Button buttonUpdate;
    String firstName, lastName, address, pincode, phone;
    RelativeLayout relativeLayoutMain;
    String updateProfileStatus, responseMessage;
    ProgressDialog progressDialog;
    ImageView mBackImage;
    String mPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_details);

        SharedPreferences prefs = getSharedPreferences(AppController.LOGIN_PREF_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("phone", null);
        if (restoredText != null) {
            mPhone = prefs.getString("phone", null);//"No name defined" is the default value.
            phone = mPhone;
        } else {

            mPhone = "9846639364";
        }

        progressDialog = new ProgressDialog(this);
        relativeLayoutMain = (RelativeLayout) findViewById(R.id.relativeLayoutMain);

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextPincode = findViewById(R.id.editTextPincode);
        mBackImage = findViewById(R.id.iv_back);

        buttonUpdate = findViewById(R.id.buttonUpdate);
        firstName = getIntent().getStringExtra("first_name");
        lastName = getIntent().getStringExtra("last_name");
        address = getIntent().getStringExtra("address");
        pincode = getIntent().getStringExtra("pincode");
        phone = mPhone;

        editTextFirstName.setText(firstName);
        editTextLastName.setText(lastName);
        editTextAddress.setText(address);
        editTextPincode.setText(pincode);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firstName = editTextFirstName.getText().toString();
                lastName = editTextLastName.getText().toString();
                address = editTextAddress.getText().toString();
                pincode = editTextPincode.getText().toString();
                phone = mPhone;
                Utils.hideKeyboard(AddressDetailsActivity.this);
                if (firstName.isEmpty()) editTextFirstName.setError("Cannot be empty");
                else updateUserProfile(firstName, lastName, address, pincode, phone);
            }
        });
        mBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressDetailsActivity.this.finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });

    }

    // To update the user profile

    public void updateUserProfile(final String firstName, final String lastName, final String address, final String pincode, final String phone) {


        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final StringRequest updateUserProfileRequest = new StringRequest(Request.Method.POST, AppController.UPDATE_PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        try {
                            // JSONArray mainResponse = new JSONArray(response);

                            JSONObject mainResponse = new JSONObject(response);
                            updateProfileStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");

                            //  Toast.makeText(getApplicationContext(),"" + registrationStatus,Toast.LENGTH_LONG).show();


                            if (updateProfileStatus.equals("SUCCESS")) {

                                // Toast.makeText(getApplicationContext(),responseMessage,Toast.LENGTH_LONG).show();

                                Toast.makeText(AddressDetailsActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();


                                SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
                                sharedPreferenceHandler.setUserProfile(getApplicationContext(), firstName, lastName, address, AppController.userEmail, phone, pincode);

                                AddressDetailsActivity.this.finish();
                                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);


                            } else {
                                Utils.hideKeyboard(AddressDetailsActivity.this);
                                Snackbar.make(relativeLayoutMain, responseMessage, Snackbar.LENGTH_LONG).show();
                            }
                            // messageHandler.sendEmptyMessage(1);
                        } catch (Exception e) {
                            Log.e("pincode", "profile");
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

                Log.d("UPDATE API : ", "CALLED ");


                Log.d("first_name", firstName);
                Log.d("last_name", lastName);
                Log.d("address", address);
                Log.d("pincode", pincode);
                Log.d("phone", phone);
                Log.d("user_type", String.valueOf(AppController.userType));
                Log.d("email", AppController.userEmail);


                params.put("first_name", firstName);
                params.put("last_name", lastName);
                params.put("address", address);
                params.put("pincode", pincode);
                params.put("phone", phone);
                params.put("user_type", String.valueOf(AppController.userType));
                params.put("email", AppController.userEmail);

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
        AppController.getInstance().addToRequestQueue(updateUserProfileRequest);

        updateUserProfileRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    } //Using Volley

    public void clearFields() {

        editTextFirstName.getText().clear();
        editTextLastName.getText().clear();
        editTextAddress.getText().clear();
        editTextPincode.getText().clear();


    }


    @Override
    public void onBackPressed() {
        AddressDetailsActivity.this.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        super.onBackPressed();
    }
}
