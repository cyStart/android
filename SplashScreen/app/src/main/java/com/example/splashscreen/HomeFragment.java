package com.example.splashscreen;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class HomeFragment extends Fragment {

    private ImageView ticketing, syncdata, profile, aboutus, reports;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ticketing = view.findViewById(R.id.ic_ticketing);
        syncdata = view.findViewById(R.id.ic_sync);
        profile = view.findViewById(R.id.ic_profile);
        aboutus = view.findViewById(R.id.ic_info);
        reports = view.findViewById(R.id.ic_reports);


        //for icon Ticketing
        ticketing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TicketsFragment ticketsFragment = new TicketsFragment();
                FragmentTransaction transactiont = getActivity().getSupportFragmentManager().beginTransaction();
                transactiont.replace(R.id.layout, ticketsFragment);
                transactiont.commit();

            }
        });

        //for icon Sync Data
        syncdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SyncFragment syncFragment = new SyncFragment();
                FragmentTransaction transactions = getActivity().getSupportFragmentManager().beginTransaction();
                transactions.replace(R.id.layout, syncFragment);
                transactions.commit();
            }
        });

        //for icon profile
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment profileFragment = new ProfileFragment();
                FragmentTransaction transactionp = getActivity().getSupportFragmentManager().beginTransaction();
                transactionp.replace(R.id.layout, profileFragment);
                transactionp.commit();
            }
        });

        //for about icon
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutUsFragment aboutUsFragment = new AboutUsFragment();
                FragmentTransaction transactionau = getActivity().getSupportFragmentManager().beginTransaction();
                transactionau.replace(R.id.layout, aboutUsFragment);
                transactionau.commit();
            }
        });

        //for reports icon
        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DayendFragment dayendFragment = new DayendFragment();
                FragmentTransaction transactionde = getActivity().getSupportFragmentManager().beginTransaction();
                transactionde.replace(R.id.layout, dayendFragment);
                transactionde.commit();
            }
        });



        return view;
    }
}