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
import com.webandcrafts.healwire.models.Orders;
import com.webandcrafts.healwire.R;

import java.util.List;

/**
 * Created by vineeth on 8/18/2016.
 */
public class MyOrderListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Orders> orderList;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public MyOrderListAdapter(Activity activity, List<Orders> orderList) {
        this.activity = activity;
        this.orderList = orderList;
    }


    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int i) {
        return orderList.get(i);
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
            view = inflater.inflate(R.layout.order_item, viewGroup,false);


        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView imageViewPrescription = (NetworkImageView) view
                .findViewById(R.id.imageViewPrescription);
        // imageViewPrescription.setDefaultImageResId(R.drawable.no_pres_square);

        TextView textViewDate = (TextView) view.findViewById(R.id.textViewDate);
        TextView textViewVerification = (TextView) view.findViewById(R.id.textViewVerification);
        TextView textViewInvoice = (TextView) view.findViewById(R.id.textViewInvoice);



        // getting movie data for the row
        Orders orders = orderList.get(i);

        // thumbnail image
        imageViewPrescription.setImageUrl(orders.getImageUrl(), imageLoader);


        // Date Added
        textViewDate.setText("Date added : " + orders.getDate());

        // Verification status

        textViewVerification.setText(orders.getStatus());


        // Invoice of Order

        textViewInvoice.setText("Invoice Id : " + orders.getInvoice());

        return view;


    }
}
