package com.example.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class UpgradeTicket extends AppCompatActivity {

    private Button search;
    private EditText ticketcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_ticket);

        search = findViewById(R.id.btnsearch);
        ticketcode = findViewById(R.id.ticketcodetxt);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpgradeTicket.this, SearchResult.class);
                startActivity(intent);
            }
        });


    }
}