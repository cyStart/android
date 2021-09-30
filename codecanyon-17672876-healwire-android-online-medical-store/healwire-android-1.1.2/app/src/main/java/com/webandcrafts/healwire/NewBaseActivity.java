package com.webandcrafts.healwire;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created By vineeth on 7/20/2018.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class NewBaseActivity extends AppCompatActivity {

    private AlertDialog mDialog;

    public Snackbar snackbar;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Snackbar.make(getWindow().getDecorView().getRootView(), "Click the pin for more options", Snackbar.LENGTH_LONG).show();


//        snackbar = Snackbar
//                .make(getWindow().getDecorView().getRootView(), "No Internet Connection", Snackbar.LENGTH_LONG);
//        snackbar.setDuration(BaseTransientBottomBar.LENGTH_INDEFINITE);
//        snackbar.getView().getLayoutParams().width = AppBarLayout.LayoutParams.MATCH_PARENT;
//        snackbar.getView().getLayoutParams().height =170;

//        View view = snackbar.getView();
//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
//        params.setMargins(0, 0, 0, Utils.dpToPx(NewBaseActivity.this, 70));
//        params.gravity = Gravity.BOTTOM;
//        view.setLayoutParams(params);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            snackbar.getView().setElevation(0);
//        }
//
//        snackbar.getView().setBackgroundColor(Color.parseColor("#007AFF"));
//        TextView tv = (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);
//        tv.setTextColor(Color.parseColor("#FFFFFF"));
//        tv.setTextSize(14);


//        tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Poppins-Regular.ttf"));


        //Internet connectivity change listener starting..........
        registerReceiver(
                mConnectivityChangeReceiver,
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));
        //End.....................................................
    }

    @Override
    protected void onDestroy() {

        if (mDialog != null) {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            mDialog = null;
        }

        unregisterReceiver(mConnectivityChangeReceiver);
        super.onDestroy();
    }

//    @Override
//    protected void onResume() {
//
//        if (!isNetworkAvailable()) {
//            showNetworkCheckingDialog();
////                        showSnackBar();
//        } else {
////                        dismissSnackBar();
//            if (mDialog != null) {
//
//                if (mDialog.isShowing()) {
//                    mDialog.dismiss();
//                }
//                mDialog = null;
//            }
//        }
//        super.onResume();
//    }

    private final BroadcastReceiver mConnectivityChangeReceiver =
            new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {


                    if (!isNetworkAvailable()) {
                        showNetworkCheckingDialog();
//                        showSnackBar();
                    } else {
//                        dismissSnackBar();
                        if (mDialog != null) {

                            if (mDialog.isShowing()) {
                                mDialog.dismiss();
                            }
                            mDialog = null;
                        }
                    }
                }
            };

    private void showNetworkCheckingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewBaseActivity.this);
        builder.setTitle("Lost Internet");
        builder.setMessage("You cannot proceed because you have lost internet connection. Please make sure that you have active WIFI or Data Connection  enabled.");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
//        builder.setIcon(R.mipmap.app_icon_blue_and_yellow);
        builder.setCancelable(false);

        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setResult(Activity.RESULT_CANCELED);
                ActivityCompat.finishAffinity(NewBaseActivity.this);
            }
        });

        mDialog = builder.create();
        mDialog.show();
    }

//    public void showSnackBar() {
//        if (snackbar != null)
//            snackbar.show();
//    }

//    public void dismissSnackBar() {
//        if (snackbar != null && snackbar.isShown())
//            snackbar.dismiss();
//    }

//    public void shakeSnackbar() {
//
//        if (snackbar != null && snackbar.isShown()) {
////            snackbar.dismiss();
//
////            snackbar.getView().setVisibility(View.GONE);
//
//            final View view = snackbar.getView();
//            view.animate()
//                    .translationY(view.getHeight()/3)
//                    .alpha(0.0f)
//                    .setDuration(120)
//                    .setListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            super.onAnimationEnd(animation);
//                            view.setVisibility(View.GONE);
//                        }
//                    });
//        }
//
//
//        handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//
//                if (snackbar != null) {
////                    snackbar.show();
////                    snackbar.getView().setVisibility(View.VISIBLE);
//
//                    final View view = snackbar.getView();
//                    view.animate()
//                            .translationY(0)
//                            .alpha(1.0f)
//                            .setDuration(150)
//                            .setListener(new AnimatorListenerAdapter() {
//                                @Override
//                                public void onAnimationEnd(Animator animation) {
//                                    super.onAnimationEnd(animation);
//                                    view.setVisibility(View.VISIBLE);
//                                }
//                            });
//                }
//            }
//        };
//
//        new Thread() {
//            public void run() {
//                try {
//                    sleep(450);
//                    handler.sendEmptyMessage(0);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//
//    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
