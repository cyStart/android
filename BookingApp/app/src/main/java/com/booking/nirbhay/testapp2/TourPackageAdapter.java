/*
 * Copyright (c) 2017. Truiton (http://www.truiton.com/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Mohit Gupt (https://github.com/mohitgupt)
 *
 */

package com.booking.nirbhay.testapp2;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TourPackageAdapter extends RecyclerView
        .Adapter<TourPackageAdapter
        .DataObjectHolder> {
    Context cntext;
    Typeface sonictonicfonts;
    private static String LOG_TAG = "CategoryRecyclerViewAdapter";
    private ArrayList<BusModel> mDataset;
    private static MyClickListener myClickListener;




    public static class DataObjectHolder extends RecyclerView.ViewHolder
           {
               TextView pronam,subjectName,startdt,enddt,courceName,teacherName;

        ImageView primg,deletbtn;

        public DataObjectHolder(View itemView) {
            super(itemView);
            pronam = (TextView) itemView.findViewById(R.id.pronam);
            subjectName = (TextView) itemView.findViewById(R.id.subjectName);
            startdt = (TextView) itemView.findViewById(R.id.startdt);
            enddt = (TextView) itemView.findViewById(R.id.enddt);
            teacherName = (TextView) itemView.findViewById(R.id.teacherName);
            courceName = (TextView) itemView.findViewById(R.id.courceName);

            primg=(ImageView)itemView.findViewById(R.id.primg);
            deletbtn=(ImageView)itemView.findViewById(R.id.deletbtn);

            Log.i(LOG_TAG, "Adding Listener");


        }


    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public TourPackageAdapter(ArrayList<BusModel> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.seach_bus_row, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        cntext=parent.getContext();

        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

        sonictonicfonts = Typeface.createFromAsset(cntext.getAssets(), "Raleway_Regular.ttf");


        holder.pronam.setText("Bus Name: "+mDataset.get(position).getBus_name());
        holder.pronam.setTypeface(sonictonicfonts);
        holder.subjectName.setText("Bus Number: "+mDataset.get(position).getBus_number());
        holder.subjectName.setTypeface(sonictonicfonts);
        holder.startdt.setText("From: "+mDataset.get(position).getBus_source());
        holder.startdt.setTypeface(sonictonicfonts);
        holder.enddt.setText("Des: "+mDataset.get(position).getBus_dest());
        holder.enddt.setTypeface(sonictonicfonts);
//
      holder.teacherName.setText("Pick-Up:"+mDataset.get(position).getBus_pickup());
        holder.teacherName.setTypeface(sonictonicfonts);
//
        holder.courceName.setText("Source Time: "+mDataset.get(position).getSource_time()+"  Dest Time: "+mDataset.get(position).getDes_time());
        holder.courceName.setTypeface(sonictonicfonts);

    }

    public void addItem(BusModel dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}