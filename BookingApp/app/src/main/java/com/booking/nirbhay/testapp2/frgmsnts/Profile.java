package com.booking.nirbhay.testapp2.frgmsnts;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class Profile extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    AppController obj;
    ArrayList results;
    ImageView drawericon,ok;
    ArrayList<String> vIDList=new ArrayList<String>();
    TextView nametxt,addresstxt,mobiletext;
    SharedPreferences mpreferences;
    SharedPreferences.Editor settingDataPrefe;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, null);
        obj = (AppController)getActivity().getApplicationContext();
        mpreferences = getActivity().getSharedPreferences(String.format("%s_preferences", getActivity().getPackageName()), Context.MODE_PRIVATE);
        settingDataPrefe = mpreferences.edit();
        TextView headername=(TextView)getActivity().findViewById(R.id.headername);
        headername.setText("Profile");

        mobiletext=(TextView)view.findViewById(R.id.mobiletext);
        addresstxt=(TextView)view.findViewById(R.id.addresstxt);
        nametxt=(TextView)view.findViewById(R.id.nametxt);
        mobiletext.setText(mpreferences.getString("mobile",""));
        addresstxt.setText(mpreferences.getString("aadress",""));
        nametxt.setText(mpreferences.getString("name",""));


        drawericon=(ImageView)view.findViewById(R.id.drawericon);
        ok=(ImageView)view.findViewById(R.id.ok);
        drawericon.setVisibility(View.VISIBLE);
        ok.setVisibility(View.VISIBLE);
        drawericon.setImageResource(R.drawable.multiply);
        ok.setImageResource(R.drawable.checked);
        drawericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        return view;
    }




}
