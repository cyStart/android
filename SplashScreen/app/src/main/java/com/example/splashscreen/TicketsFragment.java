package com.example.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class TicketsFragment extends Fragment {

    private ImageView payticket, addseat, showseats, ticketsync, upgradeticket;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tickets, container, false);

        payticket = view.findViewById(R.id.ic_payticket);
        addseat = view.findViewById(R.id.ic_addseat);
        showseats = view.findViewById(R.id.ic_availability);
        ticketsync = view.findViewById(R.id.ic_tickectsync);
        upgradeticket = view.findViewById(R.id.ic_upgradeticket);

        //issue ticket call
        payticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), IssueTicket.class);
                startActivity(intent);
            }
        });

        //addseat call
        addseat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddSeatFragment addSeatFragment = new AddSeatFragment();
                FragmentTransaction transactionas = getActivity().getSupportFragmentManager().beginTransaction();
                transactionas.replace(R.id.layout, addSeatFragment);
                transactionas.commit();
            }
        });

        //show available seats call
        showseats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SeatsAvailableFragment seatsAvailableFragment = new SeatsAvailableFragment();
                FragmentTransaction transactionas = getActivity().getSupportFragmentManager().beginTransaction();
                transactionas.replace(R.id.layout, seatsAvailableFragment);
                transactionas.commit();

            }
        });

        //sync tickets call
        ticketsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SyncFragment syncFragment = new SyncFragment();
                FragmentTransaction transactionst = getActivity().getSupportFragmentManager().beginTransaction();
                transactionst.replace(R.id.layout, syncFragment);
                transactionst.commit();
            }
        });

        //for upgrade icon
        upgradeticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpgradeTicket.class);
                startActivity(intent);
            }
        });


        return view;
    }
}
