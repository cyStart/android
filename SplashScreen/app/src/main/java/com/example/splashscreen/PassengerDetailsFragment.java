package com.example.splashscreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class PassengerDetailsFragment extends Fragment {

    private EditText name, surname, phone, idno;
    private Button btnprev, btnnext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_passenger_details, container, false);

        btnprev = view.findViewById(R.id.btnpassprev);
        btnnext = view.findViewById(R.id.btnpassnext);

        name = view.findViewById(R.id.passnametxt);
        surname = view.findViewById(R.id.passsurnametxt);
        phone = view.findViewById(R.id.passphonetxt);
        idno = view.findViewById(R.id.passidnotxt);

        //for button prev
        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SeatSelectionFragment seatSelectionFragment = new SeatSelectionFragment();
                FragmentTransaction transactionps = getActivity().getSupportFragmentManager().beginTransaction();
                transactionps.replace(R.id.issuelayout, seatSelectionFragment);
                transactionps.commit();
            }
        });

        //for button next
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentFragment paymentFragment = new PaymentFragment();
                FragmentTransaction transactionpm = getActivity().getSupportFragmentManager().beginTransaction();
                transactionpm.replace(R.id.issuelayout, paymentFragment);
                transactionpm.commit();
            }
        });

        return view;
    }
}