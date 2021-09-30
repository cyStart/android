package com.simbazet.mbizvo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.ConstraintSet.VISIBLE;

public class Login extends AppCompatActivity implements View.OnClickListener{

    public static String ipAdd;
    private ImageView bookIconImageView;
    private TextView bookITextView, signup, setip;
    private ProgressBar loadingProgressBar;
    private ProgressDialog progressDialog;
    private RelativeLayout rootView, afterAnimationView;
    private Button btnLogin;
    private TextInputEditText Username, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.loginButton);
        Username = findViewById(R.id.emailEditText);
        Password = findViewById(R.id.passwordEditText);
        signup = findViewById(R.id.signup);
        setip = findViewById(R.id.setip);
        progressDialog = new ProgressDialog(this);

        //*********** Check if ip address is set
        if (SharedPrefManager.getInstance(this).isIPset()){
            ipAdd = SharedPrefManager.getInstance(this).getIP();
        }
        else {
            Toast.makeText(this, "Server Host not Found", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("Server host not set, Contact Admin to give you the host address.\n" +
                    "This will be resolved in the production version.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                    startActivity(new Intent(Login.this, Settings.class));
                }
            });
            builder.create();
            builder.show();
        }

        //*********** Check if user if logged in
        if (SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            return;
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
        signup.setOnClickListener(this);
        setip.setOnClickListener(this);

        initViews();
        new CountDownTimer(5000, 4000) {

            @Override
            public void onTick(long millisUntilFinished) {
                startAnimation();
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void initViews() {
        bookIconImageView = findViewById(R.id.bookIconImageView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        rootView = findViewById(R.id.rootView);
        afterAnimationView = findViewById(R.id.afterAnimationView);
    }

    private void startAnimation() {
        ViewPropertyAnimator viewPropertyAnimator = bookIconImageView.animate();

        //viewPropertyAnimator.x(-100f);
        viewPropertyAnimator.y(200f);
        viewPropertyAnimator.setDuration(4000);

        viewPropertyAnimator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (SharedPrefManager.getInstance(Login.this).isLoggedIn()){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    return;
                }
                bookIconImageView.setVisibility(View.GONE);
                loadingProgressBar.setVisibility(View.GONE);
                afterAnimationView.setVisibility(View.VISIBLE);
                //startActivity(new Intent(SplashScreen.this, Login.class));
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void userLogin(){

        ipAdd = SharedPrefManager.getInstance(this).getIP();
        final String username = Username.getText().toString().trim();
        final String password = Password.getText().toString().trim();
        //final String userType = ETType.getSelectedItem().toString().trim();

        if (Username.length() < 1){
            Toast.makeText(this, "Enter Phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Password.length() < 1){
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setTitle("Authenticating");
        progressDialog.setMessage("please wait while verifying");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.login_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")){
                                Toast.makeText(Login.this, "You have been authenticated", Toast.LENGTH_SHORT).show();
                                String client = "Resident";
                                String technician = "Technician";
                                String admin = "Admin";

                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                SharedPrefManager.getInstance(getApplicationContext())
                                        .userLogin(
                                                jsonObject.getInt("userID"),
                                                jsonObject.getString("userPhone"),
                                                jsonObject.getString("userFname"),
                                                jsonObject.getString("userLname"),
                                                jsonObject.getString("userPhone"),
                                                jsonObject.getString("userType")
                                        );
                                finish();

                                /*if (jsonObject.getString("userType").equals(client)){
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    SharedPrefManager.getInstance(getApplicationContext())
                                            .userLogin(
                                                    jsonObject.getInt("userID"),
                                                    jsonObject.getString("userPhone"),
                                                    jsonObject.getString("userFname"),
                                                    jsonObject.getString("userLname"),
                                                    jsonObject.getString("userPhone"),
                                                    jsonObject.getString("userType")
                                            );
                                    finish();
                                }
                                else if (jsonObject.getString("userType").equals(client)){
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    SharedPrefManager.getInstance(getApplicationContext())
                                            .userLogin(
                                                    jsonObject.getInt("userID"),
                                                    jsonObject.getString("userPhone"),
                                                    jsonObject.getString("userFname"),
                                                    jsonObject.getString("userLname"),
                                                    jsonObject.getString("userPhone"),
                                                    jsonObject.getString("userType")
                                            );
                                    finish();
                                }
                                else if (jsonObject.getString("userType").equals(client)){
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    SharedPrefManager.getInstance(getApplicationContext())
                                            .userLogin(
                                                    jsonObject.getInt("userID"),
                                                    jsonObject.getString("userPhone"),
                                                    jsonObject.getString("userFname"),
                                                    jsonObject.getString("userLname"),
                                                    jsonObject.getString("userPhone"),
                                                    jsonObject.getString("userType")
                                            );
                                    finish();
                                }

                                else {
                                    Toast.makeText(Login.this, "Invalid user", Toast.LENGTH_SHORT).show();
                                }*/

                            }
                            else {
                                Toast.makeText(Login.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setTitle("Error");
                        builder.setMessage(error.getMessage() + "\n\nCurrent PATH :\n\n" +
                                Constants.Root_URL);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

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
                params.put("userPhone", username);
                params.put("userPassword", password);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onClick(View view) {
        if (view == setip){
            startActivity(new Intent(this, Settings.class));
        }
        if (view == signup){
            startActivity(new Intent(this, Signup.class));
        }
    }
}
