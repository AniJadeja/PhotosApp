package com.example.collapsingtoolbar.Fetch;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.collapsingtoolbar.Model.AlbumModel;

import java.util.ArrayList;

public class FetchAlbums {

    private static ArrayList<String> PhotosAlbums;
    private static ArrayList<String> PrePhotosAlbums;
    private static ArrayList<String> VideosAlbums;
    private static ArrayList<String> PreVideosAlbums;

    public  static ArrayList<AlbumModel> photo = new ArrayList<>();
    public  static ArrayList<AlbumModel> video = new ArrayList<>();

    private static Uri uri = null;
    private static Uri ThumbURI = null;
    private static long ID ;
    private static Cursor cursor;
    private static String[] projection = null;
    private static String orderBy = null;
    private static Boolean FETCHEDP = false;
    private static Boolean FETCHEDV = false;
    private static String name = null;
    private static final String TAG = "FetchAlbums";
    Activity activity;

    public FetchAlbums(Activity activity) {
        this.activity = activity;
        PhotosAlbums = new ArrayList<>();
        VideosAlbums = new ArrayList<>();
    }

    public ArrayList<AlbumModel> fetchPhotosAlbums() {



        Log.d(TAG, "fetchPhotosAlbums: called...");
        if (!FETCHEDP || !PrePhotosAlbums.equals(PhotosAlbums)) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            projection = new String[]{MediaStore.Images.Media._ID
                    ,MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
            orderBy = MediaStore.Images.Media.DATE_ADDED;
            cursor = activity.getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

            while (cursor.moveToNext()) {
                Log.d(TAG, "fetchPhotosAlbums: fetch started...");
                name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                ID = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
                if (!PhotosAlbums.contains(name)) {
                    ThumbURI = Uri.withAppendedPath(uri,""+ID);
                    AlbumModel albumModel = new AlbumModel();
                    albumModel.setAlbumName(name);
                    Log.d("Photo Album Added", "AlbumName Set " + name);
                    albumModel.setThumbURI(ThumbURI);
                    Log.d("Photo Album Added", "ThumbURI set "+ThumbURI.toString());
                    photo.add(albumModel);
                    PhotosAlbums.add(name);

                }
            }
            cursor.close();
            FETCHEDP = true;
            PrePhotosAlbums = PhotosAlbums;
            Log.d(TAG, "fetchPhotosAlbums: fetch ended...");
        }
        return photo;
    }


    public ArrayList<AlbumModel> fetchVideosAlbums() {


        Log.d(TAG, "fetchVideosAlbums: called...");
        if (!FETCHEDV || !PreVideosAlbums.equals(VideosAlbums)) {
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            projection = new String[]{
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.BUCKET_DISPLAY_NAME};
            orderBy = MediaStore.Video.Media.DATE_ADDED;
            cursor = activity.getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

            while (cursor.moveToNext()) {
                Log.d(TAG, "fetchVideosAlbums: fetch started...");
                name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                ID = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
                if (!VideosAlbums.contains(name)){
                    ThumbURI = Uri.withAppendedPath(uri,""+ID);
                    AlbumModel albumModel = new AlbumModel();
                    albumModel.setAlbumName(name);
                    Log.d("Video Album Added", "AlbumName Set " + name);
                    albumModel.setThumbURI(ThumbURI);
                    Log.d("Video Album Added", "ThumbURI set "+ThumbURI.toString());
                    video.add(albumModel);
                    VideosAlbums.add(name);
                }
            }
            cursor.close();
            FETCHEDV = true;
            PreVideosAlbums = VideosAlbums;
            Log.d(TAG, "fetchVideosAlbums: fetch ended...");
        }
        return video;
    }

    public int VAlbumSize() {
        return VideosAlbums.size();
    }

    public int PAlbumSize() {
        return PhotosAlbums.size();
    }


    public void setFETCHEDP(Boolean fetched) {
        FETCHEDP = fetched;
    }

    public void setFETCHEDV(Boolean fetched) {
        FETCHEDV = fetched;
    }

}
