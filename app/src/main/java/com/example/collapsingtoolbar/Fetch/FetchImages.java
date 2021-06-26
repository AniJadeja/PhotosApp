package com.example.collapsingtoolbar.Fetch;

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

    String name;

    public FetchImages(Activity activity) {
        this.activity = activity;
        arrayList = new ArrayList<>();
        previousList = new ArrayList<>();
    }


    public ArrayList<ImageModel> fetchImages() {

        if (!FETCHED || !previousList.equals(arrayList)) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            projection = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
            orderBy = MediaStore.Images.Media.DATE_ADDED;
            cursor = activity.getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);

            while (cursor.moveToNext()) {
                Log.d("FetchImages(): ", " Started");
                long mediaId = cursor.getLong(column_index_data);
                name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                Uri uriMedia = Uri.withAppendedPath(uri, "" + mediaId);
                ImageModel imageModel = new ImageModel();
                imageModel.setUri(uriMedia);
                arrayList.add(imageModel);
            }
            cursor.close();
            FETCHED = true;
            previousList = arrayList;
        }
        return arrayList;
    }


    public ArrayList<ImageModel> fetchImages(String Album) {

        if (!FETCHED || !previousList.equals(arrayList)) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            projection = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
            orderBy = MediaStore.Images.Media.DATE_ADDED;
            cursor = activity.getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);

            while (cursor.moveToNext()) {
                Log.d("FetchImages(): ", " Started");
                long mediaId = cursor.getLong(column_index_data);
                name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                if (name.equals(Album)) {
                    Uri uriMedia = Uri.withAppendedPath(uri, "" + mediaId);
                    ImageModel imageModel = new ImageModel();
                    imageModel.setUri(uriMedia);
                    arrayList.add(imageModel);
                }
            }
            cursor.close();
            FETCHED = true;
            previousList = arrayList;
        }
        return arrayList;
    }

    public void setFETCHED(Boolean fetched) {
        FETCHED = fetched;
    }

}
