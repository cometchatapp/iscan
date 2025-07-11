package com.arteriatech.ss.msec.iscan.v4.b4u;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.arteriatech.ss.msec.iscan.v4.R;

public class WebViewB4UActivity extends AppCompatActivity {
    WebView webView;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_b4u_web_view);

    }

    @Override
    protected void onResume() {
        super.onResume();
      //  MSFAApplication.setAnalytics(this,MSFAApplication.SP_UID,this.getClass().getSimpleName(),"B4U");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishActivity(416);
    }
}