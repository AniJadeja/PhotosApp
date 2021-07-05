package com.example.collapsingtoolbar.Model;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;

public class ImageModel implements Serializable {
    String uri;
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
