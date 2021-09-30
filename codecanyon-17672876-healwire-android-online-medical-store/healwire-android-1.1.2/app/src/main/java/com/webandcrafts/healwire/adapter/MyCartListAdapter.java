package com.webandcrafts.healwire.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webandcrafts.healwire.AppController;
import com.webandcrafts.healwire.models.MyCartItems;
import com.webandcrafts.healwire.R;
import com.webandcrafts.healwire.utils.SharedPreferenceHandler;

import java.util.List;

/**
 * Created by vineeth on 8/4/2016.
 */
public class MyCartListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private Context context_new;
    public static List<MyCartItems> cartItems;
    private ImageView iv;
    static View tmpView;

    public MyCartListAdapter(Activity activity, List<MyCartItems> cartItems) {
        this.activity = activity;
        this.cartItems = cartItems;
    }



    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public Object getItem(int i) {
        return cartItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.cart_item, parent,false);

        AtomPaymentHolder holder = null;


        holder = new AtomPaymentHolder();

        holder.m = cartItems.get(position);

        holder.relativeLayoutUpQuantity = (RelativeLayout)convertView.findViewById(R.id.relativeLayoutUpQuantity);
        holder.relativeLayoutUpQuantity.setTag(holder);
        holder.relativeLayoutDownQuantity = (RelativeLayout)convertView.findViewById(R.id.relativeLayoutDownQuantity);
        holder.relativeLayoutDownQuantity.setTag(holder);

        holder.textViewMedicineName = (TextView)convertView.findViewById(R.id.textViewGroupMedicineName);
        holder.editTextQuantity = (EditText)convertView.findViewById(R.id.editTextQuantity);
        holder.relativeLayoutItem = (RelativeLayout)convertView.findViewById(R.id.relativeLayoutItem);
        holder.relativeLayoutItem.setTag(holder);




        convertView.setTag(holder);

        setupItem(holder);

        holder.relativeLayoutUpQuantity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AtomPaymentHolder holder = (AtomPaymentHolder)v.getTag();
                holder.editTextQuantity.setText((Integer.parseInt(holder.editTextQuantity.getText().toString())+1)+"");


              Log.d("QUP",holder.editTextQuantity.getText().toString());
              Log.d("POS",String.valueOf(position));

                AppController.updated_ItemPosition = position;
                AppController.updated_MedicineName = holder.textViewMedicineName.getText().toString();
                AppController.updated_Quantity = holder.editTextQuantity.getText().toString();


                MyCartItems itemsUp = new MyCartItems(AppController.updated_MedicineName,AppController.updated_Quantity);
                SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
                sharedPreferenceHandler.addCartItemsAtPosition(activity,itemsUp,AppController.updated_ItemPosition);

                Log.d("UP STORED",String.valueOf(position));




            }
        });
        holder.relativeLayoutDownQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AtomPaymentHolder holder = (AtomPaymentHolder)v.getTag();
                if(Integer.parseInt(holder.editTextQuantity.getText().toString())>1) {
                    holder.editTextQuantity.setText((Integer.parseInt(holder.editTextQuantity.getText().toString()) - 1) + "");

                    Log.d("QDOWN",holder.editTextQuantity.getText().toString());
                    Log.d("POS",String.valueOf(position));


                    AppController.updated_ItemPosition = position;
                    AppController.updated_MedicineName = holder.textViewMedicineName.getText().toString();
                    AppController.updated_Quantity = holder.editTextQuantity.getText().toString();


                    MyCartItems itemsDown = new MyCartItems(AppController.updated_MedicineName,AppController.updated_Quantity);
                    SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
                    sharedPreferenceHandler.addCartItemsAtPosition(activity,itemsDown,AppController.updated_ItemPosition);

                    Log.d("DOWN STORED",String.valueOf(position));


                }


            }
        });




        return convertView;

    }




    private void setupItem(AtomPaymentHolder holder) {

        holder.textViewMedicineName.setText(holder.m.getMedName());
        holder.editTextQuantity.setText(String.valueOf(holder.m.getQty()));


    }

    public static class AtomPaymentHolder {

        MyCartItems m ;
        RelativeLayout relativeLayoutUpQuantity,relativeLayoutDownQuantity,relativeLayoutItem;
        TextView textViewMedicineName;
        EditText editTextQuantity;

    }


}
