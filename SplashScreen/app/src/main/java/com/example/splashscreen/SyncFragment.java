package com.example.splashscreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

public class SyncFragment extends Fragment {

    private Spinner spinners, spinners1, spinners2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sync, container, false);

        //Select Train
        spinners = view.findViewById(R.id.spinners);
        String[] itemss = {"HARARE_DOWN","MUTARE_DOWN","BULAWAYO_DOWN"};
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, itemss);
        spinners.setAdapter(adapter);

        //Select Station
        spinners1 = view.findViewById(R.id.spinners1);
        String[] itemss1 = {"<select station>","PLUMTREE","HARARE"};
        ArrayAdapter adapter1 = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, itemss1);
        spinners1.setAdapter(adapter1);



        //Select Train Actual Departure
        spinners2 = view.findViewById(R.id.spinners2);
        String[] itemss2 = {"Fri 23 Oct","Thurs 11 Dec","Monday 27 Sept"};
        ArrayAdapter adapter2 = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, itemss2);
        spinners2.setAdapter(adapter2);

        return view;
    }
}