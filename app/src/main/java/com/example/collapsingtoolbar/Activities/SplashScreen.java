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

    /*===============================================================   LIFE-CYCLE METHODS   ===============================================================*/

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


    /*===============================================================   UTILITY METHODS   ===============================================================*/

    public void askPermission()         //Requests permissions from the user.
    { ActivityCompat.requestPermissions(SplashScreen.this, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 1);}

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == 0) {

                            //Starting new Activity As the permissions are granted...

                new Handler(Looper.getMainLooper()).post(()->{
                        startActivity(new Intent(this,MainActivity.class));
                        overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
                        finishAffinity();
                });


            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_EXTERNAL_STORAGE")) {

                            //Permissions are denied once so, it is necessary to show dialog again

                dialog.set("Permission Required !!!","In order to work properly Storage Permission is Required");
                dialog.show("askPermission");

            } else {

                            //Permissions are denied for eternity, so no features depended on the permissions will work.

                dialog.set("Permission Denied !!!","You need to provide the permissions manually in order for the app to work properly...");
                dialog.show("null");
            }
        }
    }

    @Override
    public void choice(int choice) {            //Interface method to get if user has touched the positive button.
        if (choice == 1)
        {askPermission();}
    }
}