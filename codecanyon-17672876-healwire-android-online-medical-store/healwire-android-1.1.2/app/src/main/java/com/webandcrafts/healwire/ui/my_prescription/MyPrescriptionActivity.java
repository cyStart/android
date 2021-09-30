package com.webandcrafts.healwire.ui.my_prescription;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;
import com.webandcrafts.healwire.AppController;
import com.webandcrafts.healwire.NewBaseActivity;
import com.webandcrafts.healwire.ui.prescription_details.PrescriptionDetailsActivity;
import com.webandcrafts.healwire.R;
import com.webandcrafts.healwire.adapter.MyPrescriptionListAdapter;
import com.webandcrafts.healwire.models.Prescription;
import com.webandcrafts.healwire.ui.home.HomeActivity;
import com.webandcrafts.healwire.utils.SharedPreferenceHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPrescriptionActivity extends NewBaseActivity {

    private Toolbar mToolbar;
    ProgressDialog progressDialog;
    String prescriptionStatus, responseMessage, email;
    private SwipeRefreshLayout swipeRefreshLayout;
    TextView textViewEmpty;
    SharedPreferenceHandler sharedPreferenceHandlerStatus;
    private List<Prescription> prescriptionList = new ArrayList<Prescription>();
    private ListView listViewPrescription;
    private MyPrescriptionListAdapter listPrescriptionAdapter;
    private ImageView mBackIcon;
    int list_position;
    JSONObject jsonObjectPrescription;
    public static List<JSONObject> unpaidPrescriptionlist = new ArrayList<JSONObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
        email = sharedPreferenceHandler.getUserEmail(getApplicationContext());
        setContentView(R.layout.activity_my_prescription);
        list_position = AppController.invoice_Id_position;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        textViewEmpty = (TextView) findViewById(android.R.id.empty);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        listViewPrescription = (ListView) findViewById(R.id.listViewPrescription);
        listPrescriptionAdapter = new MyPrescriptionListAdapter(this, prescriptionList);
        listViewPrescription.setAdapter(listPrescriptionAdapter);
        mBackIcon = findViewById(R.id.iv_back);
        progressDialog = new ProgressDialog(this);
        getPrescriptionThumb(email);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPrescriptionThumb(email);
            }
        });


        listViewPrescription.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppController.invoice_Id_position = i;
                Prescription prescription = prescriptionList.get(AppController.invoice_Id_position);
                Intent intentPrescriptionDetails = new Intent(MyPrescriptionActivity.this, PrescriptionDetailsActivity.class);
                intentPrescriptionDetails.putExtra("status",prescription.getStatus());
                intentPrescriptionDetails.putExtra("date",prescription.getDate());
                intentPrescriptionDetails.putExtra("invoice",prescription.getInvoiceStatus());
                startActivity(intentPrescriptionDetails);
                overridePendingTransition(R.anim.right, R.anim.left);

            }
        });
        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyPrescriptionActivity.this.finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    //To load detials of My Prescription of a user


    public synchronized void getPrescriptionThumb(final String email) {

        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        unpaidPrescriptionlist.clear();


        StringRequest doLogin = new StringRequest(Request.Method.POST, AppController.GET_PRESCRIPTION_THUMB_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        if (swipeRefreshLayout.isRefreshing())
                            swipeRefreshLayout.setRefreshing(false);
                        prescriptionList.clear();
                        try {

                            JSONObject mainResponse = new JSONObject(response);
                            prescriptionStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");
                            if (prescriptionStatus.equals("SUCCESS")) {
                                JSONObject jsonObjectData = mainResponse.getJSONObject("data");
                                JSONArray jsonArrayPrescriptions = jsonObjectData.getJSONArray("prescriptions");

                                for (int i = 0; i < jsonArrayPrescriptions.length(); i++) {

                                    jsonObjectPrescription = jsonArrayPrescriptions.getJSONObject(i);

                                            unpaidPrescriptionlist.add(jsonObjectPrescription);
                                    if (MyPrescriptionActivity.unpaidPrescriptionlist.size() == 0) {
                                        textViewEmpty.setVisibility(View.VISIBLE);
                                    } else {
                                        textViewEmpty.setVisibility(View.GONE);
                                    }

                                    String create_on = jsonArrayPrescriptions.getJSONObject(i).getString("created_on");
                                    Log.d("Dates", String.valueOf(create_on));
                                    String pres_status = jsonArrayPrescriptions.getJSONObject(i).getString("pres_status");
                                    String path = jsonArrayPrescriptions.getJSONObject(i).getString("path");
                                    int invoice_id = jsonArrayPrescriptions.getJSONObject(i).getInt("invoice_id");
                                    int pres_status_id = jsonArrayPrescriptions.getJSONObject(i).getInt("pres_status_id");
                                    int invoice_status_id = jsonArrayPrescriptions.getJSONObject(i).getInt("invoice_status_id");
                                    String invoice_status = jsonArrayPrescriptions.getJSONObject(i).getString("invoice_status");
                                    Prescription prescription = new Prescription();
                                    prescription.setDate(create_on);
                                    prescription.setStatus(pres_status);
                                    prescription.setStatusId(pres_status_id);
                                    prescription.setImageUrl(path);
                                    prescription.setInvoice_Id(invoice_id);
                                    prescription.setInvoiceStatus(invoice_status);
                                    prescriptionList.add(prescription);
                                    AppController.invoiceIdList.add(invoice_id);
                                    Log.d("INVOICE_IDS", String.valueOf(AppController.invoiceIdList.get(i)));

                                }




                                listPrescriptionAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            // messageHandler.sendEmptyMessage(99);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  messageHandler.sendEmptyMessage(98);

                Log.d("EMPTY", "NO DATA FROM SERVER");

                progressDialog.dismiss();
                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
                textViewEmpty.setVisibility(View.VISIBLE);


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("email", email);

//                Toast.makeText(getApplicationContext(),"mail:"+email,Toast.LENGTH_LONG).show();


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
        AppController.getInstance().addToRequestQueue(doLogin);

    }//volley

    @Override
    public void onBackPressed() {
        MyPrescriptionActivity.this.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        super.onBackPressed();
    }
}
