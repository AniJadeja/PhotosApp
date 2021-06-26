package com.example.collapsingtoolbar.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


public class AllPhotosFragment extends Fragment implements PhotosAdapter.OnImageClickListner {


    public static CustomRecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ImageModel> arrayList;
    FetchImages fetchImages;
    Thread Task;
    Bundle bundle;
    String TAG ="AllPhotosFragment";


    public AllPhotosFragment() {}
    Parcelable State;
    View view;
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
        Task = new Thread(this::fetchImages);
        Task.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        fetchImages.setFETCHED(false);
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


    public void fetchImages() {
        new Thread(() -> requireActivity().runOnUiThread(() -> {
            arrayList = fetchImages.fetchImages();
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            adapter = new PhotosAdapter(requireActivity().getApplicationContext(), arrayList, getActivity(),this);
            Log.d("getItemCount"," "+adapter.getItemCount());
            recyclerView.setAdapter(adapter);
            layoutManager.onRestoreInstanceState(State); // Restore State
            Log.d("FetchImages(): ", " RecyclerView Adapter attached");
        })
        ).start();
    }


    @Override
    public void onclick(int position) {
        Intent intent = new Intent(getActivity(), FullPhoto.class);
        intent.putExtra("uri",arrayList.get(position).getUri().toString());
        startActivity(intent);
    }
}