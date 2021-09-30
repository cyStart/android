package com.webandcrafts.healwire.ui.home;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.webandcrafts.healwire.ui.my_cart.MyCartActivity;
import com.webandcrafts.healwire.ui.my_orders.MyOrdersActivity;
import com.webandcrafts.healwire.ui.my_prescription.MyPrescriptionActivity;
import com.webandcrafts.healwire.NewBaseActivity;
import com.webandcrafts.healwire.ui.profile.ProfileActivity;
import com.webandcrafts.healwire.R;
import com.webandcrafts.healwire.ui.about_us.AboutusActivity;

public class MainActivity extends NewBaseActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT<16){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else{

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            View decorView = getWindow().getDecorView();
            //Hide the status bar
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            //Remember that you should never show the action bar if the status bar is hidden, so hide that too if necessary..
//            ActionBar ab= getSupportActionBar();
//            ab.hide();

            try{
            getSupportActionBar().hide();}
            catch (Exception e ){}


        }

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);




//            ActionBar actionBar = getSupportActionBar();
//            actionBar.setDisplayHomeAsUpEnabled(true);


        initNavigationDrawer();

    }


    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id){
                    case R.id.home:

                        drawerLayout.closeDrawers();
                        break;
                    case R.id.profile:

                        Intent intentProfile =  new Intent(getApplicationContext(),ProfileActivity.class);
                        startActivity(intentProfile);

                        break;
                    case R.id.myprescription:

                        Intent intentPrescription=  new Intent(getApplicationContext(),MyPrescriptionActivity.class);
                        startActivity(intentPrescription);

                        drawerLayout.closeDrawers();
                        break;
                    case R.id.myorders:

                        Intent intentOrders=  new Intent(getApplicationContext(),MyOrdersActivity.class);
                        startActivity(intentOrders);

                        drawerLayout.closeDrawers();
                        break;
                    case R.id.itemsincart:

                        Intent intentItemsInCart=  new Intent(getApplicationContext(),MyCartActivity.class);
                        startActivity(intentItemsInCart);

                        drawerLayout.closeDrawers();
                        break;
                    case R.id.about:

                        Intent intentAbout=  new Intent(getApplicationContext(),AboutusActivity.class);
                        startActivity(intentAbout);

                        drawerLayout.closeDrawers();
                        break;
                    case R.id.exit:
                        AlertDialog alertDialog;
                        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Do you want to logout?");
                        builder.setIcon(android.R.drawable.ic_dialog_alert);
//        builder.setIcon(R.mipmap.app_icon_blue_and_yellow);
                        builder.setCancelable(false);

                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setResult(Activity.RESULT_OK);
                                MainActivity.super.onBackPressed();
                                System.exit(0);
                            }
                        });

                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setResult(Activity.RESULT_CANCELED);
                            }
                        });
                        alertDialog = builder.create();
                        alertDialog.show();

                }
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
      //  TextView tv_email = (TextView)header.findViewById(R.id.textViewName);
        //tv_email.setText("raj.amalw@learn2crack.com");
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }






}
