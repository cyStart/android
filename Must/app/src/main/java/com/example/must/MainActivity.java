package com.example.must;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*
         Button button = (Button) findViewById(R.id.buttonNext);

         button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                 startActivity(intent);
             }
         });
        */
    }

    public void btn_add_Activity(View view){
        startActivity(new Intent(getApplicationContext(), MainActivity2.class));
    }
}