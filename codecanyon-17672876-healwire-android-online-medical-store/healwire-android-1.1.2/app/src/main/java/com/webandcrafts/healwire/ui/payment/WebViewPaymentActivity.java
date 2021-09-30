package com.webandcrafts.healwire.ui.payment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.webandcrafts.healwire.AppController;
import com.webandcrafts.healwire.NewBaseActivity;
import com.webandcrafts.healwire.R;
import com.webandcrafts.healwire.ui.payment_response.PaymentResponseActivity;
import com.webandcrafts.healwire.utils.SharedPreferenceHandler;

import java.util.HashMap;
import java.util.Map;

public class WebViewPaymentActivity extends NewBaseActivity {

    boolean loadingFinished = true;
    boolean redirect = false;
    SharedPreferenceHandler sharedPreferenceHandler;
    WebView webViewPayment;
    ProgressDialog progressDialog;
    private ImageView mBackIcon;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_payment);
        mBackIcon = findViewById(R.id.iv_back);
        sharedPreferenceHandler = new SharedPreferenceHandler();
        webViewPayment = findViewById(R.id.webViewPayment);
        webViewPayment.setWebViewClient(new MyWebViewClient());
        webViewPayment.getSettings().setJavaScriptEnabled(true);
        webViewPayment.getSettings().setSupportZoom(true);
        webViewPayment.getSettings().setBuiltInZoomControls(true);
        webViewPayment.getSettings().setDomStorageEnabled(true);

        Map<String, String> extraHeaders = new HashMap<String, String>();
        extraHeaders.put("Cookie", sharedPreferenceHandler.getCookie(getApplicationContext()));
        webViewPayment.loadUrl(AppController.BASE_PAYMENT_URL + "/" + String.valueOf(AppController.INVOICE_ID) + "/1", extraHeaders);

        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebViewPaymentActivity.this.finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });

    }

    public class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d("RES_STARTED", "SITE STARTED = " + url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            Log.d("RES_LOADED", "SITE FINISHED = " + url);

            if (AppController.PAYMENT_SUCCESS_URL.equals(url)) {
                AppController.PAYMENT_STATUS = "success";
                Intent intentHome = new Intent(getApplicationContext(), PaymentResponseActivity.class);
                startActivity(intentHome);
                WebViewPaymentActivity.this.finish();

            } else if (AppController.PAYMENT_FAILED_URL.equals(url)) {
                AppController.PAYMENT_STATUS = "failure";
                Intent intentHome = new Intent(getApplicationContext(), PaymentResponseActivity.class);
                startActivity(intentHome);
                WebViewPaymentActivity.this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        WebViewPaymentActivity.this.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        super.onBackPressed();
    }
}
