package com.example.tickets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private Button btnsync, btnupgrade, btndetail, btnreport;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnsync = findViewById(R.id.sync);
        btnupgrade = findViewById(R.id.upgrade);
        btndetail = findViewById(R.id.detail);
        btnreport = findViewById(R.id.report);

        //1st fragment call
        btnsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SyncFragment syncFragment = new SyncFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.layout, syncFragment);
                transaction.commit();
            }
        });

        //2nd fragment call



        //3rd fragment call




        //4th fragment call


    }
}