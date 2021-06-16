package com.example.collapsingtoolbar.Activities;

import android.app.Activity;
import android.content.ComponentCallbacks2;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.collapsingtoolbar.Fragments.AllPhotosFragment;
import com.example.collapsingtoolbar.Fragments.AllVideosFragment;
import com.example.collapsingtoolbar.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity implements ComponentCallbacks2 {
    CollapsingToolbarLayout collapsingToolbar;
    EditText search;
    InputMethodManager imm;
    Thread Task;
    SmoothBottomBar bottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Task = new Thread(this::init);
        Task.start();
        try {
            Task.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AllPhotosFragment photosFragment = new AllPhotosFragment();
        AllVideosFragment videosFragment = new AllVideosFragment();

        replace(photosFragment);
        bottomBar.setOnItemSelectedListener(i -> {
            switch (i){
                case 0:
                    replace(photosFragment);
                    break;
                case 1:
                    replace(videosFragment);
                    break;
            }
            return true;
        });
    }
    public void clearSearch() {
        if (search.hasFocus()) {
            search.clearFocus();
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }
    public void init()
    {
        collapsingToolbar = findViewById(R.id.colap_toolbar);
        final Typeface tf = ResourcesCompat.getFont(MainActivity.this, R.font.odin);
        collapsingToolbar.setCollapsedTitleTypeface(tf);
        collapsingToolbar.setExpandedTitleTypeface(tf);
        search = findViewById(R.id.search);
        imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        bottomBar = findViewById(R.id.bottomBar);
    }

    private void replace(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame,fragment);
        transaction.commit();
    }

}