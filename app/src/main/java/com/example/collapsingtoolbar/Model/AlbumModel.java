package com.example.collapsingtoolbar.Model;

import android.net.Uri;

public class AlbumModel {
    String AlbumName;

    public String getAlbumName() {
        return AlbumName;
    }

    public void setAlbumName(String albumName) {
        AlbumName = albumName;
    }

    public Uri getThumbURI() {
        return ThumbURI;
    }

    public void setThumbURI(Uri thumbURI) {
        ThumbURI = thumbURI;
    }

    Uri ThumbURI;
}
