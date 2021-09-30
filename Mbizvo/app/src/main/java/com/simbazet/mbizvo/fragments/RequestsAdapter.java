package com.simbazet.mbizvo.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.simbazet.mbizvo.R;
import com.simbazet.mbizvo.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder> {

    private List<RequestsData> faultsDataList;
    public static List<RequestsData> faultsDataLists;
    private Context context;

    public RequestsAdapter(List<RequestsData> faultsDataList, Context context) {

        faultsDataLists = new ArrayList<>();

        int userid = SharedPrefManager.getInstance(context).getUserID();
        String usertype = SharedPrefManager.getInstance(context).getUserType();
        for (RequestsData fd: faultsDataList) {
            if (usertype.equals("Resident")){
                if (fd.getUserID() == userid){
                    faultsDataLists.add(fd);
                }
            }
            if (usertype.equals("Technician")){

                if (fd.getTechID() == userid){
                    faultsDataLists.add(fd);
                }
            }
            if (usertype.equals("Admin")){

                faultsDataLists.add(fd);
            }
        }
        this.faultsDataList = faultsDataLists;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_faults, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequestsData faultsData = faultsDataList.get(position);
        String usertype = SharedPrefManager.getInstance(context).getUserType();

        holder.txtTitle.setText(faultsData.getRequestTitle());
        holder.txtDate.setText(faultsData.getRequestDate());
        holder.txtStatus.setText(faultsData.getRequestStatus());
        holder.txtStart.setText(faultsData.getTaskStart());
        holder.txtEnd.setText(faultsData.getTaskEnd());
        holder.txtLocation.setText(faultsData.getRequestLocation());
        holder.txtTaskStatus.setText(faultsData.getTaskStatus());

        if(usertype.equals("Resident")){
            holder.taskLocations.setVisibility(View.GONE);
            holder.taskDates.setVisibility(View.GONE);
        }
        if(usertype.equals("Technician")){
            holder.faultDates.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return faultsDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle, txtDate, txtStatus, txtStart, txtEnd, txtLocation, txtTaskStatus;
        public RelativeLayout faultDates,taskLocations, taskDates;
        public CheckBox checkbox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtStart = itemView.findViewById(R.id.txtStart);
            txtEnd = itemView.findViewById(R.id.txtEnd);
            txtLocation = itemView.findViewById(R.id.txtLocation);
            txtTaskStatus = itemView.findViewById(R.id.txtTaskStatus);
            checkbox = itemView.findViewById(R.id.checkbox);

            faultDates = itemView.findViewById(R.id.faultDates);
            taskLocations = itemView.findViewById(R.id.taskLocations);
            taskDates = itemView.findViewById(R.id.taskDates);
        }
    }
}
