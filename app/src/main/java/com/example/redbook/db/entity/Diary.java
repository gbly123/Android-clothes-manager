package com.example.redbook.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Diary implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "diary_title")
    public String title;

    @ColumnInfo(name = "diary_content")
    public String content;

    @ColumnInfo(name = "diary_pic")
    public String picPath;

    @ColumnInfo(name = "diary_talk")
    public String talk;

    @ColumnInfo(name = "diary_time")
    public long time;


    public Diary(String title, String content, String picPath, String talk, long time) {
        this.title = title;
        this.content = content;
        this.picPath = picPath;
        this.talk = talk;
        this.time = time;
    }
}
