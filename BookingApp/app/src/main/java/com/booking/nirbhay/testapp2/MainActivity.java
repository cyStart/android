package com.booking.nirbhay.testapp2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.booking.nirbhay.testapp2.frgmsnts.ListOfBus;
import com.booking.nirbhay.testapp2.frgmsnts.PakegTour;
import com.booking.nirbhay.testapp2.frgmsnts.Profile;

public class MainActivity extends AppCompatActivity {

    LinearLayout home,profile,busList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        home=(LinearLayout)findViewById(R.id.home);
        profile=(LinearLayout)findViewById(R.id.profile);
        busList=(LinearLayout)findViewById(R.id.busList);
        home.setBackgroundColor(Color.parseColor("#aeaeae"));
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.setBackgroundColor(Color.parseColor("#aeaeae"));
                profile.setBackgroundColor(Color.parseColor("#dddddd"));
                busList.setBackgroundColor(Color.parseColor("#dddddd"));
                Fragment fObje=new ListOfBus();
                callFragment(fObje);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.setBackgroundColor(Color.parseColor("#dddddd"));
                profile.setBackgroundColor(Color.parseColor("#aeaeae"));
                busList.setBackgroundColor(Color.parseColor("#dddddd"));
                Fragment fObje=new Profile();
                callFragmentWithBack(fObje,"pro");
            }
        });
        busList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.setBackgroundColor(Color.parseColor("#dddddd"));
                profile.setBackgroundColor(Color.parseColor("#dddddd"));
                busList.setBackgroundColor(Color.parseColor("#aeaeae"));
                Fragment fObje=new PakegTour();
                callFragmentWithBack(fObje,"pk");

            }
        });
        Fragment fObje=new ListOfBus();
        callFragment(fObje);
    }
    public void callFragment(Fragment callFrag)
    {
        FragmentManager req_mnt=getFragmentManager();
        FragmentTransaction req_transion=req_mnt.beginTransaction();
        req_transion.replace(R.id.content_frame, callFrag);
        req_transion.commit();

    }
    public void callFragmentWithBack(Fragment callFrag,String bk)
    {
        FragmentManager req_mnt=getFragmentManager();
        FragmentTransaction req_transion=req_mnt.beginTransaction().addToBackStack(bk);
        req_transion.replace(R.id.content_frame, callFrag);
        req_transion.commit();

    }

}
