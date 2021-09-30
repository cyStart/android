package com.webandcrafts.healwire.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.webandcrafts.healwire.AppController;
import com.webandcrafts.healwire.models.Prescription;
import com.webandcrafts.healwire.R;

import java.util.List;

/**
 * Created by vineeth on 8/12/2016.
 */
public class MyPrescriptionListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Prescription> prescriptionList;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public MyPrescriptionListAdapter(Activity activity, List<Prescription> prescriptionList) {
        this.activity = activity;
        this.prescriptionList = prescriptionList;
    }

    @Override
    public int getCount() {
        return prescriptionList.size();
    }

    @Override
    public Object getItem(int i) {
        return prescriptionList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null)
            view = inflater.inflate(R.layout.prescription_item, viewGroup,false);


        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView imageViewPrescription = (NetworkImageView) view
                .findViewById(R.id.imageViewPrescription);
       // imageViewPrescription.setDefaultImageResId(R.drawable.no_pres_square);

        TextView textViewDate = (TextView) view.findViewById(R.id.textViewDate);
        TextView textViewVerification = (TextView) view.findViewById(R.id.textViewVerification);




        // getting movie data for the row
        Prescription prescription = prescriptionList.get(i);



            // thumbnail image
            imageViewPrescription.setImageUrl(prescription.getImageUrl(), imageLoader);


            // Date Added
            textViewDate.setText("Date added : " + prescription.getDate());

            // Verification status

            textViewVerification.setText(prescription.getStatus()+" , "+prescription.getInvoiceStatus());







        return view;




    }
}
