package com.bartz.mymusicplayer;

import android.graphics.Bitmap;

public class DataModel {

    String title;
    String author;
    String duration;
    Bitmap image;

    public DataModel(String title, String author, String duration, Bitmap image) {
        this.title = title;
        this.author = author;
        this.duration = duration;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getDuration() {
        return duration;
    }
}
