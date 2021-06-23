package com.example.collapsingtoolbar.Model;

import android.net.Uri;

public class VideoModel {
    Uri uri;
    long Duration;

    public long getDuration() {
        return Duration;
    }

    public void setDuration(long duration) {
        Duration = duration;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
