package com.example.splashscreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class JourneyFragment extends Fragment {

    private Spinner spinner, spinner1, spinner2, spinner3;
    private Button btnjourneynext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_journey, container, false);

        btnjourneynext = view.findViewById(R.id.btnjourneynext);

        //Select Train
        spinner = view.findViewById(R.id.spinner);
        String[] items = {"HARARE_DOWN","MUTARE_DOWN","BULAWAYO_DOWN"};
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);

        //Select From Destination
        spinner1 = view.findViewById(R.id.spinner1);
        String[] items1 = {"KWEKWE","PLUMTREE","HARARE"};
        ArrayAdapter adapter1 = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, items1);
        spinner1.setAdapter(adapter1);



        //Select Where you are going
        spinner2 = view.findViewById(R.id.spinner2);
        String[] items2 = {"BULAWAYO","GWERU","MASVINGO"};
        ArrayAdapter adapter2 = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, items2);
        spinner2.setAdapter(adapter2);



        //Select your CoachClass
        spinner3 = view.findViewById(R.id.spinner3);
        String[] items3 = {"A1","B2","C3"};
        ArrayAdapter adapter3 = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, items3);
        spinner3.setAdapter(adapter3);

        btnjourneynext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SeatSelectionFragment seatSelectionFragment = new SeatSelectionFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.issuelayout, seatSelectionFragment);
                transaction.commit();
            }
        });


        return view;
    }
}