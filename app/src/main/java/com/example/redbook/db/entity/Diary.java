package com.example.redbook.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Diary {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "diary_title")
    public String title;

    @ColumnInfo(name = "diray_content")
    public String content;

    @ColumnInfo(name = "diray_pic")
    public String picPath;

    @ColumnInfo(name = "diray_talk")
    public String talk;

    @ColumnInfo(name = "diray_time")
    public long time;

    public Diary(String title, String content, String picPath, String talk, long time) {
        this.title = title;
        this.content = content;
        this.picPath = picPath;
        this.talk = talk;
        this.time = time;
    }
}
