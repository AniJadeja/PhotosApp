package com.example.collapsingtoolbar.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.collapsingtoolbar.Adapter.PhotosAlbumAdapter;
import com.example.collapsingtoolbar.Adapter.VideoAlbumAdapter;
import com.example.collapsingtoolbar.Fetch.FetchAlbums;
import com.example.collapsingtoolbar.Model.AlbumModel;
import com.example.collapsingtoolbar.R;
import com.example.collapsingtoolbar.utils.CustomRecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class AlbumsFragment extends Fragment implements PhotosAlbumAdapter.OnAlbumClickListner, VideoAlbumAdapter.OnAlbumClickListner {

    PhotosAlbumAdapter PhotoAdapter;
    VideoAlbumAdapter VideoAdapter;
    FetchAlbums albums;
    CustomRecyclerView PhotosRV, VideosRV;
    ArrayList<AlbumModel> photoX, videoX;
    TextView seeAllPhotoAlbums, seeAllVideoAlbums, videoTag, imageTag;

    boolean photoPressed = false, videoPressed = true;
    public static final String VIDEO = "Video";
    public static final String IMAGE = "Image";
    Thread Task;
    TextView count;
    String TAG = "AlbumsFragment";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_albums, container, false);
    }


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
    public void onResume() {
        super.onResume();
        requireActivity().runOnUiThread(() -> {
            count.setText(albums.fetchPhotosAlbums().size()+albums.fetchVideosAlbums().size()+" Albums");
            actionPhotos(true,true);
            actionVideos(true,true);
            setButton();

        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    private void setButton() {
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


    private void init() {
        count = requireActivity().findViewById(R.id.count);
        PhotosRV = requireView().findViewById(R.id.ImagesAlbum);
        VideosRV = requireView().findViewById(R.id.VideosAlbum);
        albums = new FetchAlbums(getActivity());
        seeAllPhotoAlbums = requireView().findViewById(R.id.AllImageAlbums);
        seeAllVideoAlbums = requireView().findViewById(R.id.AllVideoAlbums);
        imageTag = requireView().findViewById(R.id.ImageTag);
        videoTag = requireView().findViewById(R.id.VideoTag);

    }




    @Override
    public void onclick(int position, String type) {
        Intent i = new Intent(getActivity(), AlbumPhotos.class);
        i.putExtra("position", position);
        i.putExtra("type", type);
        requireActivity().startActivity(i);
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
            VideosRV.setLayoutManager(new GridLayoutManager(getActivity(), 3));
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
            PhotosRV.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            PhotoAdapter = new PhotosAlbumAdapter(getContext(), photoX, getActivity(), this);
            PhotosRV.setAdapter(PhotoAdapter);
            imageTag.setVisibility(View.VISIBLE);
            seeAllPhotoAlbums.setVisibility(View.VISIBLE);
        }
    }

}


























