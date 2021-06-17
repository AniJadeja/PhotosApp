package com.example.collapsingtoolbar.utils;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.collapsingtoolbar.Model.ImageModel;
import com.example.collapsingtoolbar.Model.VideoModel;

import java.util.ArrayList;

public class FetchImages {

    Activity activity;
    private static ArrayList<ImageModel> arrayList;
    private static ArrayList<ImageModel> previousList;

    private static Uri uri = null;
    private static Cursor cursor;
    private static int column_index_data;
    private static String[] projection = null;
    private static String orderBy = null;
    private static Boolean FETCHED = false;


    public FetchImages(Activity activity) {
        this.activity = activity;
        arrayList = new ArrayList<>();
        previousList = new ArrayList<>();
    }


    public ArrayList<ImageModel> fetchImages() {

        if(!FETCHED || !previousList.equals(arrayList)) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            projection = new String[]{MediaStore.Images.Media._ID};
            orderBy = MediaStore.Images.Media.DEFAULT_SORT_ORDER;
            cursor = activity.getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);

            new Thread(() -> {
                while (cursor.moveToNext()) {
                    Log.d("FetchImages(): ", " Started");
                    long mediaId = cursor.getLong(column_index_data);
                    Uri uriMedia = Uri.withAppendedPath(uri, "" + mediaId);
                    ImageModel imageModel = new ImageModel();
                    imageModel.setUri(uriMedia);
                    arrayList.add(imageModel);
                }
                cursor.close();
                Log.d("FetchImages(): ", " Ended");

            }).start();
            FETCHED = true;
            previousList = arrayList;
        }
        return arrayList;
    }


    public void setFETCHED(Boolean fetched) {
        FETCHED = fetched;
    }

}
