package com.example.collapsingtoolbar.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.collapsingtoolbar.Fragments.AllPhotosFragment;
import com.example.collapsingtoolbar.Fragments.AllVideosFragment;

public class FragmentAdapter extends FragmentStateAdapter {


    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    AllPhotosFragment photosFragment = new AllPhotosFragment();
    AllVideosFragment videosFragment = new AllVideosFragment();

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position)
        {
            case 1: return videosFragment;

        }

        return photosFragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
