package com.ltstefanesti.muzeemd.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.webkit.WebView;

import com.ltstefanesti.muzeemd.R;

public class MapsActivity extends AppCompatActivity {
    @SuppressLint({"MissingInflatedId", "SetJavaScriptEnabled", "SourceLockedOrientationActivity"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        WebView mWebView = findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("https://www.google.com/maps/d/embed?mid=100rpKPEfuQ_Gbg8I-QbiVDVCvGptnQQ&ehbc=2E312F&ll=46.797722806849315%2C28.42805681250001&z=7");
    }
}