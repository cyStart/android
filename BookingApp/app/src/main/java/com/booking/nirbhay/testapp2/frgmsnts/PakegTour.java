package com.booking.nirbhay.testapp2.frgmsnts;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.booking.nirbhay.testapp2.AppController;
import com.booking.nirbhay.testapp2.BusListRowAdapter;
import com.booking.nirbhay.testapp2.BusModel;
import com.booking.nirbhay.testapp2.CustomHttpClient;
import com.booking.nirbhay.testapp2.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rakesh on 12/16/2016.
 */

public class PakegTour extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    AppController obj;
    ArrayList results;

    ArrayList<String> vIDList=new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myoderhitorylist, null);
        obj = (AppController)getActivity().getApplicationContext();
        TextView headername=(TextView)view.findViewById(R.id.headername);
        headername.setText("Tour  Packages");
        mRecyclerView = (RecyclerView)view.findViewById(R.id.orderhtry);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        new MyOderList().execute();

        return view;
    }

    class MyOderList extends AsyncTask<String,String,String>
    {

        String req;
        ProgressDialog pd;
        String _sid;

        @Override
        protected void onPreExecute() {
            pd=new ProgressDialog(getActivity());
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
//                    mAdapter = new BusListRowAdapter(results);
//                    mRecyclerView.setAdapter(mAdapter);

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
