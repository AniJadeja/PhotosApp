package com.example.collapsingtoolbar.Model;

import android.net.Uri;

import java.io.Serializable;

public class AlbumModel implements Serializable {           //Implements Serializable in order to serialize the data so it can be stored in internal storage.
    String AlbumName;
    String ThumbURI;            //Uri is stored as string because Uri Class cannot be serialized for saving into localStorage.
    public String getAlbumName() {
        return AlbumName;
    }

    public void setAlbumName(String albumName) {
        AlbumName = albumName;
    }

    public String getThumbURI() {
        return ThumbURI;
    }

    public void setThumbURI(String thumbURI) {
        ThumbURI = thumbURI;
    }


}
