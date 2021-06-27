package com.example.collapsingtoolbar.Activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.collapsingtoolbar.Fragments.AllPhotosFragment;
import com.example.collapsingtoolbar.R;

import static com.example.collapsingtoolbar.Fetch.FetchAlbums.PhotosAlbums;

public class AlbumPhotos extends AppCompatActivity {

    String TAG = "AlbumPhotos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_photos);
        int position = getIntent().getIntExtra("position",0);
        FragmentManager manager = getSupportFragmentManager();
        Log.d(TAG, "onCreate: PhotoAlbums.get "+PhotosAlbums.get(position));
        manager.beginTransaction().replace(R.id.frameAlbum, new AllPhotosFragment(PhotosAlbums.get(position))).commit();

    }
}