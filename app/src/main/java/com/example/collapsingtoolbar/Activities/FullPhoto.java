package com.example.collapsingtoolbar.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.collapsingtoolbar.R;
import com.example.collapsingtoolbar.utils.GlideApp;

import static androidx.core.view.WindowInsetsCompat.Type.systemBars;
import static androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE;

public class FullPhoto extends AppCompatActivity {

    String TAG = "NewActivity";
    ImageView imageView;

    /*===============================================================   LIFE-CYCLE METHOD   ===============================================================*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_photo);

        String s = getIntent().getStringExtra("uri");           //Gets the uri passed by previous activity
        Log.d(TAG, "onCreate: Got Uri , "+s);

        imageView = findViewById(R.id.fullPhoto);

        GlideApp.with(getApplicationContext())
                .load(Uri.parse(s))
                .into(imageView);           //Loads the uri in the image view

    }

}