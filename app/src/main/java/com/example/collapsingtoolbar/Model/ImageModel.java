package com.example.collapsingtoolbar.Model;

import android.net.Uri;

import java.io.Serializable;

public class ImageModel implements Serializable {
    String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
