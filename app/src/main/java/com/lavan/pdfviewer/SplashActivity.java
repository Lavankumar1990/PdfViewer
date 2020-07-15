package com.lavan.pdfviewer;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import java.io.File;



public class SplashActivity extends AppCompatActivity {


    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setVersionName();
        loadHandler();
    }


    private void loadHandler() {
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        checkBackground();
                    }
                }, 500);
    }

    private void checkBackground() {
        startActivity(new Intent(SplashActivity.this, ShowPdfListActivity.class));
        finish();
    }


    private void setVersionName() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            ((TextView) findViewById(R.id.tv_version_name)).setText("Version : " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

}
