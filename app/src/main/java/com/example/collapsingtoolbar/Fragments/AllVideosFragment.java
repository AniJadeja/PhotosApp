package com.example.collapsingtoolbar.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collapsingtoolbar.Activities.FullPhoto;
import com.example.collapsingtoolbar.Activities.VideoPlay;
import com.example.collapsingtoolbar.Adapter.VideosAdapter;
import com.example.collapsingtoolbar.Model.VideoModel;
import com.example.collapsingtoolbar.R;
import com.example.collapsingtoolbar.utils.CustomRecyclerView;
import com.example.collapsingtoolbar.utils.FetchVideos;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.Objects;


public class AllVideosFragment extends Fragment implements VideosAdapter.OnVideoClickListner {


    CustomRecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<VideoModel> arrayList;
    FetchVideos fetchVideos;
    Thread Task;
    Parcelable State;
    String TAG = "AllVideosFragment";


    public AllVideosFragment() {}

    @Override
    public void onStart() {
        Log.d(TAG, "onStart: called...");
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
        Log.d(TAG, "onResume: callled...");
        super.onResume();
        Task = new Thread(this::fetchImages);
        Task.start();
        try {
            Task.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        State = layoutManager.onSaveInstanceState(); // Save RecyclerView State
    }

    @Override
    public void onStop() {
        super.onStop();
        fetchVideos.setFETCHED(false);
        Log.d(TAG, "onStop: called...");
        Log.d(TAG, "onStop: setFetched(false)");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_videos, container, false);
    }

    void init() {
        recyclerView = requireView().findViewById(R.id.recyclerview);
        layoutManager = new GridLayoutManager(getActivity(), 3);
        arrayList = new ArrayList<>();
        fetchVideos = new FetchVideos(getActivity());
    }



    private void fetchImages() {
        arrayList = fetchVideos.fetchVideos();
        new Thread(() -> requireActivity().runOnUiThread(() -> {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            VideosAdapter adapter = new VideosAdapter(requireActivity().getApplicationContext(), arrayList, getActivity(),this);
            recyclerView.setAdapter(adapter);
            layoutManager.onRestoreInstanceState(State); //Restore state
            Log.d("FetchImages(): ", " RecyclerView Adapter attached");
        })
        ).start();
    }

    @Override
    public void onClick(int position) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getActivity(),VideoPlay.class);
                intent.putExtra("Uri",arrayList.get(position).getUri().toString());
                startActivity(intent);
            }
        });

    }
}