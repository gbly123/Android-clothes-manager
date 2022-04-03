package com.example.redbook.db.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.redbook.db.entity.Talk;
import com.example.redbook.db.entity.TalkCategory;

import java.util.List;

@Dao
public interface TalkCategoryDao {

    @Insert
    void insertCategory(List<TalkCategory> talkCategories);


    @Query("SELECT * FROM TalkCategory ORDER BY ID")
    LiveData<List<TalkCategory>> getAllCategory();
}
