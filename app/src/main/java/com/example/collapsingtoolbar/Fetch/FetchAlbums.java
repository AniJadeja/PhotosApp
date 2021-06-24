package com.example.collapsingtoolbar.Fetch;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.collapsingtoolbar.Adapter.VideosAdapter;
import com.example.collapsingtoolbar.Model.ImageModel;

import java.util.ArrayList;

public class FetchAlbums {

    private ArrayList<String> PhotosAlbums;
    private ArrayList<String> PrePhotosAlbums;
    private ArrayList<String> VideosAlbums;
    private ArrayList<String> PreVideosAlbums;


    private static Uri uri = null;
    private static Cursor cursor;
    private static int column_index_data;
    private static String[] projection = null;
    private static String orderBy = null;
    private static Boolean FETCHED = false;
    private static String name;
    private static String TAG ="FetchAlbums";
    Activity activity;

    public FetchAlbums(Activity activity) {
        this.activity = activity;
        PhotosAlbums = new ArrayList<>();
        VideosAlbums = new ArrayList<>();
    }

    public ArrayList<String> fetchPhotosAlbums() {

        if (!FETCHED || !PrePhotosAlbums.equals(PhotosAlbums)) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            projection = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
            orderBy = MediaStore.Images.Media.DATE_ADDED;
            cursor = activity.getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);

            while (cursor.moveToNext()) {
                Log.d(TAG, "fetchPhotosAlbums: fetch started...");
                name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
            }
            cursor.close();
            FETCHED = true;
            PrePhotosAlbums = PhotosAlbums;
            Log.d(TAG, "fetchPhotosAlbums: fetch ended...");
        }
        return PhotosAlbums;
    }


    public void setFETCHED(Boolean fetched) {
        FETCHED = fetched;
    }

}
