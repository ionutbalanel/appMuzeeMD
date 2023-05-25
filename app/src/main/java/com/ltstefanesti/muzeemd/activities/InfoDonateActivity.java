package com.ltstefanesti.muzeemd.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import com.ltstefanesti.muzeemd.R;

public class InfoDonateActivity extends AppCompatActivity {
    PDFView pdfView;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_donate);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        pdfView = findViewById(R.id.pdfView);
        pdfView.fromAsset("banner.pdf").load();
    }
}