package com.example.collapsingtoolbar.utils;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.collapsingtoolbar.Model.VideoModel;

import java.util.ArrayList;

public class FetchVideos {

    Activity activity;
    private static ArrayList<VideoModel> arrayList;

    private static Uri uri = null;
    private static Cursor cursor;
    private static int column_index_data;
    private static String[] projection = null;
    private static String orderBy = null;

    public FetchVideos(Activity activity){
        this.activity =activity;
        arrayList = new ArrayList<>();
    }


    public ArrayList<VideoModel> fetchVideos() {

            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            projection = new String[]{MediaStore.Video.Media._ID};
            orderBy = MediaStore.Images.Media.DEFAULT_SORT_ORDER;


        cursor = activity.getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);

        new Thread(() -> {
            while (cursor.moveToNext()) {
                Log.d("FetchVideos(): ", " Started");
                long mediaId = cursor.getLong(column_index_data);
                Uri uriMedia = Uri.withAppendedPath(uri, "" + mediaId);
                VideoModel videoModel = new VideoModel();
                videoModel.setUri(uriMedia);
                arrayList.add(videoModel);
            }
            cursor.close();
            Log.d("FetchVideos(): ", " Ended");

        }).start();
        return arrayList;
    }

}
