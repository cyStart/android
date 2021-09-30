package com.example.splashscreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class AddSeatFragment extends Fragment {

    private Spinner spinneras, spinneras1, spinneras2, spinneras3, spinneras4;
    private Button btncancel, btnok;
    private EditText amount;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_seat, container, false);

        amount = view.findViewById(R.id.addseatamount);
        radioGroup = view.findViewById(R.id.radioGroup);

        //Select Seat #
        spinneras = view.findViewById(R.id.spinneras);
        String[] itemsas = {"--select--", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        ArrayAdapter adapteras = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, itemsas);
        spinneras.setAdapter(adapteras);

        //Select Concession
        spinneras1 = view.findViewById(R.id.spinneras1);
        String[] itemsas1 = {"--select--", "rudd", "rail", "concession"};
        ArrayAdapter adapteras1 = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, itemsas1);
        spinneras1.setAdapter(adapteras1);

        //Select Appr #
        spinneras2 = view.findViewById(R.id.spinneras2);
        String[] itemsas2 = {"--select--", "221", "233", "211"};
        ArrayAdapter adapteras2 = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, itemsas2);
        spinneras2.setAdapter(adapteras2);

        //Select Luggage Quantity
        spinneras3 = view.findViewById(R.id.spinneras3);
        String[] itemsas3 = {"--select--", "1", "2", "3", "4", "5"};
        ArrayAdapter adapteras3 = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, itemsas3);
        spinneras3.setAdapter(adapteras3);


        //Select Penalty
        spinneras4 = view.findViewById(R.id.spinneras4);
        String[] itemsas4 = {"--select--", "2", "4", "6"};
        ArrayAdapter adapteras4 = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, itemsas4);
        spinneras4.setAdapter(adapteras4);

        return view;
    }
    public void onClick(View view){

        int radioID = radioGroup.getCheckedRadioButtonId();
        radioButton = view.findViewById(radioID);

        textView.setText("Your choice is: " +radioButton.getText());


    }

    public void checkButton(View view) {

        int radioID = radioGroup.getCheckedRadioButtonId();
        radioButton = view.findViewById(radioID);

        Toast.makeText(getActivity(), "Selected Radio Button: " + radioButton.getText(), Toast.LENGTH_SHORT).show();
    }
}