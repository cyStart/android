package com.example.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class PaymentFragment extends Fragment {

    private Button btnprev, btnnext;
    private EditText totalpay, paid, balance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        btnprev = view.findViewById(R.id.btnpayprev);
        btnnext = view.findViewById(R.id.btnpaynext);

        totalpay = view.findViewById(R.id.paymenttxt);
        paid = view.findViewById(R.id.paidtxt);
        balance = view.findViewById(R.id.balancetxt);

        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PassengerDetailsFragment passengerDetailsFragment = new PassengerDetailsFragment();
                FragmentTransaction transactionps = getActivity().getSupportFragmentManager().beginTransaction();
                transactionps.replace(R.id.issuelayout, passengerDetailsFragment);
                transactionps.commit();
            }
        });


        //for button next
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TicketComplete.class);
                startActivity(intent);
            }
        });

        return view;
    }
}