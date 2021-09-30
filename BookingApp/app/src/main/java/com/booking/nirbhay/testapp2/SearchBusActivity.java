package com.booking.nirbhay.testapp2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchBusActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    AppController obj;
    ArrayList results;

    ArrayList<String> vIDList=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myoderhitorylist);
        obj = (AppController) getApplicationContext();
        TextView headername=(TextView)findViewById(R.id.headername);
        headername.setText("Bus List");
        mRecyclerView = (RecyclerView)findViewById(R.id.orderhtry);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(SearchBusActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        new MyOderList().execute();
    }
    class MyOderList extends AsyncTask<String,String,String>
    {

        String req;
        ProgressDialog pd;
        String _sid;

        @Override
        protected void onPreExecute() {
            pd=new ProgressDialog(SearchBusActivity.this);
            pd.setMessage("Please wait....");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String cateUrl= AppController.baseURL+AppController.getBusDeatils;
            try {
                req= CustomHttpClient.urlincoding(cateUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return req;
        }

        @Override
        protected void onPostExecute(String s) {

            try
            {
                pd.dismiss();

                JSONObject jObjetc = new JSONObject(s);
                JSONObject jsonObjectSub = jObjetc.getJSONObject("response");
                String flg = jsonObjectSub.getString("status");

                results = new ArrayList<BusModel>();
                //	String flag=jObjetc.getString("status");
                if (flg.equals("1")) {
                    JSONArray jArrObject = jsonObjectSub.getJSONArray("data");
                    for (int i = 0; i < jArrObject.length(); i++) {
                        // item2 = new RowItems_Banner();
                        JSONObject jIndexObject = jArrObject.getJSONObject(i);

                        vIDList.add(jIndexObject.getString("bus_id"));
                        BusModel obj = new BusModel(
                                jIndexObject.getString("bus_id"),
                                jIndexObject.getString("bus_name"),
                                jIndexObject.getString("bus_number"),
                                jIndexObject.getString("totalSeats"),
                                jIndexObject.getString("route_id"),
                                jIndexObject.getString("bus_source"),
                                jIndexObject.getString("bus_dest"),
                                jIndexObject.getString("bus_pickup"),
                                jIndexObject.getString("source_time"),
                                jIndexObject.getString("des_time"));
                        results.add(i, obj);


                    }
                    mAdapter = new BusListRowAdapter(SearchBusActivity.this,results,getFragmentManager());
                    mRecyclerView.setAdapter(mAdapter);

                    ((BusListRowAdapter) mAdapter).setOnItemClickListener(new BusListRowAdapter
                            .MyClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Log.i("tags Greens", " Clicked on Item " + position);
//                            String vID=vIDList.get(position);
//                            gObject.setvID(vID);
//                           // gObject.setVendorName("Vendor List");
//                            Fragment vendorFrgObject=new ProdcutsFragment();
//                            oprnList(vendorFrgObject);
//                            Toast.makeText(getActivity(),"click----"+position,Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
