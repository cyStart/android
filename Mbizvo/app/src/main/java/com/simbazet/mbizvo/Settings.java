package com.simbazet.mbizvo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Set;

public class Settings extends AppCompatActivity {

    EditText txtSettings;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        txtSettings = findViewById(R.id.etSettings);
        btnSave = findViewById(R.id.btnSettings);
        if(SharedPrefManager.getInstance(this).isIPset()){
            txtSettings.setText(SharedPrefManager.getInstance(this).getIP());
        }


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManager.getInstance(Settings.this).setIP(txtSettings.getText().toString());
                Login.ipAdd = SharedPrefManager.getInstance(Settings.this).getIP();
                Toast.makeText(Settings.this, "Changes have been saved, " +
                        "Restart Application for changes to take effect", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(Settings.this, Login.class));
            }
        });
    }
}
