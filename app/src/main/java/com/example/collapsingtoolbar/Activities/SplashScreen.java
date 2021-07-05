package com.example.collapsingtoolbar.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.collapsingtoolbar.R;
import com.example.collapsingtoolbar.utils.Dialog;


public class SplashScreen extends AppCompatActivity implements Dialog.DialogCallBack {

    Dialog dialog;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
        dialog = new Dialog(SplashScreen.this);
        dialog.setCallBack(this);
        textView = findViewById(R.id.colap_toolbar);
        askPermission();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void askPermission()
    { ActivityCompat.requestPermissions(SplashScreen.this, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 1);}

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == 0) {
                //permissions granted

                new Handler(Looper.getMainLooper()).postDelayed(()->{
                        startActivity(new Intent(this,MainActivity.class));
                        overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
                        finishAffinity();
                },80);


            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_EXTERNAL_STORAGE")) {
               //perissions denied once
                dialog.set("Permission Required !!!","In order to work properly Storage Permission is Required");
                dialog.show("askPermission");

            } else {
                //permissions denied for eternity
                dialog.set("Permission Denied !!!","You need to provide the permissions manually in order for the app to work properly...");
                dialog.show("null");
            }
        }
    }

    @Override
    public void choice(int choice) {
        if (choice == 1)
        {askPermission();}
    }
}