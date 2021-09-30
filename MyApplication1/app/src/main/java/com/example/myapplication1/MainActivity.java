package com.example.myapplication1;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText name, contact, dob;
    Button insert, update, delete, view;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        contact = findViewById(R.id.contact);
        dob = findViewById(R.id.dob);

        insert = findViewById(R.id.btnInsert);
        update = findViewById(R.id.btnUpdate);
        delete = findViewById(R.id.btnDelete);
        view = findViewById(R.id.btnView);

        DB = new DBHelper(this);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameTXT = name.getText().toString();
                String contactTXT = contact.getText().toString();
                String dobTXT = dob.getText().toString();

                Boolean checkInsertData = DB.insertUserData(nameTXT,contactTXT,dobTXT);
                if (checkInsertData==true)
                    Toast.makeText(MainActivity.this,"New Entry Inserted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "Could not complete insertion", Toast.LENGTH_SHORT).show();
            }
        });



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameTXT = name.getText().toString();
                String contactTXT = contact.getText().toString();
                String dobTXT = dob.getText().toString();

                Boolean checkUpdateData = DB.updateUserData(nameTXT,contactTXT,dobTXT);
                if (checkUpdateData==true)
                    Toast.makeText(MainActivity.this,"Entry Updated", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "Failed to Update", Toast.LENGTH_SHORT).show();
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameTXT = name.getText().toString();

                Boolean checkDeleteData = DB.deleteData(nameTXT);
                if (checkDeleteData==true)
                    Toast.makeText(MainActivity.this,"Entry Deleted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "Delete Unsuccesful", Toast.LENGTH_SHORT).show();
            }
        });


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = DB.getData();
                if (res.getCount()==0){
                    Toast.makeText(MainActivity.this, "No Entries to show", Toast.LENGTH_SHORT).show();
                    ret
                }
            }
        });
        /*
        new CountDownTimer(10000,100){
            public void onTick(long milliSecondsUntilDone){
                Log.i("Seconds left!",String.valueOf(milliSecondsUntilDone/1000));
            }
            public void onFinish(){
                Log.i("We are done", "No more countdown");
            }
        }.start();
        */
    }
}