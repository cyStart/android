package com.webandcrafts.healwire.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.webandcrafts.healwire.AppController;
import com.webandcrafts.healwire.R;
import com.webandcrafts.healwire.models.SubstituteMedicine;

import java.util.List;

/**
 * Created by vineeth on 8/23/2016.
 */
public class SubstituteMedicineAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<SubstituteMedicine> substituteMedicineList;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public SubstituteMedicineAdapter(Activity activity, List<SubstituteMedicine> substituteMedicineList) {
        this.activity = activity;
        this.substituteMedicineList = substituteMedicineList;
    }



    @Override
    public int getCount() {
        return substituteMedicineList.size();
    }

    @Override
    public Object getItem(int i) {
        return substituteMedicineList.get(i);
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
            view = inflater.inflate(R.layout.list_alternatives_items, viewGroup,false);


//        if (imageLoader == null)
//            imageLoader = AppController.getInstance().getImageLoader();
//        NetworkImageView imageViewPrescription = (NetworkImageView) view
//                .findViewById(R.id.imageViewPrescription);
        // imageViewPrescription.setDefaultImageResId(R.drawable.no_pres_square);

        TextView textViewMedicineName = (TextView) view.findViewById(R.id.textViewMedicineName);
        TextView textViewMedicinePrice = (TextView) view.findViewById(R.id.textViewMedicinePrice);




        // getting movie data for the row
        SubstituteMedicine substituteMedicine = substituteMedicineList.get(i);



//        // thumbnail image
//        imageViewPrescription.setImageUrl(substituteMedicine.getImageUrl(), imageLoader);


        // Date Added
        textViewMedicineName.setText(substituteMedicine.getItem_name());

        // Verification status

        textViewMedicinePrice.setText(""+substituteMedicine.getItem_price());







        return view;


    }
}
