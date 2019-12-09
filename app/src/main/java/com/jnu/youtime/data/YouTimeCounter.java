package com.jnu.youtime.data;

import android.graphics.Bitmap;

public class YouTimeCounter {
    private long time;
    private Bitmap image;
    private String title;
    private String note;
    public YouTimeCounter(){}
    public long getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }
    public Bitmap getImage()
    {
        return image;
    }

    public YouTimeCounter(long time, String title, String note, Bitmap image)
    {
        this.time=time;
        this.title=title;
        this.note=note;
        this.image=image;
    }

}
