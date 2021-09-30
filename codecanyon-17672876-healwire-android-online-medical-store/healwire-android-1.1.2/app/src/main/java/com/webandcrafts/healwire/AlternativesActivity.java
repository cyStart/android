package com.webandcrafts.healwire;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.webandcrafts.healwire.adapter.SubstituteMedicineAdapter;
import com.webandcrafts.healwire.models.SubstituteMedicine;
import com.webandcrafts.healwire.utils.SharedPreferenceHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlternativesActivity extends NewBaseActivity {

    RelativeLayout relativeLayoutMain;
    ListView listViewAlternatives;

    private List<SubstituteMedicine> substituteMedicineList = new ArrayList<SubstituteMedicine>();

    String substitutemedicineStatus,responseMessage;
    int price;
    String item_price;

    SubstituteMedicineAdapter substituteMedicineAdapter;

    int mId;

    String medicineName,medicineId;

    TextView textViewSubstitute,textViewAlternatives;

    TextView textViewEmpty;


    ArrayList<HashMap<String,String>> alternativesMedicineList;
    private SimpleAdapter listAdapter ;

     ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alternatives);

        alternativesMedicineList = new ArrayList<HashMap<String,String>>();
        progressDialog = new ProgressDialog(this);

        textViewEmpty = (TextView) findViewById(android.R.id.empty);

        textViewSubstitute = (TextView) findViewById(R.id.textViewSubstitute);
        textViewAlternatives = (TextView) findViewById(R.id.textViewAlternatives);


        listViewAlternatives = (ListView) findViewById(R.id.listViewAlternatives);

//           medicineName = "9-PM EYE DROPS";
//           medicineId = "7";



        medicineName = getIntent().getStringExtra("item_name");
        medicineId = getIntent().getStringExtra("item_code");
        item_price = getIntent().getStringExtra("price");
        mId =  getIntent().getIntExtra("id",0);

//
//        Toast.makeText(getApplicationContext(),""+medicineName,Toast.LENGTH_LONG).show();
//       Toast.makeText(getApplicationContext(),""+medicineId,Toast.LENGTH_LONG).show();


        textViewSubstitute.setText(medicineName);
        textViewAlternatives.setText(" (Alternatives) "+item_price);

       // doPostRequest(medicineName,medicineId);

        listViewAlternatives = (ListView) findViewById(R.id.listViewAlternatives);
        substituteMedicineAdapter = new SubstituteMedicineAdapter(this, substituteMedicineList);
        listViewAlternatives.setAdapter(substituteMedicineAdapter);
//

         loadSubstituteMedicines(medicineName,mId);

     //  loadAlternativeMedicines(medicineName,medicineId);



