package com.example.collapsingtoolbar.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.collapsingtoolbar.Fragments.AlbumsFragment;
import com.example.collapsingtoolbar.Fragments.AllPhotosFragment;
import com.example.collapsingtoolbar.Fragments.AllVideosFragment;

public class FragmentAdapter extends FragmentStateAdapter {



    /*===============================================================   CONSTRUCTOR   ===============================================================*/

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    /*===============================================================   OVERRIDEN METHODS   ===============================================================*/

    @NonNull
    @Override
    public Fragment createFragment(int position) {          //Choosing which fragment should be returned on the page number.

        switch (position)
        {
            case 1: return new AlbumsFragment();
            case 2: return new AllVideosFragment();

        }

        return new AllPhotosFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }           //Returning total how many pages are there.

}
