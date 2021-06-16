package com.example.collapsingtoolbar.Activities;

import android.app.Activity;
import android.content.ComponentCallbacks2;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.collapsingtoolbar.Adapter.FragmentAdapter;
import com.example.collapsingtoolbar.Fragments.AllPhotosFragment;
import com.example.collapsingtoolbar.Fragments.AllVideosFragment;
import com.example.collapsingtoolbar.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity implements ComponentCallbacks2 {

    CollapsingToolbarLayout collapsingToolbar;
    EditText search;
    Thread Task;
    ViewPager2 pager2;
    FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Task = new Thread(this::init);
        Task.start();
        try { Task.join(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        pager2.setAdapter(adapter);


    }

    public void init()
    {
        collapsingToolbar = findViewById(R.id.colap_toolbar);
        final Typeface tf = ResourcesCompat.getFont(MainActivity.this, R.font.odin);
        collapsingToolbar.setCollapsedTitleTypeface(tf);
        collapsingToolbar.setExpandedTitleTypeface(tf);
        search = findViewById(R.id.search);
        pager2 = findViewById(R.id.frame);
        adapter = new FragmentAdapter(getSupportFragmentManager(),getLifecycle());

        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (pager2.getCurrentItem() == 0)
                {collapsingToolbar.setTitle("Photos");}
                else if (pager2.getCurrentItem() == 1)
                {collapsingToolbar.setTitle("Videos");}
            }
        });

    }
}