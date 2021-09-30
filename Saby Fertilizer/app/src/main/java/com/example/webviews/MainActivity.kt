package com.example.webviews

import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private val web: WebView? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }

     val web : WebView = findViewById<WebView>(R.id.webview);

        val myset: WebSettings = web.getSettings();
        web.settings.setJavaScriptEnabled(true);
        val mWebViewClient = WebViewClient()
        web.setWebViewClient(mWebViewClient)

        web.settings.setAllowFileAccess(true);
        web.settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        web.settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        web.settings.setAppCacheEnabled(true);
        web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        myset.setDomStorageEnabled(true);
        myset.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        myset.setUseWideViewPort(true);
        myset.setSavePassword(true);
        myset.setSaveFormData(true);
        myset.setEnableSmoothTransition(true);

        web.loadUrl("http://192.168.43.180/Ecommerce%20Site%20PHP/ecommerce/index.php");

    }
    override fun onBackPressed() {
        if(web!= null && web.canGoBack())
            web.goBack();// if there is previous page open it
        else
            super.onBackPressed();//if there is no previous page, close app
    }
}

/* override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
      if (event.action==KeyEvent.ACTION_DOWN){
          switch (keyCode){
           case   KeyEvent.KEYCODE_BACK;
              if (web.canGoBack()){
                  web.goBack();
              }else{
                  finish();
              }
              return true;
          }
      }
      return super.onKeyDown(keyCode, event)
  }*/