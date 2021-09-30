package com.webandcrafts.healwire.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.webandcrafts.healwire.models.PrescriptionCart;
import com.webandcrafts.healwire.R;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vineeth on 8/16/2016.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    static TextView textViewQuantity,textViewTotal;


    private Context context;
   // private List<String> listDataHeader; // header titles
    // child data in format of header title, child title
    private List<PrescriptionCart> listDataHeader; // header titles

    private HashMap<String, List<String>> listDataChild;

    DecimalFormat decimalFormatPrice = new DecimalFormat("#0.00"); // Set your desired format here.

    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listDataChild) {
        this.context = context;
       // this.listDataHeader = listDataHeader;
        this.listDataChild = listDataChild;
    }

    public ExpandableListAdapter(Context context, List<PrescriptionCart> listDataHeader) {
        this.context = context;
        this.listDataHeader = listDataHeader;

    }

    @Override
    public int getGroupCount() {

        return this.listDataHeader.size();

    }

    @Override
    public int getChildrenCount(int i) {

      return 1;


    }

    @Override
    public Object getGroup(int i) {

        return this.listDataHeader.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {

        return this.listDataChild.get(this.listDataHeader.get(i))
                .get(i1);


    }

    @Override
    public long getGroupId(int i) {

        return i;

    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {


       // String headerTitle = (String) getGroup(i);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.medicine_list_group, null);
        }


        PrescriptionCart prescriptionCart = listDataHeader.get(i);


        TextView textViewGroupMedicineName = (TextView) view
                .findViewById(R.id.textViewGroupMedicineName);

        textViewGroupMedicineName.setText(prescriptionCart.getItem_name());

       textViewQuantity = (TextView) view.findViewById(R.id.textViewQuantity);
       textViewTotal = (TextView) view.findViewById(R.id.textViewTotal);

        textViewQuantity.setText(""+prescriptionCart.getQuantity());


        if(prescriptionCart.getCurr_position().equals("BEFORE"))
             textViewTotal.setText(prescriptionCart.getCurrency()+" "+ decimalFormatPrice.format(prescriptionCart.getTotal_price()));
         else
            textViewTotal.setText(decimalFormatPrice.format(prescriptionCart.getTotal_price())+" "+prescriptionCart.getCurrency());



        return view;


    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

       // final String childText = (String) getChild(i, i1);

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.prescription_details_list_item, null);
        }

        PrescriptionCart prescriptionCart = listDataHeader.get(i);


        TextView textViewUnitPriceValue = (TextView) view.findViewById(R.id.textViewUnitPriceValue);
        TextView textViewQuantityValue = (TextView) view.findViewById(R.id.textViewQuantityValue);
        TextView textViewSubTotalValue = (TextView) view.findViewById(R.id.textViewSubTotalValue);
        TextView textViewUnitDiscValue = (TextView) view.findViewById(R.id.textViewUnitDiscValue);
        TextView textViewDiscountValue = (TextView) view.findViewById(R.id.textViewDiscountValue);
        TextView textViewTotalPriceValue = (TextView) view.findViewById(R.id.textViewTotalPriceValue);





      //  textViewMedicine.setText(""+prescriptionCart.getItem_name());
        textViewQuantityValue.setText(""+prescriptionCart.getQuantity());

        if(prescriptionCart.getCurr_position().equals("BEFORE")){

            textViewUnitPriceValue.setText(prescriptionCart.getCurrency()+" "+decimalFormatPrice.format(prescriptionCart.getUnit_price()));
            textViewUnitDiscValue.setText(prescriptionCart.getCurrency()+" "+decimalFormatPrice.format(prescriptionCart.getDiscount_percent()));
            textViewTotalPriceValue.setText(prescriptionCart.getCurrency()+" "+decimalFormatPrice.format(prescriptionCart.getTotal_price()));
            int calc_sub_total = prescriptionCart.getUnit_price() * prescriptionCart.getQuantity();
            textViewSubTotalValue.setText(decimalFormatPrice.format(calc_sub_total)+" "+prescriptionCart.getCurrency());
            int cal_discount = prescriptionCart.getDiscount_percent() * prescriptionCart.getQuantity();
            textViewDiscountValue.setText(prescriptionCart.getCurrency()+" "+decimalFormatPrice.format(cal_discount));

        }
        else{

            textViewUnitPriceValue.setText(decimalFormatPrice.format(prescriptionCart.getUnit_price())+" "+prescriptionCart.getCurrency());
            textViewUnitDiscValue.setText(decimalFormatPrice.format(prescriptionCart.getDiscount_percent())+" "+prescriptionCart.getCurrency());
            textViewTotalPriceValue.setText(decimalFormatPrice.format(prescriptionCart.getTotal_price())+" "+prescriptionCart.getCurrency());
            int calc_sub_total = prescriptionCart.getUnit_price() * prescriptionCart.getQuantity();
            textViewSubTotalValue.setText(decimalFormatPrice.format(calc_sub_total)+" "+prescriptionCart.getCurrency());
            int cal_discount = prescriptionCart.getDiscount_percent() * prescriptionCart.getQuantity();
            textViewDiscountValue.setText(decimalFormatPrice.format(cal_discount)+" "+prescriptionCart.getCurrency());


        }





        //txtListChild.setText(childText);
        return view;


    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
