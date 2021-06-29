package com.example.collapsingtoolbar.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.collapsingtoolbar.Activities.FullPhoto;
import com.example.collapsingtoolbar.Adapter.PhotosAdapter;
import com.example.collapsingtoolbar.Model.ImageModel;
import com.example.collapsingtoolbar.R;
import com.example.collapsingtoolbar.Fetch.FetchImages;
import com.example.collapsingtoolbar.utils.CustomRecyclerView;

import java.util.ArrayList;
import java.util.SplittableRandom;


public class AllPhotosFragment extends Fragment implements PhotosAdapter.OnImageClickListner {


    public static CustomRecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ImageModel> arrayList;
    FetchImages fetchImages;
    Thread Task;
    public String Album = "FETCH_ALL";
    String TAG = "Flow AllPhotosFragment";


    public AllPhotosFragment() {
    }

    public AllPhotosFragment(String Album) {
        this.Album = Album;
    }

    Parcelable State;
    PhotosAdapter adapter;

    @Override
    public void onStart() {
        super.onStart();
        Task = new Thread(this::init);
        Task.start();
        try {
            Task.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().runOnUiThread(this::fetchImages);
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_photos, container, false);
    }

    public void init() {
        recyclerView = requireView().findViewById(R.id.recyclerviewPhotos);
        layoutManager = new GridLayoutManager(getActivity(), 4);
        fetchImages = new FetchImages(getActivity());
    }


    @Override
    public void onPause() {
        super.onPause();
        State = layoutManager.onSaveInstanceState(); // Save RecyclerView State
    }


    @SuppressLint("SetTextI18n")
    public void fetchImages() {
        arrayList = fetchImages.fetchImages(Album);
        TextView count = requireActivity().findViewById(R.id.count);
        count.setText(arrayList.size() + " Photos");
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new PhotosAdapter(requireActivity().getApplicationContext(), arrayList, getActivity(), this);
        recyclerView.setAdapter(adapter);
        layoutManager.onRestoreInstanceState(State); // Restore State
    }


    @Override
    public void onclick(int position) {
        Intent intent = new Intent(getActivity(), FullPhoto.class);
        intent.putExtra("uri", arrayList.get(position).getUri().toString());
        startActivity(intent);
    }
}