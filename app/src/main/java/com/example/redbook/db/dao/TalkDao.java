package com.example.redbook.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.redbook.db.entity.Talk;

import java.util.List;

@Dao
public interface TalkDao {
    @Insert
    void insertTalk(List<Talk> talks);

    @Query("SELECT * FROM talk WHERE cate_id == :categoryId ORDER BY ID DESC")
    LiveData<List<Talk>> getTalksByCategory(int categoryId);

    @Query("SELECT * FROM talk  ORDER BY ID")
    LiveData<List<Talk>> getAllTalks();
}
