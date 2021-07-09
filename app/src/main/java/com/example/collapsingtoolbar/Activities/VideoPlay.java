package com.example.collapsingtoolbar.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.collapsingtoolbar.R;
import com.example.collapsingtoolbar.utils.GlideApp;

public class VideoPlay extends AppCompatActivity {

    VideoView videoView;
    String TAG = "VideoActivity";
    Boolean VIDEO_PLAYING = false;
    MediaController mediaController;
    ImageView thumbnail;


    /*===============================================================   LIFE-CYCLE METHOD   ===============================================================*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);


        String s = getIntent().getStringExtra("Uri");           //Getting the uri of the video passed by previous activity
        Log.d(TAG, "onCreate: Got Uri , " + s);

        thumbnail = findViewById(R.id.thumbnail);
        GlideApp.with(getApplicationContext())
                .load(Uri.parse(s))
                .into(thumbnail);           //Load the uri into thumbnail
        videoView = findViewById(R.id.video);
        videoView.setVideoURI(Uri.parse(s));

        mediaController = new MediaController(this);            //Media controller is set in order to play and pause the video.


    }

    /*===============================================================   UTILITY METHOD   ===============================================================*/

    public void videoPlay(View view) {
        thumbnail.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.start();
    }
}