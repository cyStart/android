package com.example.tabbedapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fragment4 extends Fragment {
    EditText editText;
    Button button;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // return inflater.inflate(R.layout.fragment4,container,false);

        View view = inflater.inflate(R.layout.fragment4, container, false);
        button = view.findViewById(R.id.btn);
        editText = view.findViewById(R.id.etName);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(),SecondActivity.class);
                intent.putExtra("key", editText.getText().toString());
                startActivity(intent);
            }
        });
        return view;
    }

}
