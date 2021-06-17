package com.example.collapsingtoolbar.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.collapsingtoolbar.Fragments.AllPhotosFragment;
import com.example.collapsingtoolbar.Model.ImageModel;
import com.example.collapsingtoolbar.R;
import com.example.collapsingtoolbar.utils.GlideApp;

import java.util.ArrayList;

public class FullPhoto extends AppCompatActivity {

    String TAG = "NewActivity";
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_photo);

        String s = getIntent().getStringExtra("uri");
        Log.d(TAG, "onCreate: Got Uri , "+s);

        imageView = findViewById(R.id.fullPhoto);

        GlideApp.with(getApplicationContext())
                .load(Uri.parse(s))
                .into(imageView);

    }

}