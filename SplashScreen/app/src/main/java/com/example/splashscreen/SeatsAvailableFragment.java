package com.example.splashscreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

public class SeatsAvailableFragment extends Fragment {

    private Spinner spinnersav, spinnersav1, spinnersav2;
    private Button btnshowseats;
    private EditText showavailabletxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seats_available, container, false);

        btnshowseats = view.findViewById(R.id.btnshowseats);
        showavailabletxt = view.findViewById(R.id.showavailabletxt);

        //Select Coach Class
        spinnersav = view.findViewById(R.id.spinnersav);
        String[] itemssav = {"--select--","a1","a2","a3","a4","a5"};
        ArrayAdapter adaptersav = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, itemssav);
        spinnersav.setAdapter(adaptersav);

        //Select From
        spinnersav1 = view.findViewById(R.id.spinnersav1);
        String[] itemssav1 = {"--select--","Harare","Chegutu","Plumtree","Bulawayo","Kwekwe"};
        ArrayAdapter adaptersav1 = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, itemssav1);
        spinnersav1.setAdapter(adaptersav1);

        //Select To
        spinnersav2 = view.findViewById(R.id.spinnersav2);
        String[] itemssav2 = {"--select--","Mutare","Beitbridge","Nyamapanda","Gweru"};
        ArrayAdapter adaptersav2 = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, itemssav2);
        spinnersav2.setAdapter(adaptersav2);


        return view;
    }
}