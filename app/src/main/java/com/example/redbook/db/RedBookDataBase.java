package com.example.redbook.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.redbook.db.dao.TalkCategoryDao;
import com.example.redbook.db.dao.TalkDao;
import com.example.redbook.db.entity.Talk;
import com.example.redbook.db.entity.TalkCategory;

@Database(entities = {Talk.class, TalkCategory.class}, version = 1,exportSchema = false)
public abstract class RedBookDataBase extends RoomDatabase {

    private static final String DB_NAME = "red_book";

    public RedBookDataBase() {
    }

    private static RedBookDataBase redBookDataBase;

    synchronized public static RedBookDataBase getRedBookDataBaseInstance(Context context) {
        if (null == redBookDataBase) {
            redBookDataBase = Room.databaseBuilder(context.getApplicationContext(), RedBookDataBase.class, DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return redBookDataBase;
    }

    public abstract TalkDao getTalkDao();

    public abstract TalkCategoryDao getTalkCategoryDao();
}
