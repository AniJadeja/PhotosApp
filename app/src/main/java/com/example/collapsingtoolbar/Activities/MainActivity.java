package com.example.collapsingtoolbar.Activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.collapsingtoolbar.Adapter.FragmentAdapter;
import com.example.collapsingtoolbar.Adapter.PhotosAdapter;
import com.example.collapsingtoolbar.Fragments.AllPhotosFragment;
import com.example.collapsingtoolbar.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;

import java.lang.reflect.Field;



public class MainActivity extends AppCompatActivity{

    CollapsingToolbarLayout collapsingToolbar;
    EditText search;
    ViewPager2 pager2;
    FragmentAdapter adapter;
    Thread thread ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        thread = new Thread(this::init);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pager2.setAdapter(adapter);

        collapsingToolbar.setTitle("Albums");



        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (pager2.getCurrentItem() == 0)
                {
                    collapsingToolbar.setTitle("All Photos");
                }
                else if (pager2.getCurrentItem() == 1)
                {collapsingToolbar.setTitle("Albums");
                }
                else if (pager2.getCurrentItem() == 2)
                {collapsingToolbar.setTitle("All Videos");
                }
            }
        });

        thread = new Thread(this::setFling);
        thread.start();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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


    }

    private void setFling()
    {

        try {
            final Field recyclerViewField = ViewPager2.class.getDeclaredField("mRecyclerView");
            recyclerViewField.setAccessible(true);

            final RecyclerView recyclerView = (RecyclerView) recyclerViewField.get(pager2);

            final Field touchSlopField = RecyclerView.class.getDeclaredField("mTouchSlop");
            touchSlopField.setAccessible(true);

            final int touchSlop = (int) touchSlopField.get(recyclerView);
            touchSlopField.set(recyclerView, touchSlop * 4);//6 is empirical value
        } catch (Exception ignore) {
        }
    }

}