package com.example.collapsingtoolbar.Fetch;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;

import com.example.collapsingtoolbar.Fragments.AllPhotosFragment;
import com.example.collapsingtoolbar.Model.ImageModel;
import com.example.collapsingtoolbar.Model.VideoModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingDeque;

public class FetchImages {

    Activity activity;
    Context context;
    private static ArrayList<ImageModel> arrayList;
    private static Uri uri = null;
    private static Cursor cursor;
    private static int column_index_data;
    private static String[] projection = null;
    private static String orderBy = null;
    private static String name;



    private static final String TAG = "Flow FetchImages";

    public FetchImages(Activity activity, Context context) {
        this.activity = activity;
        arrayList = new ArrayList<>();
        this.context = context;
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
            if (name == null) {
                name = "Root";
            }
            if (Album.equals("FETCH_ALL")) {
                Uri uriMedia = Uri.withAppendedPath(uri, "" + mediaId);
                ImageModel imageModel = new ImageModel();
                imageModel.setUri(uriMedia.toString());
                arrayList.add(imageModel);
            } else if (name.equals(Album)) {
                Uri uriMedia = Uri.withAppendedPath(uri, "" + mediaId);
                ImageModel imageModel = new ImageModel();
                imageModel.setUri(uriMedia.toString());
                arrayList.add(imageModel);
            }
        }

        cursor.close();

        return arrayList;
    }


    public ArrayList<ImageModel> fetchInitImages() {
        int i = 0;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        projection = new String[]{MediaStore.Images.Media._ID
                , MediaStore.Images.Media.DISPLAY_NAME
                ,MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        orderBy = MediaStore.Images.Media.DATE_ADDED;
        cursor = activity.getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);




        while (cursor.moveToNext()) {
            long mediaId = cursor.getLong(column_index_data);
            Uri uriMedia = Uri.withAppendedPath(uri, "" + mediaId);
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));

            Bitmap bitmap = null;
            try { Size size = new Size(100,100);
                bitmap = context.getContentResolver().loadThumbnail(uriMedia,size,null);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ContextWrapper cw = new ContextWrapper(context);
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            // Create imageDir
            File mypath=new File(directory,name+".bmp");
            File dir = context.getDir("Files",Context.MODE_PRIVATE);
            if (!dir.exists())
                dir.mkdir();







            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mypath);
                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    assert fos != null;
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }








            ImageModel imageModel = new ImageModel();
            imageModel.setUri(uriMedia.toString());
            arrayList.add(imageModel);
            i++;
            if (i==30)
            {break;}
        }
        cursor.close();
        return arrayList;
    }

}