//        //sub = (TextView)v.findViewById(R.id.tv_sububstitute);
//        if(alternativesMedicineList!=null){
//            //listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
//            //alterlist.setAdapter( listAdapter );
//
//            String[] from = {"item_name","mrp"};
//            int[] to = {R.id.textViewMedicineName,R.id.textViewMedicinePrice};
//            listAdapter = new SimpleAdapter(getApplicationContext(), alternativesMedicineList, R.layout.list_alternatives_items, from, to);
//            listViewAlternatives.setAdapter(listAdapter);
//
//        }




    }





    //To load detials of Alternative medicine information


    public synchronized void loadAlternativeMedicines(final String alter_name, final String id){

        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();



        StringRequest doLogin = new StringRequest(Request.Method.POST,AppController.LOAD_SUBSTITUTE_MEDICINE_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


//                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                        progressDialog.dismiss();

                        int i;

                        try{


                            JSONObject mainResponse = new JSONObject(response);
                            substitutemedicineStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");

                            if(substitutemedicineStatus.equals("SUCCESS")){

                            JSONObject jsonObjectData = mainResponse.getJSONObject("data");
                            price = jsonObjectData.getInt("price");

                            JSONArray jsonArrayMedicines = jsonObjectData.getJSONArray("medicines");


                               // Double save=0d;
                                for(i = 0;i<jsonArrayMedicines.length();i++){
                                    HashMap<String, String> hm = new HashMap<String,String>();
                                    hm.put("item_name", jsonArrayMedicines.getJSONObject(i).getString("item_name"));
                                    hm.put("mrp", jsonArrayMedicines.getJSONObject(i).getString("mrp"));

                                    Toast.makeText(getApplicationContext(),"GOT:"+jsonArrayMedicines.getJSONObject(i).getString("item_name"),Toast.LENGTH_LONG).show();

//                                    hm.put("item_code", jsonArrayMedicines.getJSONObject(i).getString("item_code"));
//                                    hm.put("compos", jsonArrayMedicines.getJSONObject(i).getString("composition"));
                                    //hm.put("savings", msgArray.getJSONObject(i).getString("mrp"));
                                    //save=Double.parseDouble(local.getString("price"))-Double.parseDouble( jsonArrayMedicines.getJSONObject(i).getString("mrp"));
                                  //  hm.put("savings", ""+save);
                                    alternativesMedicineList.add(hm);

                                }
                                listAdapter.notifyDataSetChanged();
                            }
                        }catch(Exception e){
                            // messageHandler.sendEmptyMessage(99);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               //  messageHandler.sendEmptyMessage(98);
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("id",id);
                params.put("n", alter_name);


                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map headers = new HashMap();
                SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();

                if(!sharedPreferenceHandler.getCookie(getApplicationContext()).equals(""))
                    headers.put("Cookie", sharedPreferenceHandler.getCookie(getApplicationContext()));

                Log.d("HEADER_LOG",headers.toString());

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


//----------------------------------------new ..................................


    public synchronized void loadSubstituteMedicines(final String alter_name, final int id){

        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();



        StringRequest loadMedicineRequest = new StringRequest(Request.Method.POST,AppController.LOAD_SUBSTITUTE_MEDICINE_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


//                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                        progressDialog.dismiss();


                        try{


                            JSONObject mainResponse = new JSONObject(response);
                            substitutemedicineStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");

                            if(substitutemedicineStatus.equals("SUCCESS")){

                                JSONObject jsonObjectData = mainResponse.getJSONObject("data");
                                price = jsonObjectData.getInt("price");

                                JSONArray jsonArrayMedicines = jsonObjectData.getJSONArray("medicines");



                                //-----------------

                                for(int i = 0; i<jsonArrayMedicines.length();i++){

                                    SubstituteMedicine substituteMedicine = new SubstituteMedicine();

                                    String item_name = jsonArrayMedicines.getJSONObject(i).getString("item_name");
                                    String mrp = jsonArrayMedicines.getJSONObject(i).getString("mrp");
                                    int id = jsonArrayMedicines.getJSONObject(i).getInt("id");


                                    substituteMedicine.setItem_name(item_name);
                                    substituteMedicine.setItem_price(mrp);


                                    substituteMedicineList.add(substituteMedicine);

                                }

                                //-------------------

                                //Toast.makeText(getApplicationContext(),"Count = "+substituteMedicineList.size(),Toast.LENGTH_SHORT).show();


                                substituteMedicineAdapter.notifyDataSetChanged();
                            }
                        }catch(Exception e){
                            // messageHandler.sendEmptyMessage(99);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  messageHandler.sendEmptyMessage(98);

                Log.d("EMPTY","NO DATA FROM SERVER");

                progressDialog.dismiss();
                textViewEmpty.setVisibility(View.VISIBLE);



            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("id",String.valueOf(id));
                params.put("n", alter_name);


                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map headers = new HashMap();
                SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();

                if(!sharedPreferenceHandler.getCookie(getApplicationContext()).equals(""))
                    headers.put("Cookie", sharedPreferenceHandler.getCookie(getApplicationContext()));

                Log.d("HEADER_LOG",headers.toString());

                // return super.getHeaders();
                return headers;

            }



        };
        AppController.getInstance().addToRequestQueue(loadMedicineRequest);
//        doLogin.setRetryPolicy(new DefaultRetryPolicy(
//                (int) TimeUnit.SECONDS.toMillis(20),
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }//volley











}
