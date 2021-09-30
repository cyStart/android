package com.webandcrafts.healwire.ui.prescription_details;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.webandcrafts.healwire.ui.payment.WebViewPaymentActivity;
import com.webandcrafts.healwire.adapter.ExpandableListAdapter;
import com.webandcrafts.healwire.models.PrescriptionCart;
import com.webandcrafts.healwire.ui.home.HomeActivity;
import com.webandcrafts.healwire.ui.my_prescription.MyPrescriptionActivity;
import com.webandcrafts.healwire.utils.SharedPreferenceHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrescriptionDetailsActivity extends NewBaseActivity {

    static PrescriptionDetailsActivity prescriptionDetailsActivity;

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
    String prescriptionStatus, responseMessage;
    int list_invoice_id, list_position;
    ImageView mBackIcon;
    TextView mTextViewPrescriptionDetails;
    TextView textViewSubTotalValue, textViewShippingCostValue, textViewDiscountValue, textViewNetPayableValue;
    Button buttonPayNow;


    public static PrescriptionDetailsActivity getInstance() {
        return prescriptionDetailsActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_details);
        prescriptionDetailsActivity = this;
        mTextViewPrescriptionDetails = findViewById(R.id.textViewPrescriptionDetails);
        mBackIcon = findViewById(R.id.iv_back);
        buttonPayNow = findViewById(R.id.buttonPayNow);
        // buttonPayNow.setVisibility(View.INVISIBLE);
        progressDialog = new ProgressDialog(this);
        // get the listview
        expandableListViewMedicine = findViewById(R.id.expandableListViewMedicine);
        LayoutInflater inflater = this.getLayoutInflater();
        RelativeLayout listFooterView = (RelativeLayout) inflater.inflate(
                R.layout.expandable_list_footer_view, null);

        textViewSubTotalValue = listFooterView.findViewById(R.id.textViewSubTotalValue);
        textViewShippingCostValue = listFooterView.findViewById(R.id.textViewShippingCostValue);
        textViewDiscountValue = listFooterView.findViewById(R.id.textViewDiscountValue);
        textViewNetPayableValue = listFooterView.findViewById(R.id.textViewNetPayableValue);


        LayoutInflater inflater2 = this.getLayoutInflater();
        RelativeLayout listHeaderView = (RelativeLayout) inflater2.inflate(
                R.layout.expandable_list_header_view, null);
        expandableListViewMedicine.addHeaderView(listHeaderView);
        expandableListViewMedicine.addFooterView(listFooterView);
        sharedPreferenceHandlerStatus = new SharedPreferenceHandler();
        sharedPreferenceHandlerStatus.getPrescriptionStatus(getApplicationContext());
        sharedPreferenceHandlerStatus.getInvoiceStatus(getApplicationContext());
        Log.d("PREFS", "8888888888888888888");
        Log.d("STAT", String.valueOf(SharedPreferenceHandler.UNVERIFIED));
        getPrescriptionThumb(SharedPreferenceHandler.EMAIL);

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

        Intent mIntentValues = getIntent();
        if (mIntentValues.getExtras() != null) {
            String date = mIntentValues.getStringExtra("date");
            String status = mIntentValues.getStringExtra("status");
            String invoice = mIntentValues.getStringExtra("invoice");
            mTextViewPrescriptionDetails.setText(date);
            setButtonWithstatus(status, invoice);
            // buttonPayNow.setVisibility(View.VISIBLE);
        }

        buttonPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppController.INVOICE_ID = list_invoice_id;

                Intent intentWebViewPayment = new Intent(getApplicationContext(), WebViewPaymentActivity.class);
                startActivity(intentWebViewPayment);
                overridePendingTransition(R.anim.right, R.anim.left);
            }
        });

        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrescriptionDetailsActivity.this.finish();
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

                        progressDialog.dismiss();

                        try {

                            JSONObject mainResponse = new JSONObject(response);


                            prescriptionStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");

                            if (prescriptionStatus.equals("SUCCESS")) {

                                JSONObject jsonObjectData = mainResponse.getJSONObject("data");
                                currency = jsonObjectData.getString("currency");
                                currency_position = jsonObjectData.getString("curr_position");
                                payment_url = jsonObjectData.getString("payment_url");

                                AppController.BASE_PAYMENT_URL = payment_url;

                                JSONArray jsonArrayCart = MyPrescriptionActivity.unpaidPrescriptionlist.get(list_position).getJSONArray("cart");

                                invoice = MyPrescriptionActivity.unpaidPrescriptionlist.get(list_position).getString("invoice");
                                invoice_id = MyPrescriptionActivity.unpaidPrescriptionlist.get(list_position).getInt("invoice_id");
                                sub_total = MyPrescriptionActivity.unpaidPrescriptionlist.get(list_position).getInt("sub_total");
                                discount = MyPrescriptionActivity.unpaidPrescriptionlist.get(list_position).getInt("discount");
                                tax = MyPrescriptionActivity.unpaidPrescriptionlist.get(list_position).getInt("tax");
                                shipping = MyPrescriptionActivity.unpaidPrescriptionlist.get(list_position).getInt("shipping");
                                total = MyPrescriptionActivity.unpaidPrescriptionlist.get(list_position).getInt("total");

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

    }//volley

    private void setButtonWithstatus(String pres_status, String invoice_status) {

        int status_id;
        int pres_id;
        if (invoice_status.equals("Paid")) status_id = 2;
        else status_id = 1;
        if (pres_status.equals("Verified")) pres_id = 2;
        else pres_id = 1;

        if (status_id == 1 && pres_id == 1) {
            buttonPayNow.setText("Unverified");
            buttonPayNow.setEnabled(false);
        } else if (status_id == 1 && pres_id == 2) {
            buttonPayNow.setText("Pay Now");
            buttonPayNow.setEnabled(true);
        } else if (status_id == 2 && pres_id == 2) {
            buttonPayNow.setText("Paid");
            buttonPayNow.setEnabled(false);
        }


    }


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }


    //To load detials of My Prescription of a user


    public synchronized void getPrescriptionDetailsByInvoiceId(final int invoice_id, final int position) {

        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        StringRequest doLogin = new StringRequest(Request.Method.POST, AppController.GET_PRESCRIPTION_THUMB_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        try {

                            JSONObject mainResponse = new JSONObject(response);
                            prescriptionStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");
                            if (prescriptionStatus.equals("SUCCESS")) {
                                JSONObject jsonObjectData = mainResponse.getJSONObject("data");
                                JSONArray jsonArrayPrescriptions = jsonObjectData.getJSONArray("prescriptions");
                                String created_on = HomeActivity.unpaidPrescription.get(list_position).getString("created_on");
                                String pres_status = HomeActivity.unpaidPrescription.get(list_position).getString("pres_status");
                                String path = HomeActivity.unpaidPrescription.get(list_position).getString("path");
                                int invoice_id = HomeActivity.unpaidPrescription.get(list_position).getInt("invoice_id");
                                int invoice_status_id = HomeActivity.unpaidPrescription.get(list_position).getInt("invoice_status_id");
                                int pres_status_id = HomeActivity.unpaidPrescription.get(list_position).getInt("pres_status_id");


                                Log.d("BLACKBOARD", "VALUE");
                                Log.d("MATCH_IDS", String.valueOf(created_on));
                                Log.d("MATCH_IDS", String.valueOf(pres_status));
                                Log.d("MATCH_IDS", String.valueOf(path));
                                Log.d("MATCH_IDS", String.valueOf(invoice_id));


                                //   textViewPrescriptionDetails.setText(created_on);
                                Log.d("LOOP", "FOO");


                                if (SharedPreferenceHandler.UNVERIFIED == pres_status_id) {

                                    //  buttonPayNow.setVisibility(View.INVISIBLE);

                                } else if (SharedPreferenceHandler.VERIFIED == pres_status_id && SharedPreferenceHandler.PENDING == invoice_status_id) {


                                } else if (SharedPreferenceHandler.VERIFIED == pres_status_id && SharedPreferenceHandler.PAID == invoice_status_id) {


                                }


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


    }//volley


    @Override
    public void onBackPressed() {
        PrescriptionDetailsActivity.this.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        super.onBackPressed();
    }
}
