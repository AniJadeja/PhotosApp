package com.example.collapsingtoolbar.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collapsingtoolbar.Adapter.AlbumAdapter;
import com.example.collapsingtoolbar.Fetch.FetchAlbums;
import com.example.collapsingtoolbar.Model.AlbumModel;
import com.example.collapsingtoolbar.R;
import com.example.collapsingtoolbar.utils.CustomRecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class AlbumsFragment extends Fragment {

    AlbumAdapter PhotoAdapter,VideoAdapter;
    ArrayList<AlbumModel> PAlbum;
    FetchAlbums PhotosAlbums,VideosAlbums;
    CustomRecyclerView PhotosRV,VideosRV;
    ArrayList<String> Photo,Video;
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


    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();

        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            Photo = PhotosAlbums.fetchPhotosAlbums();
            Video = VideosAlbums.fetchVideosAlbums();
            count.setText(PhotosAlbums.PAlbumSize()+VideosAlbums.VAlbumSize()+getString(R.string._Albums));
            IACount.setText(PhotosAlbums.PAlbumSize()+getString(R.string._Albums));
            VACount.setText(VideosAlbums.VAlbumSize()+getString(R.string._Albums));
            PhotoAdapter = new AlbumAdapter(Photo,getActivity());
            VideoAdapter = new AlbumAdapter(Video,getActivity());
            PhotosRV.setAdapter(PhotoAdapter);
            VideosRV.setAdapter(VideoAdapter);
        });


    }

    private void init()
    {
        count = Objects.requireNonNull(getActivity()).findViewById(R.id.count);
        IACount = requireView().findViewById(R.id.ImageAlbumCount);
        VACount = requireView().findViewById(R.id.VideoAlbumCount);
        PhotosRV = requireView().findViewById(R.id.ImagesAlbum);
        VideosRV = requireView().findViewById(R.id.VideosAlbum);
        Photo = new ArrayList<>();
        Video = new ArrayList<>();
        PAlbum = new ArrayList<>();
        PhotosAlbums = new FetchAlbums(getActivity());
        VideosAlbums = new FetchAlbums(getActivity());
        PhotosRV.setLayoutManager(new GridLayoutManager(getActivity(),3));
        VideosRV.setLayoutManager(new GridLayoutManager(getActivity(),3));

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_albums, container, false);
    }
}