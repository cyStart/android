package com.example.splashscreen;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class IssueTicket extends AppCompatActivity {

//    private Spinner spinner, spinner1, spinner2, spinner3;
    private Button btnjourney, btnseat, btnpassenger, btnpayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_ticket);

        JourneyFragment journeyFragment = new JourneyFragment();
        FragmentTransaction transactionj = getSupportFragmentManager().beginTransaction();
        transactionj.replace(R.id.issuelayout, journeyFragment);
        transactionj.commit();

        btnjourney = findViewById(R.id.btnjourney);
        btnseat = findViewById(R.id.btnseat);
        btnpassenger = findViewById(R.id.btnpassenger);
        btnpayment = findViewById(R.id.btnpayment);


        //1st fragment call for button_journey
        btnjourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               JourneyFragment journeyFragment = new JourneyFragment();
                FragmentTransaction transactionj = getSupportFragmentManager().beginTransaction();
                transactionj.replace(R.id.issuelayout, journeyFragment);
                transactionj.addToBackStack(null);
                transactionj.commit();
            }
        });

        //seats button :: 2nd button
        btnseat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SeatSelectionFragment seatSelectionFragment = new SeatSelectionFragment();
                FragmentTransaction transactionss = getSupportFragmentManager().beginTransaction();
                transactionss.replace(R.id.issuelayout, seatSelectionFragment);
                transactionss.addToBackStack(null);
                transactionss.commit();
            }
        });

        //call button_passenger
        btnpassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PassengerDetailsFragment passengerDetailsFragment = new PassengerDetailsFragment();
                FragmentTransaction transactionpd = getSupportFragmentManager().beginTransaction();
                transactionpd.replace(R.id.issuelayout, passengerDetailsFragment);
                transactionpd.addToBackStack(null);
                transactionpd.commit();
            }
        });

        //call payment button
        btnpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentFragment paymentFragment = new PaymentFragment();
                FragmentTransaction transactionp = getSupportFragmentManager().beginTransaction();
                transactionp.replace(R.id.issuelayout, paymentFragment);
                transactionp.addToBackStack(null);
                transactionp.commit();
            }
        });


    }
}
