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

    /*===============================================================   CONSTRUCTOR   ===============================================================*/

    public FetchVideos(Activity activity) {
        this.activity = activity;
        arrayList = new ArrayList<>();
    }

    /*===============================================================   UTILITY METHODS   ===============================================================*/


                //This method returns all the videos from SharedInternalStorage depending upon the AlbumName

    public ArrayList<VideoModel> fetchVideos(String Album) {
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            projection = new String[]{MediaStore.Video.Media._ID
                    ,MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Video.Media.DURATION};
            orderBy = MediaStore.Video.Media.DATE_ADDED;            //Sorting the videos by date
            cursor = activity.getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);
            arrayList.clear();          //Before executing or adding the videos into list, It is necessary that list is clear to ensure there will be no duplicate entries.
            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                if (name == null)
                {
                    name = "Root";          //Android returns null for root of the SharedInternal Storage which needs be corrected to root.
                }
                if (Album.equals("FETCH_ALL"))          //If album choice is "FETCH_ALL" it will add videos to list regardless of album name.
                {
                    long mediaId = cursor.getLong(column_index_data);
                    long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    Uri uriMedia = Uri.withAppendedPath(uri, "" + mediaId);
                    VideoModel videoModel = new VideoModel();
                    videoModel.setDuration(duration);
                    videoModel.setUri(uriMedia.toString());
                    arrayList.add(videoModel);
                }
                else if (name.equals(Album))            //If album choice is specific it will add videos to list only the name of album of image and choice , both are same.
                {
                    long mediaId = cursor.getLong(column_index_data);
                    long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    Uri uriMedia = Uri.withAppendedPath(uri, "" + mediaId);
                    VideoModel videoModel = new VideoModel();
                    videoModel.setDuration(duration);
                    videoModel.setUri(uriMedia.toString());
                    arrayList.add(videoModel);}
            }
            cursor.close();
        return arrayList;
    }

                //This methods returns first 18 videos from recent videos.

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
                videoModel.setUri(uriMedia.toString());
                arrayList.add(videoModel);
                i++;
                if (i==18)
                {break;}
            }
            cursor.close();
        return arrayList;
    }
}




























