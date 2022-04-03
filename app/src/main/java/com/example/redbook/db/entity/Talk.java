package com.example.redbook.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Talk {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "talk_name")
    public String name;

    @ColumnInfo(name = "cate_id")
    public int categoryId;

    public Talk(String name, int categoryId) {
        this.name = name;
        this.categoryId = categoryId;
    }
}
