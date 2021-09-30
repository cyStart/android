package com.simbazet.mbizvo.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.simbazet.mbizvo.Constants;
import com.simbazet.mbizvo.MainActivity;
import com.simbazet.mbizvo.R;
import com.simbazet.mbizvo.RequestHandler;
import com.simbazet.mbizvo.Settings;
import com.simbazet.mbizvo.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AssignTaskFragment extends Fragment implements View.OnClickListener{

    private static Context context;
    private Context mCtx;
    private ProgressDialog progressDialog;
    private TextView login;
    private Button btnSave, btnPick;
    private Spinner selectType;
    private LinearLayout lll;
    public static EditText techName, faultTitle;
    final ItemsFragment it = new ItemsFragment();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assign_task, container, false);
        //return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();


        progressDialog = new ProgressDialog(context);
        btnSave = view.findViewById(R.id.btnSave);
        btnPick = view.findViewById(R.id.btnPick);
        techName = view.findViewById(R.id.techName);
        faultTitle = view.findViewById(R.id.faultTitle);

        faultTitle.setText(MainActivity.faultTitle);
        btnSave.setOnClickListener(this);
        btnPick.setOnClickListener(this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
    }

    private void assignTask(){

        //ipAdd = SharedPrefManager.getInstance(this).getIP();
        final String techID = String.valueOf(MainActivity.techID);
        final String taskID = String.valueOf(MainActivity.taskID);

        progressDialog.setTitle("Assigning");
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.assignTask,
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
                                builder.setMessage(jsonObject.getString("message"));
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        MainActivity.selectedAction.setText("Requests");
                                        MainActivity.fragmentManager.beginTransaction().replace(R.id.containerView, new RequestsFragment()).commit();
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
                params.put("techID", techID);
                params.put("taskID", taskID);
                params.put("userID", String.valueOf(SharedPrefManager.getInstance(context).getUserID()));
                params.put("userType", SharedPrefManager.getInstance(context).getUserType());
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

    }

    @Override
    public void onClick(View view) {
        if (view == btnSave){
            if (techName.getText().toString().isEmpty() || faultTitle.getText().toString().isEmpty()){
                Toast.makeText(context, "Fields can not be blank", Toast.LENGTH_SHORT).show();
            }
            else{ assignTask();}

        }
        if(view == btnPick){
            it.show(MainActivity.fragmentManager, "Technician_Pick");
        }
    }
}

