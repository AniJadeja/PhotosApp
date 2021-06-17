package com.example.collapsingtoolbar.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.collapsingtoolbar.Model.VideoModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class FetchVideos {

    Activity activity;
    private static ArrayList<VideoModel> arrayList;
    private static ArrayList<VideoModel> previousList;

    private static Uri uri = null;
    private static Cursor cursor;
    private static int column_index_data;
    private static String[] projection = null;
    private static String orderBy = null;
    private static Boolean FETCHED = false;


    public FetchVideos(Activity activity){
        this.activity = activity;
        arrayList = new ArrayList<>();
        previousList = new ArrayList<>();
    }


    public ArrayList<VideoModel> fetchVideos() {

        if(!FETCHED || !previousList.equals(arrayList)) {
            Log.d("FetchVideos()", "fetchVideos: Initiated... ");
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            projection = new String[]{MediaStore.Video.Media._ID};
            orderBy = MediaStore.Video.Media.DEFAULT_SORT_ORDER;
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
            FETCHED = true;
            previousList = arrayList;
        }
        return previousList;
    }


    public void setFETCHED(Boolean fetched)
    {FETCHED =fetched;}
}




























