package com.simbazet.mbizvo.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.simbazet.mbizvo.MainActivity;
import com.simbazet.mbizvo.MapsActivity;
import com.simbazet.mbizvo.R;
import com.simbazet.mbizvo.SharedPrefManager;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.simbazet.mbizvo.SitesData;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener {

    TextView nameuser, walletuser, review, network, plugins, myapps, mainmenus,
            pagetitle, pagesubtitle;

    Animation atg, atgtwo, atgthree;

    LinearLayout LLreports, LLhistory, LLfaults, LLsites, LLadmin, LLresident, LLfaultsR, LLreportRequest, LLtechnician, LLsitesT, LLfaultsT;

    PieChart pieChart ;
    private static ArrayList<Entry> entries ;
    ArrayList<String> PieEntryLabels ;
    PieDataSet pieDataSet ;
    PieData pieData ;
    private static Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();

        String ut = SharedPrefManager.getInstance(getContext()).getUserType();

        atg = AnimationUtils.loadAnimation(getActivity(), R.anim.atg);
        atgtwo = AnimationUtils.loadAnimation(getActivity(), R.anim.atgtwo);
        atgthree = AnimationUtils.loadAnimation(getActivity(), R.anim.atgthree);

        nameuser = view.findViewById(R.id.nameuser);
        walletuser = view.findViewById(R.id.walletuser);

        LLtechnician = view.findViewById(R.id.LLtechnician);
        LLsitesT = view.findViewById(R.id.LLsitesT);
        LLfaultsT = view.findViewById(R.id.LLfaultsT);

        LLadmin = view.findViewById(R.id.LLadmin);
        LLresident = view.findViewById(R.id.LLresident);
        LLfaultsR = view.findViewById(R.id.LLfaultsR);
        LLreportRequest = view.findViewById(R.id.LLreportRequest);

        LLreports = view.findViewById(R.id.LLreports);
        LLfaults = view.findViewById(R.id.LLfaults);
        LLsites = view.findViewById(R.id.LLsites);

        review = view.findViewById(R.id.review);
        network = view.findViewById(R.id.network);
        plugins = view.findViewById(R.id.plugins);
        //myapps = view.findViewById(R.id.myapps);
        //mainmenus = view.findViewById(R.id.mainmenus);

        pagetitle = view.findViewById(R.id.pagetitle);
        pagesubtitle = view.findViewById(R.id.pagesubtitle);

        LLfaults.setOnClickListener(this);
        LLsites.setOnClickListener(this);
        LLreports.setOnClickListener(this);
        LLsitesT.setOnClickListener(this);
        LLfaultsT.setOnClickListener(this);

        LLfaultsR.setOnClickListener(this);
        LLreportRequest.setOnClickListener(this);

        if("Resident".equals(ut)){
            LLadmin.setVisibility(View.GONE);
            LLtechnician.setVisibility(View.GONE);
        }
        if("Admin".equals(ut)){
            LLtechnician.setVisibility(View.GONE);
            LLresident.setVisibility(View.GONE);
        }
        if("Technician".equals(ut)){
            LLadmin.setVisibility(View.GONE);
            LLresident.setVisibility(View.GONE);
        }


        //Setting fields
        String fname = SharedPrefManager.getInstance(getActivity()).getFname();
        String lname = SharedPrefManager.getInstance(getActivity()).getLname();
        String fullName = lname + " " + fname;
        nameuser.setText(fullName);
        String type = "(" + SharedPrefManager.getInstance(getActivity()).getUserType() + ")";
        walletuser.setText(type);

        pieChart = view.findViewById(R.id.chart1);

        entries = new ArrayList<>();

        PieEntryLabels = new ArrayList<String>();

        AddValuesToPIEENTRY();

        AddValuesToPieEntryLabels();

        pieDataSet = new PieDataSet(entries, "");

        pieData = new PieData(PieEntryLabels, pieDataSet);

        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        pieChart.setData(pieData);

        pieChart.animateY(3000);



        /*pagetitle.startAnimation(atgtwo);
        pagesubtitle.startAnimation(atgtwo);*/
        
    }

    @Override
    public void onClick(View view) {

        if ((view == LLfaults)) {
            MainActivity.selectedAction.setText("Requests");
            MainActivity.fragmentManager.beginTransaction().replace(R.id.containerView, new RequestsFragment()).commit();
        }if ((view == LLfaultsT)) {
            MainActivity.selectedAction.setText("Requests");
            MainActivity.fragmentManager.beginTransaction().replace(R.id.containerView, new RequestsFragment()).commit();
        }if ((view == LLfaultsR)) {
            MainActivity.selectedAction.setText("Requests");
            MainActivity.fragmentManager.beginTransaction().replace(R.id.containerView, new RequestsFragment()).commit();
        }

        if ((view == LLsites)) {
            startActivity(new Intent(getContext(), MapsActivity.class));
        }
        if ((view == LLsitesT)) {
            siteTech();
            startActivity(new Intent(getContext(), MapsActivity.class));
        }

        if ((view == LLreports)) {
            MainActivity.selectedAction.setText("Reports");
            MainActivity.fragmentManager.beginTransaction().replace(R.id.containerView, new ReportsFragment()).commit();
        }

        if ((view == LLreportRequest)) {
            MainActivity.selectedAction.setText("Reports");
            MainActivity.fragmentManager.beginTransaction().replace(R.id.containerView, new ReportRequestFragment()).commit();
        }
    }

    public static void AddValuesToPIEENTRY(){
        String userid = SharedPrefManager.getInstance(context).getUserID().toString();
        String usertype = SharedPrefManager.getInstance(context).getUserType();

        int total = 0;
        int assigned = 0;
        int completed = 0;

        for (SitesData fd: MainActivity.sitesList) {
            if (usertype.equals("Resident")){
                if (fd.getUserID().equals(userid)){
                    total++;
                    if("Assigned".equals(fd.getTaskStatus())) { assigned++;  }
                    if("Completed".equals(fd.getTaskStatus())) { completed++;  }
                }
            }
            if (usertype.equals("Technician")){
                if (fd.getTechID().equals(userid)){
                    total++;
                    if("Assigned".equals(fd.getTaskStatus())) { assigned++;  }
                    if("Completed".equals(fd.getTaskStatus())) { completed++;  }
                }
            }
            if (usertype.equals("Admin")){
                total++;
                if("Assigned".equals(fd.getTaskStatus())) { assigned++;  }
                if("Completed".equals(fd.getTaskStatus())) { completed++;  }
            }
        }
        entries.add(new BarEntry(total, 0));
        entries.add(new BarEntry(assigned, 1));
        entries.add(new BarEntry(completed, 2));

    }

    public void AddValuesToPieEntryLabels(){
        String usertype = SharedPrefManager.getInstance(context).getUserType();
        if (usertype.equals("Technician")){
            PieEntryLabels.add("Accumulated");
        }
        if (usertype.equals("Admin")){
            PieEntryLabels.add("Total");
        }
        if (usertype.equals("Resident")){
            PieEntryLabels.add("Reported");
        }
        PieEntryLabels.add("Assigned");
        PieEntryLabels.add("Completed");

    }

    private static void siteTech(){
        for(SitesData sd : MainActivity.sitesList){
            if(sd.getTechID().equals(SharedPrefManager.getInstance(context).getUserID().toString())){
                MainActivity.sitesDataList.add(sd);
            }
        }
    }
}
