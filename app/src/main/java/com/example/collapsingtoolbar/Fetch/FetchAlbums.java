package com.example.collapsingtoolbar.Fetch;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.view.TintableBackgroundView;

import com.example.collapsingtoolbar.Adapter.PhotosAdapter;
import com.example.collapsingtoolbar.Model.AlbumModel;

import java.util.ArrayList;
import java.util.stream.Collectors;

//This class helps user to get all the MediaAlbums from sharedInternalStorage.

public class FetchAlbums {

    public static ArrayList<String> PhotosAlbums;
    public static ArrayList<String> VideosAlbums;
    private static AlbumModel albumModel;
    private static final ArrayList<AlbumModel> photo = new ArrayList<>();
    private static final ArrayList<AlbumModel> video = new ArrayList<>();

    private static Uri uri = null;
    private static Uri ThumbURI = null;
    private static long ID;
    private static Cursor cursor;
    private static String[] projection = null;
    private static String orderBy = null;
    private static String name = null;
    private static final String TAG = "FetchAlbums";
    Activity activity;

    /*===============================================================   CONSTRUCTOR   ===============================================================*/

    public FetchAlbums(Activity activity) {
        this.activity = activity;
        PhotosAlbums = new ArrayList<>();
        VideosAlbums = new ArrayList<>();
        albumModel = new AlbumModel();
    }

    /*===============================================================   UTILITY METHODS   ===============================================================*/

                //This methods is to fetch all the PhotosAlbums

    public ArrayList<AlbumModel> fetchPhotosAlbums() {
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        projection = new String[]{MediaStore.Images.Media._ID
                , MediaStore.Images.Media.BUCKET_DISPLAY_NAME};         //Media_ID and BUCKET_DISPLAY_NAME are used to set album name
        orderBy = MediaStore.Images.Media.DATE_ADDED;
        cursor = activity.getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
        photo.clear();          //Before saving anything to album list, It should be empty, to ensure that there is no duplicate entries.
        PhotosAlbums.clear();
        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
            if (name == null) {
                name = "Root";          //The root of the file explorer and root of the SharedInternalStorage is not defined in android, so android returns null as a name.
                                        //Which needs be corrected as root.
            }
            ID = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
            if (!PhotosAlbums.contains(name)) {         //Function to add thumbnail uri and album name will only run if the album name is not repeated.
                ThumbURI = Uri.withAppendedPath(uri, "" + ID);
                albumModel = new AlbumModel();
                albumModel.setThumbURI(ThumbURI.toString());            //Adds uri of the thumbnail to list.
                albumModel.setAlbumName(name);          //Adds album name to list.
                photo.add(albumModel);
                PhotosAlbums.add(name);         //PhotosAlbums is a String ArrayList which decides if the album name is repeated or not. Without position it's hard to execute photo.get().getAlbumName and compare it to all the previous entries. So, a different arrayList of String type is taken to simplify the process.
            }
        }
        cursor.close();
        return photo;
    }

                //This method returns only first 3 PhotosAlbums from the SharedInternalStorage.

    public ArrayList<AlbumModel> fetchInitPhotosAlbums() {
        int i = 0;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        projection = new String[]{MediaStore.Images.Media._ID
                ,MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        orderBy = MediaStore.Images.Media.DATE_ADDED;
        cursor = activity.getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
        photo.clear();
        PhotosAlbums.clear();
        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
            if (name == null) {
                name = "Root";
            }
            ID = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
            if (!PhotosAlbums.contains(name)) {
                ThumbURI = Uri.withAppendedPath(uri, "" + ID);
                albumModel = new AlbumModel();
                albumModel.setThumbURI(ThumbURI.toString());
                albumModel.setAlbumName(name);
                photo.add(albumModel);
                PhotosAlbums.add(name);
                i++;
            }
            if (i == 3)
                break;
        }
        cursor.close();
        return photo;
    }

                //This method returns first 3 VideosAlbums from the SharedInternalStorage.

    public ArrayList<AlbumModel> fetchInitVideosAlbums() {
        int i = 0;
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        projection = new String[]{
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME};
        orderBy = MediaStore.Video.Media.DATE_ADDED;
        cursor = activity.getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
        video.clear();
        VideosAlbums.clear();
        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
            if (name == null) {
                name = "Root";
            }
            ID = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
            if (!VideosAlbums.contains(name)) {
                ThumbURI = Uri.withAppendedPath(uri, "" + ID);
                AlbumModel albumModel = new AlbumModel();
                albumModel.setAlbumName(name);
                albumModel.setThumbURI(ThumbURI.toString());
                video.add(albumModel);
                VideosAlbums.add(name);
                i++;
            }

            if (i == 3)
                break;

        }
        cursor.close();
        return video;
    }

                //This method returns all the VideosAlbums from the SharedInternalStorage.

    public ArrayList<AlbumModel> fetchVideosAlbums() {
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        projection = new String[]{
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME};
        orderBy = MediaStore.Video.Media.DATE_ADDED;
        cursor = activity.getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
        video.clear();
        VideosAlbums.clear();
        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
            if (name == null) {
                name = "Root";
            }
            ID = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
            if (!VideosAlbums.contains(name)) {
                ThumbURI = Uri.withAppendedPath(uri, "" + ID);
                AlbumModel albumModel = new AlbumModel();
                albumModel.setAlbumName(name);
                albumModel.setThumbURI(ThumbURI.toString());
                video.add(albumModel);
                VideosAlbums.add(name);
            }
        }
        cursor.close();
        return video;
    }
}
