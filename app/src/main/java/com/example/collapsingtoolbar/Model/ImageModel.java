package com.example.collapsingtoolbar.Model;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;

public class ImageModel implements Serializable {           //Implements Serializable in order to serialize the data so it can be stored in internal storage.
    String uri;         //Uri is stored as string because Uri Class cannot be serialized for saving into localStorage.
    Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
