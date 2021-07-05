package com.example.collapsingtoolbar.Fetch;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.collapsingtoolbar.Adapter.PhotosAdapter;
import com.example.collapsingtoolbar.Model.AlbumModel;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class FetchAlbums {

    public static ArrayList<String> PhotosAlbums;
    public static ArrayList<String> VideosAlbums;
    private static AlbumModel albumModel;
    private static final ArrayList<AlbumModel> photo = new ArrayList<>();
    private static final ArrayList<AlbumModel> video = new ArrayList<>();

    private static Uri uri = null;
    private static Uri ThumbURI = null;
    private static long ID ;
    private static Cursor cursor;
    private static String[] projection = null;
    private static String orderBy = null;
    private static String name = null;
    private static final String TAG = "FetchAlbums";
    Activity activity;

    public FetchAlbums(Activity activity) {
        this.activity = activity;
        PhotosAlbums = new ArrayList<>();
        VideosAlbums = new ArrayList<>();
        albumModel =  new AlbumModel();
    }

    public ArrayList<AlbumModel> fetchPhotosAlbums() {
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        projection = new String[]{MediaStore.Images.Media._ID
                ,MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        orderBy = MediaStore.Images.Media.DATE_ADDED;
        cursor = activity.getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
        photo.clear();
        PhotosAlbums.clear();
        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
            if (name == null)
            { name = "Root"; }
            ID = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
            if (!PhotosAlbums.contains(name)) {
                ThumbURI = Uri.withAppendedPath(uri,""+ID);
                albumModel =  new AlbumModel();
                albumModel.setThumbURI(ThumbURI);
                albumModel.setAlbumName(name);
                photo.add(albumModel);
                PhotosAlbums.add(name);
            }
        }
        cursor.close();
        return photo;
    }


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
                if (name == null)
                { name = "Root"; }
                ID = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
                if (!VideosAlbums.contains(name)){
                    ThumbURI = Uri.withAppendedPath(uri,""+ID);
                    AlbumModel albumModel = new AlbumModel();
                    albumModel.setAlbumName(name);
                    albumModel.setThumbURI(ThumbURI);
                    video.add(albumModel);
                    VideosAlbums.add(name);
                }
            }
            cursor.close();
        return video;
    }
}
