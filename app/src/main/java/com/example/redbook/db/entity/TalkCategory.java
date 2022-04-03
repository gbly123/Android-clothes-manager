package com.example.redbook.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TalkCategory {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "cate_name")
    public String name;

    public TalkCategory(String name) {
        this.name = name;
    }
}
