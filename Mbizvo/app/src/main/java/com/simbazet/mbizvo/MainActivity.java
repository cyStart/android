package com.simbazet.mbizvo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.simbazet.mbizvo.fragments.RequestsAdapter;
import com.simbazet.mbizvo.fragments.RequestsData;
import com.simbazet.mbizvo.fragments.HomeFragment;
import com.simbazet.mbizvo.fragments.RequestsFragment;
import com.simbazet.mbizvo.fragments.ItemsFragment;
import com.simbazet.mbizvo.fragments.SurburbsData;
import com.simbazet.mbizvo.fragments.UsersFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.simbazet.mbizvo.fragments.AssignTaskFragment.techName;

public class MainActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        ItemsFragment.TechnicianFRagmentListener{

    public static FragmentManager fragmentManager;
    public static PackageManager packageManager;
    public static TextView selectedAction;
    //Request assignment
    public static int taskID, techID, faultID;
    public static String faultTitle;

    //setting sites on maps
    public static List<SitesData> sitesDataList;
    public static List<SitesData> sitesList;
    public static List<SurburbsData> surburbsList;
    public static String mapsCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        packageManager = getPackageManager();
        selectedAction = findViewById(R.id.selectedAction);
        fragmentManager = getSupportFragmentManager();

        BottomNavigationView navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(this);

        String userType =  SharedPrefManager.getInstance(this).getUserType();
        if(userType.equals("Resident") || userType.equals("Technician")){
            navigationView.getMenu().removeItem(R.id.nav_users);
        }
        displayFragment(new HomeFragment());
        selectedAction.setText("Dashboard");

        //Initialising List
        sitesDataList = new ArrayList<>();
        sitesList = new ArrayList<>();
        surburbsList = new ArrayList<>();
        loadRequests();
        loadSurburbs();

    }

    public void displayFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.containerView, fragment).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_logout:
                String ipAddres = SharedPrefManager.getInstance(this).getIP(); // store ip first
                SharedPrefManager.getInstance(this).logout(); // clear all shared preferences
                SharedPrefManager.getInstance(this).setIP(ipAddres); // set back IP
                finish();
                startActivity(new Intent(this, Login.class));
                break;
            default:
                Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;
        switch(menuItem.getItemId()){
            case R.id.nav_home:
                fragment = new HomeFragment();
                selectedAction.setText("Dashboard");
                menuItem.setChecked(true);
                break;

            case R.id.nav_faults:
                fragment = new RequestsFragment();
                selectedAction.setText("Requests");
                menuItem.setChecked(true);
                break;

            case R.id.nav_users:
                fragment = new UsersFragment();
                selectedAction.setText("Users");
                menuItem.setChecked(true);
                break;
        }
        if (fragment != null){
            displayFragment(fragment);
        }
        return false;
    }

    public static boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if(status != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void sendTechnician(int id, String name) {
        techName.setText(name);
        techID = id;
    }

    private void loadRequests() {
        sitesDataList.clear();
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

                                sitesList.add(new SitesData(
                                        faults.getString("requestLat"),
                                        faults.getString("requestLong"),
                                        faults.getString("requestTitle"),
                                        faults.getString("techID"),
                                        faults.getString("userID"),
                                        faults.getString("taskStatus"),
                                        faults.getString("requestStatus")
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
                params.put("userType", SharedPrefManager.getInstance(MainActivity.this).getUserType());
                params.put("userID", SharedPrefManager.getInstance(MainActivity.this).getUserID().toString());
                return params;
            }
        };
        RequestHandler.getInstance(MainActivity.this).addToRequestQueue(stringRequest);

        //adding our stringrequest to queue
        //Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    private void loadSurburbs() {
        surburbsList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.surburbs,
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

                                surburbsList.add(new SurburbsData(
                                        faults.getString("surburb"),
                                        faults.getInt("total")
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
                params.put("userType", SharedPrefManager.getInstance(MainActivity.this).getUserType());
                params.put("userID", SharedPrefManager.getInstance(MainActivity.this).getUserID().toString());
                return params;
            }
        };
        RequestHandler.getInstance(MainActivity.this).addToRequestQueue(stringRequest);

        //adding our stringrequest to queue
        //Volley.newRequestQueue(getContext()).add(stringRequest);
    }
}
