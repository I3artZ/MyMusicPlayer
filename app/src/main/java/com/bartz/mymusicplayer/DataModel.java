package com.bartz.mymusicplayer;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DataModel implements Serializable {
    int index;
    String title;
    String author;
    Integer duration;
    String displayName;
    Bitmap image;
    String path;

    public DataModel(int index, String title, String author, String duration, String displayName, String path) {
        this.index = index;
        this.title = title;
        this.author = author;
        this.duration = Integer.valueOf(duration);
        //this.image = image;
        this.displayName = displayName;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

//    public Bitmap getImage() {
//        return image;
//    }

    public String getPath(){
        return path;
    }

    public String getDuration() {
        return createTimeLabel(duration);
    }

    public Integer getIntDuration() {
        return duration;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static String createTimeLabel(int time){
        String label = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        label = min + ":";
        if (sec < 10) label += "0";
        label += sec;

        return label;
    }

    public int getIndex() {
        return index;
    }

}

