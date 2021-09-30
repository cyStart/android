package com.simbazet.mbizvo.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.solver.widgets.ConstraintWidget.GONE;
import static com.simbazet.mbizvo.fragments.RequestsAdapter.faultsDataLists;

public class RequestsFragment extends Fragment implements View.OnClickListener{

    private RecyclerView rvRequests;
    private RecyclerView.Adapter rvAdapter;
    private List<RequestsData> faultsDataList;
    private List<RequestsData> assignedRequestsDataList;
    private List<RequestsData> faultsDL;
    private Context mCtx;
    private FloatingActionButton btnAdd;
    ProgressDialog progressDialog;
    private LinearLayout btnNew, btnAssigned, btnCompleted;
    private static int data = 0;
    RequestsData faults;
    //final TechniciansFragment tf = new TechniciansFragment();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.faults_fragment, container, false);
        //return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnNew = view.findViewById(R.id.btnNew);
        btnAssigned = view.findViewById(R.id.btnAssigned);
        btnCompleted = view.findViewById(R.id.btnCompleted);

        rvRequests = view.findViewById(R.id.rvRequests);
        btnAdd = view.findViewById(R.id.btnAdd);
        rvRequests.hasFixedSize();
        rvRequests.setLayoutManager(new LinearLayoutManager(getContext()));
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Reporting fault");
        progressDialog.setMessage("please wait while processing");

        faultsDataList = new ArrayList<>();
        assignedRequestsDataList = new ArrayList<>();
        faultsDL = new ArrayList<>();
        loadRequests();
        loadAssignedRequests();
        //loadAssignedRequests();

        if (!SharedPrefManager.getInstance(getActivity()).getUserType().equals("Resident")){
            btnAdd.hide();
        }

        btnAdd.setOnClickListener(this);
        btnNew.setOnClickListener(this);
        btnAssigned.setOnClickListener(this);
        btnCompleted.setOnClickListener(this);

        //**************ON ITEM CLICK LISTENER******************
        rvRequests.addOnItemTouchListener(new RequestOnClick(getContext(), rvRequests, new RequestOnClick.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //final RequestsData faults = null;
                if(data == 0 ){faults = faultsDataLists.get(position);}
                if(data == 1 ){faults = faultsDataLists.get(position);}
                String usertype = SharedPrefManager.getInstance(getContext()).getUserType();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(faults.getRequestTitle());
                final LayoutInflater inflater = requireActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.fault_dialog, null);
                builder.setView(dialogView);

                LinearLayout LLtechDetails = dialogView.findViewById(R.id.LLtechDetails);
                LinearLayout LLtechComment = dialogView.findViewById(R.id.LLtechComment);
                TextView title = dialogView.findViewById(R.id.title);
                TextView location = dialogView.findViewById(R.id.location);
                TextView Description = dialogView.findViewById(R.id.Description);
                TextView faultStatus = dialogView.findViewById(R.id.faultStatus);
                TextView faultDated = dialogView.findViewById(R.id.faultDated);
                TextView taskStatus = dialogView.findViewById(R.id.taskStatus);
                TextView taskDates = dialogView.findViewById(R.id.taskDates);
                TextView techDetails = dialogView.findViewById(R.id.techDetails);
                TextView techComment = dialogView.findViewById(R.id.techComment);
                ImageView imageView = dialogView.findViewById(R.id.imageView);

                FloatingActionButton direction = dialogView.findViewById(R.id.direction);

                if (!usertype.equals("Technician")){
                    direction.hide();
                }
                if (!usertype.equals("Admin")){
                    LLtechDetails.setVisibility(View.GONE);
                }
                if (faults.getTaskStatus().equals("Reported")){
                    LLtechComment.setVisibility(View.GONE);
                }

                title.setText(faults.getRequestTitle());
                location.setText(faults.getRequestLocation());
                Description.setText(faults.getRequestDesc());
                faultStatus.setText(faults.getRequestStatus());
                faultDated.setText(faults.getRequestDate());
                taskStatus.setText(faults.getTaskStatus());
                String dates = faults.getTaskStart() + " - " + faults.getTaskEnd();
                taskDates.setText(dates);
                String tech = faults.getUserFname() +" "+ faults.getUserLname() +" (" + faults.getUserPhone() +")";
                techDetails.setText(tech);
                techComment.setText(faults.getTaskSignOff());
                Picasso.get().load(Constants.Root_URL + "uploads/" + faults.getImageURL()).into(imageView);
                if(faults.getImageURL().equals("")){imageView.setImageResource(R.drawable.image_placeholder);}

                direction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            if(MainActivity.isGooglePlayServicesAvailable(getActivity())){
                                Intent mapsInt = new Intent(getContext(), MapsActivity.class);
                                mapsInt.putExtra("faultLat", faults.getRequestLat());
                                mapsInt.putExtra("faultLong", faults.getRequestLong());
                                mapsInt.putExtra("faultTitle", faults.getRequestTitle());
                                startActivity(mapsInt);
                            }
                            else {
                                Toast.makeText(getContext(), "Oops!!! INSTALL or UPDATE Google Play Services to use this service", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception ex){
                            Toast.makeText(getContext(), "Error : " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                        //startActivity(new Intent(getContext(), MapsActivity.class));
                    }
                });

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                String btnTitle = "";
                if (usertype.equals("Technician")){
                    if ("Completed".equals(faults.getTaskStatus())){
                        Toast.makeText(getContext(), "Request completed and signed off", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        builder.setPositiveButton("Sign Off", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MainActivity.faultTitle = faults.getRequestTitle();
                                MainActivity.taskID = faults.getTaskID();
                                MainActivity.faultID = faults.getRequestID();
                                MainActivity.selectedAction.setText("Sign Off Task");
                                MainActivity.fragmentManager.beginTransaction().replace(R.id.containerView, new SignOffFragment()).commit();

                            }
                        });
                    }
                }

