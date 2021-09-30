package com.webandcrafts.healwire.ui.my_order_details;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.webandcrafts.healwire.AppController;
import com.webandcrafts.healwire.NewBaseActivity;
import com.webandcrafts.healwire.R;
import com.webandcrafts.healwire.adapter.ExpandableListAdapter;
import com.webandcrafts.healwire.models.PrescriptionCart;
import com.webandcrafts.healwire.ui.home.HomeActivity;
import com.webandcrafts.healwire.utils.SharedPreferenceHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailsActivity extends NewBaseActivity {


    DecimalFormat decimalFormatPrice = new DecimalFormat("#0.00"); // Set your desired format here.

    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListViewMedicine;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    private List<PrescriptionCart> prescriptionCartList = new ArrayList<PrescriptionCart>();


    String invoice;
    int sub_total, discount, tax, shipping, total;
    int invoice_id, item_id, unit_price, discount_percent, discount_cart, quantity, total_price;
    String item_code, item_name;
    String currency, currency_position, payment_url;


    SharedPreferenceHandler sharedPreferenceHandlerStatus;


    ProgressDialog progressDialog;
    String orderStatus, responseMessage;
    int list_invoice_id, list_position;

    TextView textViewOrderDetails;
    ImageView mBackIcon;

    TextView textViewSubTotalValue, textViewShippingCostValue, textViewDiscountValue, textViewNetPayableValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);


        textViewOrderDetails = (TextView) findViewById(R.id.textViewOrderDetails);

        progressDialog = new ProgressDialog(this);

        // get the listview
        expandableListViewMedicine = (ExpandableListView) findViewById(R.id.expandableListViewMedicine);

        LayoutInflater inflater = this.getLayoutInflater();
        RelativeLayout listFooterView = (RelativeLayout) inflater.inflate(
                R.layout.expand_order_list_footer, null);


        textViewSubTotalValue = (TextView) listFooterView.findViewById(R.id.textViewSubTotalValue);
        textViewShippingCostValue = (TextView) listFooterView.findViewById(R.id.textViewShippingCostValue);
        textViewDiscountValue = (TextView) listFooterView.findViewById(R.id.textViewDiscountValue);
        textViewNetPayableValue = (TextView) listFooterView.findViewById(R.id.textViewNetPayableValue);
        mBackIcon = findViewById(R.id.iv_back);


        LayoutInflater inflater2 = this.getLayoutInflater();
        RelativeLayout listHeaderView = (RelativeLayout) inflater2.inflate(
                R.layout.expand_order_list_header, null);


        expandableListViewMedicine.addHeaderView(listHeaderView);
        expandableListViewMedicine.addFooterView(listFooterView);


        sharedPreferenceHandlerStatus = new SharedPreferenceHandler();
        sharedPreferenceHandlerStatus.getPrescriptionStatus(getApplicationContext());
        sharedPreferenceHandlerStatus.getInvoiceStatus(getApplicationContext());

        Log.d("PREFS", "8888888888888888888");
        Log.d("STAT", String.valueOf(SharedPreferenceHandler.UNVERIFIED));


        getPrescriptionThumb(SharedPreferenceHandler.EMAIL);


        // expandableListAdapter = new ExpandableListAdapter(this, prescriptionCartList, prescriptionCartList);

        expandableListAdapter = new ExpandableListAdapter(this, prescriptionCartList);


        // setting list adapter
        expandableListViewMedicine.setAdapter(expandableListAdapter);


        expandableListViewMedicine.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {


                ExpandableListAdapter expandableListAdapter = (ExpandableListAdapter) expandableListViewMedicine.getExpandableListAdapter();
                if (expandableListAdapter == null) {
                    return;
                }
                for (int j = 0; j < expandableListAdapter.getGroupCount(); j++) {
                    if (j != i) {
                        expandableListViewMedicine.collapseGroup(j);
                    }
                }


            }
        });


        list_invoice_id = AppController.invoiceIdList.get(AppController.invoice_Id_position);
        list_position = AppController.invoice_Id_position;

        getPrescriptionDetailsByInvoiceId(list_invoice_id, list_position);


        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderDetailsActivity.this.finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });


    }


    //To load detials of My Prescription of a user


    public synchronized void getPrescriptionThumb(final String email) {

        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        StringRequest doLogin = new StringRequest(Request.Method.POST, AppController.GET_PRESCRIPTION_THUMB_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


//                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                        progressDialog.dismiss();


                        try {

                            JSONObject mainResponse = new JSONObject(response);


                            orderStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");

                            if (orderStatus.equals("SUCCESS")) {

                                JSONObject jsonObjectData = mainResponse.getJSONObject("data");
                                currency = jsonObjectData.getString("currency");
                                currency_position = jsonObjectData.getString("curr_position");
                                payment_url = jsonObjectData.getString("payment_url");

                                AppController.BASE_PAYMENT_URL = payment_url;


                                JSONArray jsonArrayPrescriptions = jsonObjectData.getJSONArray("prescriptions");


                                // Toast.makeText(getApplicationContext(),"ID:"+list_position,Toast.LENGTH_LONG).show();

                                //---------------------------------------------------------------------------------------------

                                invoice = HomeActivity.paidPrescription.get(list_position).getString("invoice");
                                invoice_id = HomeActivity.paidPrescription.get(list_position).getInt("invoice_id");
                                sub_total = HomeActivity.paidPrescription.get(list_position).getInt("sub_total");
                                discount = HomeActivity.paidPrescription.get(list_position).getInt("discount");
                                tax = HomeActivity.paidPrescription.get(list_position).getInt("tax");
                                shipping = HomeActivity.paidPrescription.get(list_position).getInt("shipping");
                                total = HomeActivity.paidPrescription.get(list_position).getInt("total");


                                JSONArray jsonArrayCart = HomeActivity.paidPrescription.get(list_position).getJSONArray("cart");


                                //---------------------------------------------------------------------------------------------


//                                  invoice = jsonArrayPrescriptions.getJSONObject(list_position).getString("invoice");
//                                  invoice_id  =jsonArrayPrescriptions.getJSONObject(list_position).getInt("invoice_id");
//                                  sub_total =  jsonArrayPrescriptions.getJSONObject(list_position).getInt("sub_total");
//                                  discount = jsonArrayPrescriptions.getJSONObject(list_position).getInt("discount");
//                                  tax = jsonArrayPrescriptions.getJSONObject(list_position).getInt("tax");
//                                  shipping = jsonArrayPrescriptions.getJSONObject(list_position).getInt("shipping");
//                                  total =  jsonArrayPrescriptions.getJSONObject(list_position).getInt("total");
//
//
//                                JSONArray jsonArrayCart = jsonArrayPrescriptions.getJSONObject(list_position).getJSONArray("cart");


                                // Traverse the jsonArray

                                for (int i = 0; i < jsonArrayCart.length(); i++) {


                                    PrescriptionCart prescriptionCart = new PrescriptionCart();


                                    item_id = jsonArrayCart.getJSONObject(i).getInt("item_id");
                                    item_code = jsonArrayCart.getJSONObject(i).getString("item_code");
                                    item_name = jsonArrayCart.getJSONObject(i).getString("item_name");
                                    unit_price = jsonArrayCart.getJSONObject(i).getInt("unit_price");
                                    discount_percent = jsonArrayCart.getJSONObject(i).getInt("discount_percent");
                                    discount_cart = jsonArrayCart.getJSONObject(i).getInt("discount");
                                    quantity = jsonArrayCart.getJSONObject(i).getInt("quantity");
                                    total_price = jsonArrayCart.getJSONObject(i).getInt("total_price");

                                    // Toast.makeText(getApplicationContext(),""+item_name,Toast.LENGTH_SHORT).show();

                                    prescriptionCart.setItem_name(item_name);
                                    prescriptionCart.setQuantity(quantity);
                                    prescriptionCart.setTotal_price(total_price);
                                    prescriptionCart.setUnit_price(unit_price);
                                    prescriptionCart.setCurrency(currency);
                                    prescriptionCart.setCurr_position(currency_position);
                                    prescriptionCart.setDiscount_cart(discount_cart);
                                    prescriptionCart.setDiscount_percent(discount_percent);


                                    prescriptionCartList.add(prescriptionCart);


                                }


//
//                                for(int i = 0;i<jsonArrayPrescriptions.length();i++){
//
//                                    Prescription prescription = new Prescription();
//
//
//                                    String create_on = jsonArrayPrescriptions.getJSONObject(i).getString("created_on");
//                                    String pres_status = jsonArrayPrescriptions.getJSONObject(i).getString("pres_status");
//                                    String path = jsonArrayPrescriptions.getJSONObject(i).getString("path");
//                                    int invoice_id = jsonArrayPrescriptions.getJSONObject(i).getInt("invoice_id");
//
//                                    prescription.setDate(create_on);
//                                    prescription.setStatus(pres_status);
//                                    prescription.setImageUrl(path);
//
//                                    AppController.invoiceIdList.add(invoice_id);
//
//                                    Log.d("INVOICE_IDS",String.valueOf(AppController.invoiceIdList.get(i)));
//
//
//
//
////                                    HashMap<String, String> hm = new HashMap<String,String>();
////                                    hm.put("create_on", create_on);
////                                    hm.put("pres_status", pres_status);
////                                    hm.put("path", path);
//
//                                  //  prescriptionList.add(prescription);
//
//
//
//                                }


                                //listPrescriptionAdapter.notifyDataSetChanged();

                                if (currency_position.equals("BEFORE")) {

                                    textViewSubTotalValue.setText(currency + " " + decimalFormatPrice.format(sub_total));
                                    textViewShippingCostValue.setText(currency + " " + decimalFormatPrice.format(shipping));
                                    textViewDiscountValue.setText(currency + " " + decimalFormatPrice.format(discount));

                                    textViewNetPayableValue.setText(currency + " " + decimalFormatPrice.format(total));


                                } else {

                                    textViewSubTotalValue.setText(decimalFormatPrice.format(sub_total) + " " + currency);
                                    textViewShippingCostValue.setText(decimalFormatPrice.format(shipping) + " " + currency);
                                    textViewDiscountValue.setText(decimalFormatPrice.format(discount) + " " + currency);
                                    textViewNetPayableValue.setText(decimalFormatPrice.format(total) + " " + currency);


                                }


                                // buttonPayNow.setText("Pay Now  " +textViewNetPayableValue.getText().toString());


                                expandableListAdapter.notifyDataSetChanged();

                            }
                        } catch (Exception e) {
                            // messageHandler.sendEmptyMessage(99);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  messageHandler.sendEmptyMessage(98);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("email", email);


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
//        doLogin.setRetryPolicy(new DefaultRetryPolicy(
//                (int) TimeUnit.SECONDS.toMillis(20),
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }//volley


    //To load detials of My Prescription of a user


    public synchronized void getPrescriptionDetailsByInvoiceId(final int invoice_id, final int position) {

        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        StringRequest doLogin = new StringRequest(Request.Method.POST, AppController.GET_PRESCRIPTION_THUMB_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


//                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                        progressDialog.dismiss();


                        try {

                            JSONObject mainResponse = new JSONObject(response);


                            orderStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");

                            if (orderStatus.equals("SUCCESS")) {

                                JSONObject jsonObjectData = mainResponse.getJSONObject("data");
                                JSONArray jsonArrayPrescriptions = jsonObjectData.getJSONArray("prescriptions");


                                // for(int i = 0;i<jsonArrayPrescriptions.length();i++){

                                //Prescription prescription = new Prescription();

//                                    if(list_invoice_id == invoice_id){


                                //---------------------------------------------------------------------------------------------

                                String created_on = HomeActivity.paidPrescription.get(list_position).getString("created_on");
                                String pres_status = HomeActivity.paidPrescription.get(list_position).getString("pres_status");
                                String path = HomeActivity.paidPrescription.get(list_position).getString("path");
                                int invoice_id = HomeActivity.paidPrescription.get(list_position).getInt("invoice_id");
                                int invoice_status_id = HomeActivity.paidPrescription.get(list_position).getInt("invoice_status_id");
                                int pres_status_id = HomeActivity.paidPrescription.get(list_position).getInt("pres_status_id");


                                //---------------------------------------------------------------------------------------------
//                                        String created_on = HomeActivity.unpaidPrescription.get(list_position).getString("created_on");
//                                        String pres_status = jsonArrayPrescriptions.getJSONObject(list_position).getString("pres_status");
//                                        String path = jsonArrayPrescriptions.getJSONObject(list_position).getString("path");
//                                        int invoice_id = jsonArrayPrescriptions.getJSONObject(list_position).getInt("invoice_id");
//                                        int invoice_status_id = jsonArrayPrescriptions.getJSONObject(list_position).getInt("invoice_status_id");
//                                        int pres_status_id = jsonArrayPrescriptions.getJSONObject(list_position).getInt("pres_status_id");


                                Log.d("BLACKBOARD", "VALUE");
                                Log.d("MATCH_IDS", String.valueOf(created_on));
                                Log.d("MATCH_IDS", String.valueOf(pres_status));
                                Log.d("MATCH_IDS", String.valueOf(path));
                                Log.d("MATCH_IDS", String.valueOf(invoice_id));

//                                        break;
//                                    }


                                textViewOrderDetails.setText(created_on);

//                                       sharedPreferenceHandlerStatus.getPrescriptionStatus(getApplicationContext());
//                                       sharedPreferenceHandlerStatus.getInvoiceStatus(getApplicationContext());

                                Log.d("LOOP", "FOO");


                                if (SharedPreferenceHandler.UNVERIFIED == pres_status_id) {

                                    //   buttonPayNow.setVisibility(View.INVISIBLE);
                                    // Toast.makeText(getApplicationContext(),"NOT visible",Toast.LENGTH_LONG).show();


                                } else if (SharedPreferenceHandler.VERIFIED == pres_status_id && SharedPreferenceHandler.PENDING == invoice_status_id) {

                                    //    buttonPayNow.setVisibility(View.VISIBLE);
                                    //  Toast.makeText(getApplicationContext()," visible",Toast.LENGTH_LONG).show();

                                } else if (SharedPreferenceHandler.VERIFIED == pres_status_id && SharedPreferenceHandler.PAID == invoice_status_id) {

                                    // Toast.makeText(getApplicationContext(),"Paid",Toast.LENGTH_LONG).show();

//                                    Intent intentMyOrders = new Intent(getApplicationContext(),MyOrdersActivity.class);
//                                    startActivity(intentMyOrders);

                                }


//                                      if(pres_status.equals("Verified")){
//
//
//
//                                      }
//                                    else if(pres_status.equals("Unverified")){
//
//                                          buttonPayNow.setVisibility(View.INVISIBLE);
//                                      }


//                                    prescription.setDate(create_on);
//                                    prescription.setStatus(pres_status);
//                                    prescription.setImageUrl(path);

                                // AppController.invoiceIdList.add(invoice_id);


//                                    HashMap<String, String> hm = new HashMap<String,String>();
//                                    hm.put("create_on", create_on);
//                                    hm.put("pres_status", pres_status);
//                                    hm.put("path", path);

                                // prescriptionList.add(prescription);


                                // }


                                // listPrescriptionAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            // messageHandler.sendEmptyMessage(99);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  messageHandler.sendEmptyMessage(98);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

//                params.put("email",email);


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
//        doLogin.setRetryPolicy(new DefaultRetryPolicy(
//                (int) TimeUnit.SECONDS.toMillis(20),
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }//volley


    @Override
    public void onBackPressed() {
        OrderDetailsActivity.this.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        super.onBackPressed();
    }
}
