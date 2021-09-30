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
import androidx.fragment.app.FragmentTransaction;

public class SeatSelectionFragment extends Fragment {

    private Spinner spinnerss;
    private Button prev, next;
    EditText seatfare;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seat_selection, container, false);

        prev = view.findViewById(R.id.btnseatselleprev);
        next = view.findViewById(R.id.btnseatsellenext);
        seatfare = view.findViewById(R.id.seatsellefare);

        //Select Train
        spinnerss = view.findViewById(R.id.spinnerss);
        String[] itemss = {"--select--","1","2","3","4"};
        ArrayAdapter adapterss = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, itemss);
        spinnerss.setAdapter(adapterss);

        //for button prev
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JourneyFragment journeyFragment = new JourneyFragment();
                FragmentTransaction transactionj = getActivity().getSupportFragmentManager().beginTransaction();
                transactionj.replace(R.id.issuelayout, journeyFragment);
                transactionj.commit();
            }
        });

        //for button next
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PassengerDetailsFragment passengerDetailsFragment = new PassengerDetailsFragment();
                FragmentTransaction transactionp = getActivity().getSupportFragmentManager().beginTransaction();
                transactionp.replace(R.id.issuelayout, passengerDetailsFragment);
                transactionp.commit();
            }
        });

        return view;
    }
}