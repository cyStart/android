package com.webandcrafts.healwire.ui.fogot_passwod;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.webandcrafts.healwire.AppController;
import com.webandcrafts.healwire.NewBaseActivity;
import com.webandcrafts.healwire.R;
import com.webandcrafts.healwire.ui.reset_password.ResetPasswordActivity;
import com.webandcrafts.healwire.ui.login.LoginActivity;
import com.webandcrafts.healwire.utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ForgotActivity extends NewBaseActivity {

    EditText editTextEmail;
    Button buttonSubmit;
    String email;
    String forgotStatus,responseMessage;
    ProgressDialog progressDialog;
    ImageView mBackIcon;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        initUi();
    }

    private void initUi() {

        editTextEmail =  findViewById(R.id.editTextEmail);
        buttonSubmit =  findViewById(R.id.buttonSubmit);
        progressDialog = new ProgressDialog(this);
        mBackIcon = findViewById(R.id.iv_back);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = editTextEmail.getText().toString();
                validateEmail(email);
            }
        });
        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgotActivity.this.finish();
                overridePendingTransition(R.anim.enter_from_left,R.anim.exit_to_right);
            }
        });
        editTextEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextEmail.setError(null);
            }
        });


    }

    private void validateEmail(String email) {

        if(email.isEmpty()){
            editTextEmail.setError("Field cannot be empty");
        }
        else if(!Utils.isValidEmailId(email)){
            editTextEmail.setError("Not a valid email");
        }else
        {   AppController.userEmail = email;
            forgotPassword(email);
        Utils.hideKeyboard(ForgotActivity.this);}
    }


    // To login the user

    public void forgotPassword(final String email){


        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest forgotPasswordRequest = new StringRequest(Request.Method.POST,AppController.RESET_PASSWORD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        try{
                            JSONObject mainResponse = new JSONObject(response);
                            forgotStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");
                            if(forgotStatus.equals("SUCCESS")){
                                  Intent intentResetPassword = new Intent(getApplicationContext(),ResetPasswordActivity.class);
                                  startActivity(intentResetPassword);
                                overridePendingTransition(R.anim.right,R.anim.left);
                                  ForgotActivity.this.finish();
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
                Toast.makeText(ForgotActivity.this,  error.toString(), Toast.LENGTH_SHORT).show();

            }

        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email",email);
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(forgotPasswordRequest);
        forgotPasswordRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    @Override
    public void onBackPressed() {
        ForgotActivity.this.finish();
        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_to_right);
        super.onBackPressed();
    }
}
