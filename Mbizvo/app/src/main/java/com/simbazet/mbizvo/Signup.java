package com.simbazet.mbizvo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.simbazet.mbizvo.fragments.RequestsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity implements View.OnClickListener{

    private ProgressDialog progressDialog;
    private TextView login;
    private Button btnSignup;
    private Spinner selectType;
    private LinearLayout lll;
    private TextInputEditText fName, lName, confirm, phone, Password, question, answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        login = findViewById(R.id.signup);
        btnSignup = findViewById(R.id.loginButton);
        phone = findViewById(R.id.emailEditText);
        Password = findViewById(R.id.passwordEditText);
        fName = findViewById(R.id.fName);
        lName = findViewById(R.id.lName);
        confirm = findViewById(R.id.confirmEditText);
        question = findViewById(R.id.question);
        answer = findViewById(R.id.answer);
        selectType = findViewById(R.id.usertype);
        lll = findViewById(R.id.LLL);
        progressDialog = new ProgressDialog(this);

        lll.setVisibility(View.GONE);
        btnSignup.setOnClickListener(this);
    }

    private void userLogin(){

        //ipAdd = SharedPrefManager.getInstance(this).getIP();
        final String userFname = fName.getText().toString().trim();
        final String userLname = lName.getText().toString().trim();
        final String userPhone = phone.getText().toString().trim();
        final String password = Password.getText().toString().trim();
        final String password1 = confirm.getText().toString().trim();
        final String userQ = question.getText().toString().trim();
        final String userA = answer.getText().toString().trim();
        final String type = "Resident";
        //final String userType = ETType.getSelectedItem().toString().trim();

        if (userFname.length() < 3 || userLname.length() < 3){
            Toast.makeText(this, "Invalid name!!! minmum length is 3", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userPhone.length() < 9){
            Toast.makeText(this, "Invalid phone number!!! minmum length is 9", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(password1)){
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 4){
            Toast.makeText(this, "Password minmum length is 4", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userQ.length() < 4){
            Toast.makeText(this, "Question minmum length is 4", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userA.length() < 2){
            Toast.makeText(this, "Answer minmum length is 2", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setTitle("Registering");
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.signup_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(!jsonObject.getBoolean("error")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                                builder.setTitle("Success");
                                builder.setIcon(R.drawable.icons_happy);
                                builder.setMessage(jsonObject.getString("message"));
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startActivity(new Intent(getApplicationContext(), Login.class));
                                    }
                                });
                                builder.create();
                                builder.show();
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                                builder.setTitle("Error");
                                builder.setIcon(R.drawable.ico_crying);
                                builder.setMessage(jsonObject.getString("message"));
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                builder.create();
                                builder.show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                        builder.setTitle("Error");
                        builder.setIcon(R.drawable.ico_crying);
                        builder.setMessage(error.getMessage() + "\n\nCurrent Host :\n\n" +
                                "http://" + Constants.Server_IP +"/");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(Signup.this, Settings.class));
                            }
                        });
                        builder.create();
                        builder.show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("userFname", userFname);
                params.put("userLname", userLname);
                params.put("userPhone", userPhone);
                params.put("userType", type);
                params.put("userPassword", password);
                params.put("userQ", userQ);
                params.put("userA", userA);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onClick(View view) {
        if (view == btnSignup){
            userLogin();
        }
        if (view == login){
            startActivity(new Intent(this, Login.class));
        }
    }
}
