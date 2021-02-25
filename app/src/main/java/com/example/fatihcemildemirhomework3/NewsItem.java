package com.example.fatihcemildemirhomework3;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by atanaltay on 28/03/2017.
 */

public class NewsItem implements Serializable{

    private String title;
    private String text;

    //name of image in Drawable folder
    private String imagePath;
    private Date newsDate;
    private int id;
    private transient Bitmap bitmap;


    public NewsItem() {
    }
    public NewsItem(NewsItem copy)
    {
        Log.i("DET", "copy const called");
        this.title = copy.title;
        this.text = copy.text;
        this.imagePath = copy.imagePath;
        this.newsDate = copy.newsDate;
        this.id = copy.id;
        this.bitmap = copy.bitmap;
    }

    public NewsItem(int id, String title, String text, String imageId, Date newsDate, Bitmap bitmap) {
        this.title = title;
        this.text = text;
        this.imagePath = imageId;
        this.newsDate = newsDate;
        this.id = id;
        this.bitmap = bitmap;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageId() {
        return imagePath;
    }

    public void setImageId(String imageId) {
        this.imagePath = imageId;
    }


    public Date getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(Date newsDate) {
        this.newsDate = newsDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

}
