package com.example.collapsingtoolbar.Fetch;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.collapsingtoolbar.Fragments.AllVideosFragment;
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
    private static String name;

    public FetchVideos(Activity activity) {
        this.activity = activity;
        arrayList = new ArrayList<>();
    }


    public ArrayList<VideoModel> fetchVideos(String Album) {
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            projection = new String[]{MediaStore.Video.Media._ID
                    ,MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Video.Media.DURATION};
            orderBy = MediaStore.Video.Media.DATE_ADDED;
            cursor = activity.getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);
            arrayList.clear();
            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                if (name == null)
                {
                    name = "Root";
                }
                if (Album.equals("FETCH_ALL"))
                {
                    long mediaId = cursor.getLong(column_index_data);
                    long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    Uri uriMedia = Uri.withAppendedPath(uri, "" + mediaId);
                    VideoModel videoModel = new VideoModel();
                    videoModel.setDuration(duration);
                    videoModel.setUri(uriMedia);
                    arrayList.add(videoModel);
                }
                else if (name.equals(Album))
                {
                    long mediaId = cursor.getLong(column_index_data);
                    long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    Uri uriMedia = Uri.withAppendedPath(uri, "" + mediaId);
                    VideoModel videoModel = new VideoModel();
                    videoModel.setDuration(duration);
                    videoModel.setUri(uriMedia);
                    arrayList.add(videoModel);}
            }
            cursor.close();
        return arrayList;
    }

    public ArrayList<VideoModel> fetchInitVideos() {
        int i = 0;
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            projection = new String[]{MediaStore.Video.Media._ID,MediaStore.Video.Media.DURATION};
            orderBy = MediaStore.Video.Media.DATE_ADDED;
            cursor = activity.getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);
            arrayList.clear();
            while (cursor.moveToNext()) {
                long mediaId = cursor.getLong(column_index_data);
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                Uri uriMedia = Uri.withAppendedPath(uri, "" + mediaId);
                VideoModel videoModel = new VideoModel();
                videoModel.setDuration(duration);
                videoModel.setUri(uriMedia);
                arrayList.add(videoModel);
                i++;
                if (i==18)
                {break;}
            }
            cursor.close();
        return arrayList;
    }
}




























