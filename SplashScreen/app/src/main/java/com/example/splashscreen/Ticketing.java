package com.example.splashscreen;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class Ticketing extends AppCompatActivity {

    private LinearLayout layout;
    private Button btntickets, btnhome, btnreports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketing);

        btntickets = findViewById(R.id.btntickets);
        btnhome = findViewById(R.id.btnhome);
        btnreports = findViewById(R.id.btnreports);

        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
        transaction2.replace(R.id.layout, homeFragment);
        transaction2.commit();

        //1st fragment call to TICKETING MENU
        btntickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TicketsFragment ticketsFragment = new TicketsFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.layout, ticketsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        //2nd fragment call for button HOME
        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment homeFragment = new HomeFragment();
                FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                transaction2.replace(R.id.layout, homeFragment);
                transaction2.addToBackStack(null);
                transaction2.commit();
            }
        });

        //3rd fragment call for button REPORTS
        btnreports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*ReportsFragment reportsFragment = new ReportsFragment();
                FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                transaction3.replace(R.id.layout, reportsFragment);
                transaction3.commit();*/

                DayendFragment dayendFragment = new DayendFragment();
                FragmentTransaction transactionde = getSupportFragmentManager().beginTransaction();
                transactionde.replace(R.id.layout, dayendFragment);
                transactionde.addToBackStack(null);
                transactionde.commit();
            }
        });
    }
}