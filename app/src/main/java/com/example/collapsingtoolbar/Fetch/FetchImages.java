package com.example.collapsingtoolbar.Fetch;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.collapsingtoolbar.Model.ImageModel;
import com.example.collapsingtoolbar.Model.VideoModel;

import java.util.ArrayList;
import java.util.concurrent.BlockingDeque;

public class FetchImages {

    Activity activity;
    private static ArrayList<ImageModel> arrayList;

    private static Uri uri = null;
    private static Cursor cursor;
    private static int column_index_data;
    private static String[] projection = null;
    private static String orderBy = null;
    String name;

    public FetchImages(Activity activity) {
        this.activity = activity;
        arrayList = new ArrayList<>();
    }


    public ArrayList<ImageModel> fetchImages(String Album) {

            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            projection = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
            orderBy = MediaStore.Images.Media.DATE_ADDED;
            cursor = activity.getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);
            arrayList.clear();
            while (cursor.moveToNext()) {
                long mediaId = cursor.getLong(column_index_data);
                name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                if (Album.equals(""))
                {
                Uri uriMedia = Uri.withAppendedPath(uri, "" + mediaId);
                ImageModel imageModel = new ImageModel();
                imageModel.setUri(uriMedia);
                arrayList.add(imageModel);
                }else if (name.equals(Album))
                {
                    Uri uriMedia = Uri.withAppendedPath(uri, "" + mediaId);
                    ImageModel imageModel = new ImageModel();
                    imageModel.setUri(uriMedia);
                    arrayList.add(imageModel);
                }
            }
            cursor.close();
        return arrayList;
    }


/*    public ArrayList<ImageModel> fetchImages(String Album) {

            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            projection = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
            orderBy = MediaStore.Images.Media.DATE_ADDED;
            cursor = activity.getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);

            while (cursor.moveToNext()) {
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
        return arrayList;
    }*/
}
