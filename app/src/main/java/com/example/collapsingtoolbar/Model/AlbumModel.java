package com.example.collapsingtoolbar.Model;

import android.net.Uri;

import java.io.Serializable;

public class AlbumModel implements Serializable {
    String AlbumName;

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

    String ThumbURI;
}
