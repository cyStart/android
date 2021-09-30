package com.simbazet.mbizvo.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.simbazet.mbizvo.Constants;
import com.simbazet.mbizvo.MainActivity;
import com.simbazet.mbizvo.MapsActivity;
import com.simbazet.mbizvo.R;
import com.simbazet.mbizvo.RequestHandler;
import com.simbazet.mbizvo.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersFragment extends Fragment implements View.OnClickListener{

    private RecyclerView rvUsers;
    private RecyclerView.Adapter rvAdapter;
    private List<UsersData> usersDataList;
    private Context mCtx;
    private FloatingActionButton btnAdd;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.users_fragment, container, false);
        //return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvUsers = view.findViewById(R.id.rvUsers);
        btnAdd = view.findViewById(R.id.btnAdd);
        rvUsers.hasFixedSize();
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Reporting fault");
        progressDialog.setMessage("please wait while processing");

        usersDataList = new ArrayList<>();
        loadUsers();

        btnAdd.setOnClickListener(this);

        //**************ON ITEM CLICK LISTENER******************
        rvUsers.addOnItemTouchListener(new RequestOnClick(getContext(), rvUsers, new RequestOnClick.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final UsersData users = usersDataList.get(position);
                String usertype = SharedPrefManager.getInstance(getContext()).getUserType();

                if (usertype.equals("Resident")){

                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(users.getUserFname() + " " + users.getUserLname());
                builder.setMessage("Phone : " + users.getUserPhone() + "\n" +
                        "User Type : " + users.getUserType());

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                });
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                });
                builder.create();
                builder.show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void loadUsers() {
        usersDataList.clear();
        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.users,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting users object from json array
                                JSONObject users = array.getJSONObject(i);

                                usersDataList.add(new UsersData(
                                        users.getInt("userID"),
                                        users.getString("userFname"),
                                        users.getString("userLname"),
                                        users.getString("userPhone"),
                                        users.getString("userType")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            rvAdapter = new UsersAdapter(usersDataList,getContext());
                            rvUsers.setAdapter(rvAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("userType", SharedPrefManager.getInstance(getContext()).getUserType());
                params.put("userID", SharedPrefManager.getInstance(getContext()).getUserID().toString());
                return params;
            }
        };
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);

        //adding our stringrequest to queue
        //Volley.newRequestQueue(getContext()).add(stringRequest);
    }


    @Override
    public void onClick(View view) {
        if (view == btnAdd){
            MainActivity.selectedAction.setText("Add User");
            MainActivity.fragmentManager.beginTransaction().replace(R.id.containerView, new AddUserFragment()).commit();
        }
    }
}
