package com.example.collapsingtoolbar.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.collapsingtoolbar.Fragments.AllPhotosFragment;
import com.example.collapsingtoolbar.Fragments.AllVideosFragment;
import com.example.collapsingtoolbar.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;

import static com.example.collapsingtoolbar.Fetch.FetchAlbums.PhotosAlbums;
import static com.example.collapsingtoolbar.Fetch.FetchAlbums.VideosAlbums;
import static com.example.collapsingtoolbar.Fragments.AlbumsFragment.IMAGE;
import static com.example.collapsingtoolbar.Fragments.AlbumsFragment.VIDEO;

public class AlbumPhotos extends AppCompatActivity {

    String TAG = "AlbumPhotos";
    MaterialToolbar toolbar;
    CollapsingToolbarLayout collapsingToolbar;
    ImageView back ;
    FragmentManager manager = getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_photos);
        int position = getIntent().getIntExtra("position",0);
        String Type = getIntent().getStringExtra("type");
        toolbar = findViewById(R.id.toolbar);
        collapsingToolbar = findViewById(R.id.colap_toolbar);

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> startActivity(new Intent(AlbumPhotos.this,MainActivity.class)));
        if (Type.equals(VIDEO)){
            manager.beginTransaction().replace(R.id.frameAlbum, new AllVideosFragment(VideosAlbums.get(position))).commit();
            collapsingToolbar.setTitle(VideosAlbums.get(position));
        }
        else if (Type.equals(IMAGE))
        {
            manager.beginTransaction().replace(R.id.frameAlbum, new AllPhotosFragment(PhotosAlbums.get(position))).commit();
            collapsingToolbar.setTitle(PhotosAlbums.get(position));
        }




    }
}