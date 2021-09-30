package com.webandcrafts.healwire.ui.my_orders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.webandcrafts.healwire.AppController;
import com.webandcrafts.healwire.NewBaseActivity;
import com.webandcrafts.healwire.ui.my_order_details.OrderDetailsActivity;
import com.webandcrafts.healwire.R;
import com.webandcrafts.healwire.adapter.MyOrderListAdapter;
import com.webandcrafts.healwire.models.Orders;
import com.webandcrafts.healwire.ui.home.HomeActivity;
import com.webandcrafts.healwire.utils.SharedPreferenceHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrdersActivity extends NewBaseActivity {

    ProgressDialog progressDialog;
    public List<Orders> ordersList = new ArrayList<Orders>();
    private ListView listViewOrder;
    private MyOrderListAdapter listOrderAdapter;
    private String email;
    TextView textViewEmpty;
    ImageView mBackIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
        email = sharedPreferenceHandler.getUserEmail(getApplicationContext());
        textViewEmpty = (TextView) findViewById(android.R.id.empty);
        listViewOrder = (ListView) findViewById(R.id.listViewOrder);
        mBackIcon = findViewById(R.id.iv_back);
        listOrderAdapter = new MyOrderListAdapter(this, ordersList);
        listViewOrder.setAdapter(listOrderAdapter);
        progressDialog = new ProgressDialog(this);


        getPrescriptionThumb(email);

        listViewOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                AppController.invoice_Id_position = i;

                Intent intentOrdersDetails = new Intent(MyOrdersActivity.this, OrderDetailsActivity.class);
                startActivity(intentOrdersDetails);
                overridePendingTransition(R.anim.right,R.anim.left);

            }
        });
        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               MyOrdersActivity.this.finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    //To load detials of My Prescription of a user

    public synchronized void getPrescriptionThumb(final String email) {

        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        StringRequest prescriptionOrderRequest = new StringRequest(Request.Method.POST, AppController.GET_PRESCRIPTION_THUMB_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();


                        try {

                            JSONObject mainResponse = new JSONObject(response);


                            String orderStatus = mainResponse.getString("status");
                            String responseMessage = mainResponse.getString("msg");

                            if (orderStatus.equals("SUCCESS")) {

                                JSONObject jsonObjectData = mainResponse.getJSONObject("data");
                                JSONArray jsonArrayPrescriptions = jsonObjectData.getJSONArray("prescriptions");


                                if (HomeActivity.paidPrescription.size() == 0) {

                                    textViewEmpty.setVisibility(View.VISIBLE);
                                } else {

                                    textViewEmpty.setVisibility(View.GONE);

                                }


                                for (int i = 0; i < HomeActivity.paidPrescription.size(); i++) {


                                    //-----------------------------------------------------------------------------------------------


                                    String create_on = HomeActivity.paidPrescription.get(i).getString("created_on");
                                    String shipping_status = HomeActivity.paidPrescription.get(i).getString("shipping_status");
                                    String path = HomeActivity.paidPrescription.get(i).getString("path");
                                    int invoice_id = HomeActivity.paidPrescription.get(i).getInt("invoice_id");
                                    String invoice = HomeActivity.paidPrescription.get(i).getString("invoice");

                                    Log.d("Dates", String.valueOf(create_on));

                                    Log.d("shipping_status : ", shipping_status);

                                    Orders orders = new Orders();

                                    orders.setDate(create_on);
                                    orders.setStatus(shipping_status);
                                    orders.setImageUrl(path);
                                    orders.setInvoice(invoice);


                                    ordersList.add(orders);

                                    AppController.invoiceIdList.add(invoice_id);

                                    Log.d("INVOICE_IDS", String.valueOf(AppController.invoiceIdList.get(i)));

                                }


                                listOrderAdapter.notifyDataSetChanged();
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
                textViewEmpty.setVisibility(View.VISIBLE);


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("email", email);

                Log.d("EMAIL = ", email);


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
        AppController.getInstance().addToRequestQueue(prescriptionOrderRequest);
//        doLogin.setRetryPolicy(new DefaultRetryPolicy(
//                (int) TimeUnit.SECONDS.toMillis(20),
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }//volley

    @Override
    public void onBackPressed() {
        MyOrdersActivity.this.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        super.onBackPressed();
    }
}
