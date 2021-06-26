package com.example.collapsingtoolbar.Fetch;

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
    private static ArrayList<VideoModel> previousList;

    private static Uri uri = null;
    private static Cursor cursor;
    private static int column_index_data;
    private static String[] projection = null;
    private static String orderBy = null;
    private static Boolean FETCHED = false;
    private static String name;

    public FetchVideos(Activity activity) {
        this.activity = activity;
        arrayList = new ArrayList<>();
        previousList = new ArrayList<>();
    }


    public ArrayList<VideoModel> fetchVideos() {

        if (!FETCHED || !previousList.equals(arrayList)) {
            Log.d("FetchVideos()", "fetchVideos: Initiated... ");
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            projection = new String[]{MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.DURATION};
            orderBy = MediaStore.Video.Media.DEFAULT_SORT_ORDER;
            cursor = activity.getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);
            while (cursor.moveToNext()) {
                Log.d("FetchVideos(): ", " Started");
                long mediaId = cursor.getLong(column_index_data);
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                Uri uriMedia = Uri.withAppendedPath(uri, "" + mediaId);
                VideoModel videoModel = new VideoModel();
                videoModel.setDuration(duration);
                videoModel.setUri(uriMedia);
                arrayList.add(videoModel);
            }
            cursor.close();
            FETCHED = true;
            previousList = arrayList;
        }
        return previousList;
    }


    public ArrayList<VideoModel> fetchVideos(String Album) {

        if (!FETCHED || !previousList.equals(arrayList)) {
            Log.d("FetchVideos()", "fetchVideos: Initiated... ");
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            projection = new String[]{MediaStore.Video.Media._ID
                    ,MediaStore.Video.Media.DURATION
                    ,MediaStore.Video.Media.BUCKET_DISPLAY_NAME};
            orderBy = MediaStore.Video.Media.DEFAULT_SORT_ORDER;
            cursor = activity.getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);
            while (cursor.moveToNext()) {
                Log.d("FetchVideos(): ", " Started");
                long mediaId = cursor.getLong(column_index_data);
                name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                if (name.equals(Album))
                {
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                Uri uriMedia = Uri.withAppendedPath(uri, "" + mediaId);
                VideoModel videoModel = new VideoModel();
                videoModel.setDuration(duration);
                videoModel.setUri(uriMedia);
                arrayList.add(videoModel);}
            }
            cursor.close();
            FETCHED = true;
            previousList = arrayList;
        }
        return previousList;
    }


    public void setFETCHED(Boolean fetched) {
        FETCHED = fetched;
    }
}




























