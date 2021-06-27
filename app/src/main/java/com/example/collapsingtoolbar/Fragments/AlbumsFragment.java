package com.example.collapsingtoolbar.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
    ArrayList<AlbumModel> PhotoX, VideoX;
    Thread Task ;
    ArrayList<String> photo;
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

    }

    @Override
    public void onStop() {
        super.onStop();
        /*photosAlbums.setFETCHEDP(false);
        VideosAlbums.setFETCHEDV(false);*/
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();

        requireActivity().runOnUiThread(() -> {
            PhotoX = photosAlbums.fetchPhotosAlbums();
            photo = photosAlbums.PAlbum();
            VideoX = VideosAlbums.fetchVideosAlbums();
            PhotosRV.setLayoutManager(new GridLayoutManager(getActivity(),3));
            VideosRV.setLayoutManager(new GridLayoutManager(getActivity(),3));
            count.setText(photosAlbums.PAlbumSize()+VideosAlbums.VAlbumSize()+getString(R.string._Albums));
            IACount.setText(PhotoX.size()+getString(R.string._Albums));
            VACount.setText(VideosAlbums.VAlbumSize()+getString(R.string._Albums));
            PhotoAdapter = new AlbumAdapter(getContext(),PhotoX,getActivity(),this);
            VideoAdapter = new AlbumAdapter(getContext(),VideoX,getActivity());
            PhotosRV.setAdapter(PhotoAdapter);
            VideosRV.setAdapter(VideoAdapter);

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
        photo = new ArrayList<>();


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