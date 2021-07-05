package com.example.collapsingtoolbar.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.collapsingtoolbar.Activities.AlbumPhotos;
import com.example.collapsingtoolbar.Adapter.PhotosAdapter;
import com.example.collapsingtoolbar.Adapter.PhotosAlbumAdapter;
import com.example.collapsingtoolbar.Adapter.VideoAlbumAdapter;
import com.example.collapsingtoolbar.Fetch.FetchAlbums;
import com.example.collapsingtoolbar.Model.AlbumModel;
import com.example.collapsingtoolbar.R;
import com.example.collapsingtoolbar.utils.CustomRecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class AlbumsFragment extends Fragment implements PhotosAlbumAdapter.OnAlbumClickListner, VideoAlbumAdapter.OnAlbumClickListner {

    private static CustomRecyclerView PhotosRV, VideosRV;
    private static ArrayList<AlbumModel> photoX, videoX;

    private PhotosAlbumAdapter PhotoAdapter;
    private VideoAlbumAdapter VideoAdapter;
    private FetchAlbums albums;
    private TextView seeAllPhotoAlbums, seeAllVideoAlbums, videoTag, imageTag,count;
    private Thread Task;

    private static final String TAG = "Flow AlbumsFragment";
    public static final String VIDEO = "Video";
    public static final String IMAGE = "Image";
    private static boolean photoPressed = false, videoPressed = true;


    /*===============================================================   CONSTRUCTOR   ===============================================================*/

    public AlbumsFragment(){}

    /*===============================================================   LIFE CYCLE METHODS   ===============================================================*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_albums, container, false);
        Task = new Thread(() -> init(v));
        Task.start();
        try {
            Task.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //fetches initial 3 imageAlbums,videoAlbums and shows in recycler view to reduce the load on onCreateView
        requireActivity().runOnUiThread(() -> {
            photoX = albums.fetchInitPhotosAlbums();
            Log.d(TAG, "onCreateView: received photoX " + photoX.size());
            PhotoAdapter = new PhotosAlbumAdapter(getContext(), photoX, getActivity(), this);
            PhotosRV.setAdapter(PhotoAdapter);

            videoX = albums.fetchInitVideosAlbums();
            VideoAdapter = new VideoAlbumAdapter(getContext(), videoX, getActivity(), this);
            VideosRV.setAdapter(VideoAdapter);
            setButton();

        });
        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        //loads all the albums and saves into arrayList
        count.setText(albums.fetchPhotosAlbums().size() + albums.fetchVideosAlbums().size() + " Albums");
        actionPhotos(true, true);
        actionVideos(true, true);
        //Half visible is a parameter to show only number of albums to screen instead of all the albums.
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    /*===============================================================   UTILITY METHODS   ===============================================================*/

    //This method detects VIEW ALL button click and expands the album list or collapse it
    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    private void setButton() {
        //This is support method definition to act with View All button for video albums
        seeAllPhotoAlbums.setOnTouchListener((v, event) ->
        {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    seeAllPhotoAlbums.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_dark));
                    break;
                case MotionEvent.ACTION_UP:
                    if (photoPressed) {
                        actionPhotos(true, true);
                        actionVideos(true, true);
                        seeAllPhotoAlbums.setText("VIEW ALL");
                        seeAllPhotoAlbums.setTextColor(ContextCompat.getColor(requireContext(), R.color.red));
                        photoPressed = false;
                    } else if (!photoPressed) {
                        actionPhotos(true, false);
                        actionVideos(false, false);
                        seeAllPhotoAlbums.setText("HIDE");
                        seeAllPhotoAlbums.setTextColor(ContextCompat.getColor(requireContext(), R.color.red));
                        photoPressed = true;
                    }
                    break;
                default:
                    break;
            }
            return true;
        });

        //This is support method definition to act with View All button for video albums
        seeAllVideoAlbums.setOnTouchListener((v, event) ->
        {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    seeAllVideoAlbums.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_dark));
                    break;
                case MotionEvent.ACTION_UP:

                    if (videoPressed) {
                        actionVideos(true, true);
                        actionPhotos(true, true);
                        seeAllVideoAlbums.setText("VIEW ALL");
                        seeAllVideoAlbums.setTextColor(ContextCompat.getColor(requireContext(), R.color.red));
                        videoPressed = false;
                    } else if (!videoPressed) {
                        actionVideos(true, false);

                        seeAllVideoAlbums.setText("HIDE");
                        seeAllVideoAlbums.setTextColor(ContextCompat.getColor(requireContext(), R.color.red));
                        videoPressed = true;
                    }

                    seeAllVideoAlbums.setTextColor(ContextCompat.getColor(requireContext(), R.color.red));
                    break;
                default:
                    break;
            }
            return true;
        });
    }


    private void init(View view) {
        count = requireActivity().findViewById(R.id.count);
        PhotosRV = view.findViewById(R.id.ImagesAlbum);
        VideosRV = view.findViewById(R.id.VideosAlbum);
        albums = new FetchAlbums(getActivity());
        seeAllPhotoAlbums = view.findViewById(R.id.AllImageAlbums);
        seeAllVideoAlbums = view.findViewById(R.id.AllVideoAlbums);
        imageTag = view.findViewById(R.id.ImageTag);
        videoTag = view.findViewById(R.id.VideoTag);
        VideosRV.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        VideosRV.setHasFixedSize(true);
        PhotosRV.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        PhotosRV.setHasFixedSize(true);
    }


    private void actionVideos(boolean VISIBLE, boolean HALF_VISIBLE) {
        if (!VISIBLE) {
            VideosRV.setAdapter(null);
            videoTag.setVisibility(View.GONE);
            seeAllVideoAlbums.setVisibility(View.GONE);
        } else if (VISIBLE) {
            seeAllVideoAlbums.setText("VIEW ALL");
            videoPressed = false;
            videoX = albums.fetchVideosAlbums();
            Log.d(TAG, "actionVideos: videoX " + videoX.size());
            if (HALF_VISIBLE && videoX.size() > 3) {
                videoX.subList(3, videoX.size()).clear();
            }

            VideoAdapter = new VideoAlbumAdapter(getContext(), videoX, getActivity(), this);
            VideosRV.setAdapter(VideoAdapter);
            videoTag.setVisibility(View.VISIBLE);
            seeAllVideoAlbums.setVisibility(View.VISIBLE);
        }
    }

    private void actionPhotos(boolean VISIBLE, boolean HALF_VISIBLE) {
        if (!VISIBLE) {
            PhotosRV.setAdapter(null);
            imageTag.setVisibility(View.GONE);
            seeAllPhotoAlbums.setVisibility(View.GONE);
        } else if (VISIBLE) {
            seeAllPhotoAlbums.setText("VIEW ALL");
            photoPressed = false;
            photoX = albums.fetchPhotosAlbums();
            Log.d(TAG, "actionPhotos: photoX " + photoX.size());
            if (HALF_VISIBLE && photoX.size() > 3) {
                photoX.subList(3, photoX.size()).clear();
            }

            PhotoAdapter = new PhotosAlbumAdapter(getContext(), photoX, getActivity(), this);
            PhotosRV.setAdapter(PhotoAdapter);
            imageTag.setVisibility(View.VISIBLE);
            seeAllPhotoAlbums.setVisibility(View.VISIBLE);
        }
    }

    /*===============================================================   INTERFACE METHODS   ===============================================================*/

    @Override
    public void onclick(int position, String type) {
        Intent i = new Intent(getActivity(), AlbumPhotos.class);
        i.putExtra("position", position);
        i.putExtra("type", type);
        requireActivity().startActivity(i);
    }

}


























