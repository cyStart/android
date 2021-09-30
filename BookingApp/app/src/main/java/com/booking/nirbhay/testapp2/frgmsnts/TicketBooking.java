package com.booking.nirbhay.testapp2.frgmsnts;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.booking.nirbhay.testapp2.AppController;
import com.booking.nirbhay.testapp2.BusListRowAdapter;
import com.booking.nirbhay.testapp2.BusModel;
import com.booking.nirbhay.testapp2.CustomHttpClient;
import com.booking.nirbhay.testapp2.R;
import com.booking.nirbhay.testapp2.SearchBusActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Rakesh on 12/16/2016.
 */

public class TicketBooking extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    AppController obj;
    ArrayList results;
    ImageView drawericon,ok;
    ArrayList<String> vIDList=new ArrayList<String>();
    EditText panme,pmobile,pfrom,pto,pjouryDate;
    TextView bookingbtn;
    int mYear,mMonth,mDay;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ticket_booking, null);
        obj = (AppController)getActivity().getApplicationContext();
        TextView headername=(TextView)getActivity().findViewById(R.id.headername);
        headername.setText("Ticket booking");

        pjouryDate=(EditText)view.findViewById(R.id.pjouryDate);
        pto=(EditText)view.findViewById(R.id.pto);
        pfrom=(EditText)view.findViewById(R.id.pfrom);
        pmobile=(EditText)view.findViewById(R.id.pmobile);
        panme=(EditText)view.findViewById(R.id.panme);
        bookingbtn=(TextView)view.findViewById(R.id.bookingbtn);
        drawericon=(ImageView)view.findViewById(R.id.drawericon);
        ok=(ImageView)view.findViewById(R.id.ok);
        drawericon.setVisibility(View.VISIBLE);
        ok.setVisibility(View.VISIBLE);
        pjouryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                Calendar mcurrentDate=Calendar.getInstance();
                mYear=mcurrentDate.get(Calendar.YEAR);
                mMonth=mcurrentDate.get(Calendar.MONTH);
                mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                        pjouryDate.setText(selectedyear+"-"+selectedmonth+"-"+selectedday);
                    }
                },mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();


            }
        });
        drawericon.setImageResource(R.drawable.multiply);
        ok.setImageResource(R.drawable.checked);
        drawericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        bookingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            new Booking().execute();
            }
        });

        return view;
    }

    class Booking extends AsyncTask<String,String,String>
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
//b_uid=1&b_passanger_name=nirbhay&b_mob=8285672453&b_from=lko&b_to=gkp&b_date_of_jry=1-10-18&b_fare=200&b_dist=300&pass=1234&b_bus_id=1&b_status=1&b_date=1-10-18
            String cateUrl= AppController.baseURL+AppController.bookingTicket+"" +
                    "b_uid="+obj.getUID()+"" +
                    "&b_passanger_name="+panme.getText().toString()+"" +
                    "&b_mob="+pmobile.getText().toString()+"" +
                    "&b_from="+pfrom.getText().toString()+"" +
                    "&b_to="+pto.getText().toString()+"" +
                    "&b_date_of_jry="+pjouryDate.getText().toString()+"" +


                    "&b_bus_id="+obj.getBusId()+"" +
                    "&b_status=1";
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
                if(flg.equals("1"))
                {
                    Toast.makeText(getActivity(),""+jsonObjectSub.getString("message"),Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getActivity(), SearchBusActivity.class));
                    getActivity().finish();
                }
                else {
                    Toast.makeText(getActivity(),""+jsonObjectSub.getString("message"),Toast.LENGTH_LONG).show();

                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


        }
    }




}
