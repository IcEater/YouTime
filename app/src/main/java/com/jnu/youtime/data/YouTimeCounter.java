package com.jnu.youtime.data;

public class YouTimeCounter {
    private long time;
    private String name;
    private String note;

    public long getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }
    YouTimeCounter(long time, String name, String note)
    {
        this.time=time;
        this.name=name;
        this.note=note;
    }

}
