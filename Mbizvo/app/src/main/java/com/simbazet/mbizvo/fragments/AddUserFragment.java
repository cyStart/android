package com.simbazet.mbizvo.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;
import com.simbazet.mbizvo.Constants;
import com.simbazet.mbizvo.Login;
import com.simbazet.mbizvo.MainActivity;
import com.simbazet.mbizvo.R;
import com.simbazet.mbizvo.RequestHandler;
import com.simbazet.mbizvo.Settings;
import com.simbazet.mbizvo.SharedPrefManager;
import com.simbazet.mbizvo.Signup;
import com.simbazet.mbizvo.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class AddUserFragment extends Fragment implements View.OnClickListener{

    private static Context context;
    private Context mCtx;
    private ProgressDialog progressDialog;
    private TextView login;
    private Button btnSignup;
    private Spinner selectType;
    private LinearLayout lll;
    private TextInputEditText fName, lName, confirm, phone, Password, question, answer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_signup, container, false);
        //return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();

        login = view.findViewById(R.id.signup);
        btnSignup = view.findViewById(R.id.loginButton);
        phone = view.findViewById(R.id.emailEditText);
        Password = view.findViewById(R.id.passwordEditText);
        fName = view.findViewById(R.id.fName);
        lName = view.findViewById(R.id.lName);
        confirm = view.findViewById(R.id.confirmEditText);
        question = view.findViewById(R.id.question);
        answer = view.findViewById(R.id.answer);
        selectType = view.findViewById(R.id.usertype);
        lll = view.findViewById(R.id.LLL);
        progressDialog = new ProgressDialog(context);

        btnSignup.setOnClickListener(this);
        login.setVisibility(View.GONE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
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
        final String type = selectType.getSelectedItem().toString();
        //final String userType = ETType.getSelectedItem().toString().trim();

        if (userFname.length() < 3 || userLname.length() < 3){
            Toast.makeText(getContext(), "Invalid name!!! minmum length is 3", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userPhone.length() < 9){
            Toast.makeText(getContext(), "Invalid phone number!!! minmum length is 9", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(password1)){
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 4){
            Toast.makeText(getContext(), "Password minmum length is 4", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userQ.length() < 4){
            Toast.makeText(getContext(), "Question minmum length is 4", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userA.length() < 2){
            Toast.makeText(getContext(), "Answer minmum length is 2", Toast.LENGTH_SHORT).show();
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Success");
                                builder.setIcon(R.drawable.icons_happy);
                                builder.setMessage("User has been added successfully");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        MainActivity.selectedAction.setText("Users");
                                        MainActivity.fragmentManager.beginTransaction().replace(R.id.containerView, new UsersFragment()).commit();
                                    }
                                });
                                builder.create();
                                builder.show();
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Error");
                        builder.setIcon(R.drawable.ico_crying);
                        builder.setMessage(error.getMessage() + "\n\nCurrent Host :\n\n" +
                                "http://" + Constants.Server_IP +"/");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(context, Settings.class));
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
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

    }

    @Override
    public void onClick(View view) {
        if (view == btnSignup){
            userLogin();
        }
    }
}