                if (usertype.equals("Admin")){
                    if ("Assigned".equals(faults.getTaskStatus())){
                        Toast.makeText(getContext(), "Request already assigned", Toast.LENGTH_SHORT).show();
                    }
                    else if ("Completed".equals(faults.getTaskStatus())){
                        Toast.makeText(getContext(), "Request resolved", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        builder.setPositiveButton("Assign Request", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MainActivity.faultTitle = faults.getRequestTitle();
                                MainActivity.taskID = faults.getTaskID();
                                MainActivity.selectedAction.setText("Assign Request");
                                MainActivity.fragmentManager.beginTransaction().replace(R.id.containerView, new AssignTaskFragment()).commit();

                            }
                        });
                    }

                }



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
                //if(view.check)
                view.setBackgroundColor(Color.BLUE);
            }
        }));
    }


    private void loadRequests() {
        faultsDataList.clear();
        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.faults,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting faults object from json array
                                JSONObject faults = array.getJSONObject(i);

                                faultsDataList.add(new RequestsData(
                                        faults.getInt("requestID"),
                                        faults.getString("requestTitle"),
                                        faults.getString("requestDesc"),
                                        faults.getString("requestLat"),
                                        faults.getString("requestLong"),
                                        faults.getInt("userID"),
                                        faults.getString("requestStatus"),
                                        faults.getString("requestDate"),
                                        faults.getString("requestLocation"),
                                        faults.getString("imageURL"),
                                        faults.getString("techID"),
                                        faults.getInt("taskID"),
                                        faults.getString("taskStart"),
                                        faults.getString("taskEnd"),
                                        faults.getString("taskStatus"),
                                        faults.getString("taskSignOff"),
                                        faults.getString("userFname"),
                                        faults.getString("userLname"),
                                        faults.getString("userPhone")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            rvAdapter = new RequestsAdapter(faultsDataList,getContext());
                            rvRequests.setAdapter(rvAdapter);
                            data = 0;
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

    private void loadAssignedRequests() {
        faultsDataList.clear();
        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.assignedRequests,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting faults object from json array
                                JSONObject faults = array.getJSONObject(i);

                                assignedRequestsDataList.add(new RequestsData(
                                        faults.getInt("requestID"),
                                        faults.getString("requestTitle"),
                                        faults.getString("requestDesc"),
                                        faults.getString("requestLat"),
                                        faults.getString("requestLong"),
                                        faults.getInt("userID"),
                                        faults.getString("requestStatus"),
                                        faults.getString("requestDate"),
                                        faults.getString("requestLocation"),
                                        faults.getString("imageURL"),
                                        faults.getString("techID"),
                                        faults.getInt("taskID"),
                                        faults.getString("taskStart"),
                                        faults.getString("taskEnd"),
                                        faults.getString("taskStatus"),
                                        faults.getString("taskSignOff"),
                                        faults.getString("userFname"),
                                        faults.getString("userLname"),
                                        faults.getString("userPhone")
                                ));
                            }

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
        int userid = SharedPrefManager.getInstance(getActivity()).getUserID();
        String usertype = SharedPrefManager.getInstance(getActivity()).getUserType();

        if (view == btnAdd){
            MainActivity.selectedAction.setText("Report Request");
            MainActivity.fragmentManager.beginTransaction().replace(R.id.containerView, new ReportRequestFragment()).commit();
        }

        if (view == btnNew){
            MainActivity.selectedAction.setText("New Requests");
            faultsDL.clear();
            for (RequestsData fd : faultsDataList) {
                if("Reported".equals(fd.getRequestStatus())) { faultsDL.add(fd);  }
            }

            rvAdapter = new RequestsAdapter(faultsDL,getContext());
            rvRequests.setAdapter(rvAdapter);
            data = 1;
        }

        if (view == btnAssigned){
            faultsDL.clear();
            MainActivity.selectedAction.setText("Assigned Requests");
            for (RequestsData fd : assignedRequestsDataList) {
                if("Assigned".equals(fd.getTaskStatus())) { faultsDL.add(fd);  }
            }

            rvAdapter = new RequestsAdapter(faultsDL,getContext());
            rvRequests.setAdapter(rvAdapter);
            data = 1;
        }

        if (view == btnCompleted){
            faultsDL.clear();
            MainActivity.selectedAction.setText("Assigned Requests");
            for (RequestsData fd : assignedRequestsDataList) {
                if("Completed".equals(fd.getTaskStatus())) { faultsDL.add(fd);  }
            }

            rvAdapter = new RequestsAdapter(faultsDL,getContext());
            rvRequests.setAdapter(rvAdapter);
            data = 1;
        }

    }



}
