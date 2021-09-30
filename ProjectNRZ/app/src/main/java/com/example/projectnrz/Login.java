package com.example.projectnrz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    private Button button;
    private EditText loginid, password;
    float v=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button = findViewById(R.id.login);
        loginid = findViewById(R.id.txtlogin);
        password = findViewById(R.id.txtpassword);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);

            }
        });
        button.setTranslationY(300);
        loginid.setTranslationY(300);
        password.setTranslationY(300);

        button.setAlpha(v);
        loginid.setAlpha(v);
        password.setAlpha(v);

        loginid.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(300).start();
        password.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        button.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(900).start();
    }
}