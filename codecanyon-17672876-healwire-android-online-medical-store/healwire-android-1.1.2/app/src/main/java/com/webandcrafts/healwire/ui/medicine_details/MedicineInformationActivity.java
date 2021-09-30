package com.webandcrafts.healwire.ui.medicine_details;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.webandcrafts.healwire.AlternativesActivity;
import com.webandcrafts.healwire.AppController;
import com.webandcrafts.healwire.NewBaseActivity;
import com.webandcrafts.healwire.R;
import com.webandcrafts.healwire.models.MyCartItems;
import com.webandcrafts.healwire.ui.login.LoginActivity;
import com.webandcrafts.healwire.ui.my_cart.MyCartActivity;
import com.webandcrafts.healwire.utils.PreferenceManager;
import com.webandcrafts.healwire.utils.SharedPreferenceHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MedicineInformationActivity extends NewBaseActivity {

    public String item_name, item_code, composition, discount_type, tax_type, manufacturer, group;
    public int id, mrp, discount, tax, is_delete, is_pres_required;
    public int quantityValue;
    RelativeLayout relativeLayoutSubstitute, relativeLayoutAddToCart;
    RelativeLayout relativeLayoutDownQuantity, relativeLayoutUpQuantity;
    EditText editTextQuantity;
    int current_value = 0;
    String quantity;
    String flag;
    int current_quantity, added_quantity, new_quantity;
    ImageView mBackicon;


    String substitutemedicineStatus, responseMessage;


    TextView textViewMedicineName, textViewMedicinePrice, textViewCompositionDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_information);
        textViewMedicineName = (TextView) findViewById(R.id.textViewGroupMedicineName);
        textViewMedicinePrice = (TextView) findViewById(R.id.textViewMedicinePrice);
        textViewCompositionDetails = (TextView) findViewById(R.id.textViewCompositionDetails);
        relativeLayoutSubstitute = (RelativeLayout) findViewById(R.id.relativeLayoutSubstitute);
        relativeLayoutAddToCart = (RelativeLayout) findViewById(R.id.relativeLayoutAddToCart);
        relativeLayoutDownQuantity = (RelativeLayout) findViewById(R.id.relativeLayoutDownQuantity);
        relativeLayoutUpQuantity = (RelativeLayout) findViewById(R.id.relativeLayoutUpQuantity);
        editTextQuantity = (EditText) findViewById(R.id.editTextQuantity);
        mBackicon = findViewById(R.id.iv_back);
        editTextQuantity.setText("1");
        Selection.setSelection(editTextQuantity.getText(), editTextQuantity.length());
        displayMedicineInformation();
        SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
        sharedPreferenceHandler.getCartArrayList(getApplicationContext());
        for (int j = 0; j < SharedPreferenceHandler.CART_LENGTH; j++) {
            MyCartItems myCarts = (MyCartItems) SharedPreferenceHandler.cartList.get(j);
            SharedPreferenceHandler.arrayListMedicineName.add(myCarts.getMedName());
        }

        relativeLayoutSubstitute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentAlternativesMedicine = new Intent(getApplicationContext(), AlternativesActivity.class);
                intentAlternativesMedicine.putExtra("item_name", item_name);
                intentAlternativesMedicine.putExtra("item_code", item_code);
                intentAlternativesMedicine.putExtra("id", id);
                intentAlternativesMedicine.putExtra("price", textViewMedicinePrice.getText().toString());
                startActivity(intentAlternativesMedicine);
                overridePendingTransition(R.anim.right, R.anim.left);

            }
        });


        relativeLayoutAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                quantity = editTextQuantity.getText().toString();

                if (AppController.userStatus.equals("logout") && (Integer.parseInt(quantity) >= 1 && Integer.parseInt(quantity) < 500)) {

                    Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intentLogin);
                    overridePendingTransition(R.anim.right, R.anim.left);

                } else  if(AppController.userStatus.equals("login") && (Integer.parseInt(quantity) >= 1 && Integer.parseInt(quantity) < 500)){

                    MyCartItems items = new MyCartItems(id, item_name, quantity, is_pres_required);
                    SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
                    sharedPreferenceHandler.checkItemExists(getApplicationContext(), items);

                    MyCartItems it;

                    if (SharedPreferenceHandler.CART_LENGTH == 0) {


                        //  Toast.makeText(getApplicationContext(),"First item",Toast.LENGTH_SHORT).show();

                        //Cart is empty

                        sharedPreferenceHandler.addCartItems(getApplicationContext(), items);

                        Intent intentMyCart = new Intent(getApplicationContext(), MyCartActivity.class);
                        intentMyCart.putExtra("item_name", item_name);
                        intentMyCart.putExtra("item_code", item_code);
                        intentMyCart.putExtra("quantity", quantity);
                        intentMyCart.putExtra("is_pres_required", is_pres_required);
                        intentMyCart.putExtra("id", id);
                        startActivity(intentMyCart);
                        overridePendingTransition(R.anim.right, R.anim.left);
                        MedicineInformationActivity.this.finish();


                    } else {

                        // Cart is not empty. CART_LENGTH > 0

                        for (int i = 0; i < SharedPreferenceHandler.CART_LENGTH; i++) {

                            //  it = (MyCartItems) SharedPreferenceHandler.cartList.get(i);

                            Log.d("CART LENGTH", String.valueOf(SharedPreferenceHandler.CART_LENGTH));


//                        Toast.makeText(getApplicationContext(),it.getQty()+"",Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getApplicationContext(),it.getMedName()+"",Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getApplicationContext(),it.getFlag()+"",Toast.LENGTH_SHORT).show();


                            if (SharedPreferenceHandler.arrayListMedicineName.contains(items.getMedName())) {

                                // Already exist


                                Toast.makeText(getApplicationContext(), "Item Already added", Toast.LENGTH_SHORT).show();

                                AppController.ITEM_IN_CART = true;


                                Intent intentMyCart = new Intent(getApplicationContext(), MyCartActivity.class);
                                intentMyCart.putExtra("item_name", item_name);
                                intentMyCart.putExtra("item_code", item_code);
                                intentMyCart.putExtra("quantity", quantity);
                                intentMyCart.putExtra("is_pres_required", is_pres_required);
                                intentMyCart.putExtra("id", id);


                                startActivity(intentMyCart);
                                MedicineInformationActivity.this.finish();



                                break;


                            } else {


                                sharedPreferenceHandler.addCartItems(getApplicationContext(), items);

                                Intent intentMyCart = new Intent(getApplicationContext(), MyCartActivity.class);
                                intentMyCart.putExtra("item_name", item_name);
                                intentMyCart.putExtra("item_code", item_code);
                                intentMyCart.putExtra("quantity", quantity);
                                intentMyCart.putExtra("is_pres_required", is_pres_required);
                                intentMyCart.putExtra("id", id);

                                startActivity(intentMyCart);
                                MedicineInformationActivity.this.finish();

                                break;


                            }


                        }

                        Log.d("EXIT", "Exit from Loop");


                    } // CART_LENGTH > 0


                } else {
                    Toast.makeText(MedicineInformationActivity.this, "Please choose medicine between 0 and 500", Toast.LENGTH_SHORT).show();
                }//else of login status


            } //onClick close
        });

        relativeLayoutDownQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                quantity = editTextQuantity.getText().toString();
                current_value = Integer.parseInt(quantity);

                if (current_value > 1) {

                    current_value = current_value - 1;

                    editTextQuantity.setText("" + current_value);
                    Selection.setSelection(editTextQuantity.getText(), editTextQuantity.length());


                } else {

                    editTextQuantity.setText("" + current_value);
                    Selection.setSelection(editTextQuantity.getText(), editTextQuantity.length());

                }


            }
        });


        relativeLayoutUpQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                quantity = editTextQuantity.getText().toString();
                current_value = Integer.parseInt(quantity);

                if (current_value < 500) {

                    current_value = current_value + 1;
                    editTextQuantity.setText("" + current_value);
                    Selection.setSelection(editTextQuantity.getText(), editTextQuantity.length());


                }


            }
        });
        mBackicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MedicineInformationActivity.this.finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });

    }


    public void setAddToCartPreferences() {


        SharedPreferences prefs = this.getSharedPreferences(PreferenceManager.MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();


        //Set also the number of cart items list

        for (int i = 0; i < AppController.cartLength; i++) {

            edit.putString("cartItemsList" + i, MyCartActivity.cartItemsList.get(i) + "");

        }

        edit.putString("cartSize", AppController.cartLength + "");
        edit.commit();


    }

    public void getCartListPreferences() {


        SharedPreferences prefs = this.getSharedPreferences(PreferenceManager.MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        ArrayList<String> myAList = new ArrayList<String>();
        int size = prefs.getInt("cartSize", 0);

        for (int j = 0; j < size; j++) {
            //myAList.add(prefs.getString("cartItemsList"+j));
        }


    }


    public void displayMedicineInformation() {

        item_name = getIntent().getStringExtra("item_name");
        item_code = getIntent().getStringExtra("item_code");
        composition = getIntent().getStringExtra("composition");
        mrp = getIntent().getExtras().getInt("mrp");
        id = getIntent().getIntExtra("id", 0);
        is_pres_required = getIntent().getIntExtra("is_pres_required", 0);


        textViewMedicineName.setText(item_name);
        textViewMedicinePrice.setText(String.format(getString(R.string.Rs)) + " " + mrp);
        textViewCompositionDetails.setText(composition);


    }


    //


    public void substituteMedicine(final String name, final String id) {

        StringRequest substituteMedicineRequest = new StringRequest(Request.Method.GET, AppController.LOAD_SUBSTITUTE_MEDICINE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            JSONObject mainResponse = new JSONObject(response);
                            substitutemedicineStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");


                            if (substitutemedicineStatus.equals("SUCCESS")) {

                                JSONArray jsonArrayData = mainResponse.getJSONArray("data");


                                for (int i = 0; i < jsonArrayData.length(); i++) {

                                    JSONObject jsonObjectData = jsonArrayData.getJSONObject(i);

                                    HashMap<String, String> hm = new HashMap<String, String>();
                                    hm.put("txt", jsonObjectData.getString("item_name"));
//                                hm.put("mrp",  jsonObjectData.getString("mrp"));
//                                hm.put("discount", jsonObjectData.getString("discount"));

                                    String itemName = jsonObjectData.getString("item_name");


                                }


                            }

                        } catch (Exception e) {
                            // messageHandler.sendEmptyMessage(99);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //messageHandler.sendEmptyMessage(98);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("n", name);
                params.put("id", id);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(substituteMedicineRequest);

        // AppController.getInstance().cancelPendingRequests(TAG_LOAD_MEDICINE_REQUEST);


        //  Toast.makeText(getApplicationContext(),"Retrieved data",Toast.LENGTH_SHORT).show();


        substituteMedicineRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20), 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }//volley

    @Override
    public void onBackPressed() {
        MedicineInformationActivity.this.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        super.onBackPressed();
    }
}
