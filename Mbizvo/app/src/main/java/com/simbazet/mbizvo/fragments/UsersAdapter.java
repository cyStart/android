package com.simbazet.mbizvo.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simbazet.mbizvo.R;
import com.simbazet.mbizvo.SharedPrefManager;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private List<UsersData> usersDataList;
    private Context context;

    public UsersAdapter(List<UsersData> usersDataList, Context context) {
        this.usersDataList = usersDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UsersData usersData = usersDataList.get(position);

        String name = usersData.getUserFname() + " " + usersData.getUserLname();
        holder.txtName.setText(name);
        holder.txtPhone.setText(usersData.getUserPhone());
        holder.txtType.setText(usersData.getUserType());

    }

    @Override
    public int getItemCount() {
        return usersDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtPhone, txtType;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            txtType = itemView.findViewById(R.id.txtType);
        }
    }
}
