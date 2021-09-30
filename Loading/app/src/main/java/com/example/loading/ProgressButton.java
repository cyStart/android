package com.example.loading;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ProgressButton {

    private CardView cardView;
    private ConstraintLayout layout;
    private ProgressBar progressBar;
    private TextView textView;
    ProgressButton (Context ctx, View view){

        cardView = view.findViewById(R.id.cardview);
        layout = view.findViewById(R.id.constraint_layout);
        progressBar = view.findViewById(R.id.progressbar);
        textView = view.findViewById(R.id.textView);

    }
    void buttonActivated(){
        progressBar.setVisibility(View.VISIBLE);
        textView.setText("Loading...");
    }
    void buttonFinished(){
        progressBar.setVisibility(View.GONE);
        //textView.setText("Complete");
    }
}
