package com.example.collapsingtoolbar.Model;

import android.net.Uri;

import java.io.Serializable;

public class VideoModel implements Serializable {           //Implements Serializable in order to serialize the data so it can be stored in internal storage.
    String uri;         //Uri is stored as string because Uri Class cannot be serialized for saving into localStorage.
    long Duration;

    public long getDuration() {
        return Duration;
    }

    public void setDuration(long duration) {
        Duration = duration;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
