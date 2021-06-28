package com.example.collapsingtoolbar.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LongDef;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.collapsingtoolbar.Activities.AlbumPhotos;
import com.example.collapsingtoolbar.Adapter.AlbumAdapter;
import com.example.collapsingtoolbar.Fetch.FetchAlbums;
import com.example.collapsingtoolbar.Model.AlbumModel;
import com.example.collapsingtoolbar.R;
import com.example.collapsingtoolbar.utils.CustomRecyclerView;

import java.util.ArrayList;

public class AlbumsFragment extends Fragment implements AlbumAdapter.OnAlbumClickListner {

    AlbumAdapter PhotoAdapter,VideoAdapter;
    FetchAlbums photosAlbums,VideosAlbums;
    CustomRecyclerView PhotosRV,VideosRV;
    ArrayList<AlbumModel> photoX, videoX;
    ArrayList<AlbumModel> PrePhoto, PreVideo;
    Thread Task ;
    TextView count,IACount,VACount;
    String TAG = "AlbumsFragment";
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
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: Not Added ");
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Added on Resume()");
        requireActivity().runOnUiThread(() -> {
            photoX = photosAlbums.fetchPhotosAlbums();
            PhotosRV.setLayoutManager(new GridLayoutManager(getActivity(),3));
            IACount.setText(photoX.size()+getString(R.string._Albums));
            PhotoAdapter = new AlbumAdapter(getContext(),photoX,getActivity(),this);
            PhotosRV.setAdapter(PhotoAdapter);



            videoX = VideosAlbums.fetchVideosAlbums();
            VideosRV.setLayoutManager(new GridLayoutManager(getActivity(),3));
            VACount.setText(videoX.size()+getString(R.string._Albums));
            VideoAdapter = new AlbumAdapter(getContext(),videoX,getActivity());
            VideosRV.setAdapter(VideoAdapter);
            count.setText(photoX.size()+videoX.size()+getString(R.string._Albums));
        });


    }

    private void init()
    {
        count = requireActivity().findViewById(R.id.count);
        IACount = requireView().findViewById(R.id.ImageAlbumCount);
        VACount = requireView().findViewById(R.id.VideoAlbumCount);
        PhotosRV = requireView().findViewById(R.id.ImagesAlbum);
        VideosRV = requireView().findViewById(R.id.VideosAlbum);
        photosAlbums = new FetchAlbums(getActivity());
        VideosAlbums = new FetchAlbums(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_albums, container, false);
    }

    @Override
    public void onclick(int position) {
        Intent i =new Intent(getActivity(), AlbumPhotos.class);
        i.putExtra("position",position);
        getActivity().startActivity(i);
    }



}