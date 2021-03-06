package com.example.redbook.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.redbook.db.entity.Diary;

import java.util.List;

@Dao
public interface DiaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTalk(Diary diary);

    //    @Query("select * from weather_city_table where city_name LIKE '%' || :message || '%' order by case when city_name = :message then 1 when city_name LIKE :message || '%' then 2 when city_name LIKE '%' || :message || '%' then 3 when city_name LIKE '%' || :message  then 4 else 0 END")
    @Query("SELECT * FROM diary WHERE diary_title LIKE '%' || :key || '%' OR diary_talk LIKE '%' || :key || '%' OR diary_content LIKE '%' || :key || '%' ORDER BY CASE WHEN diary_title LIKE '%' || :key || '%' THEN 1 ELSE 10 END, CASE WHEN diary_talk LIKE '%' || :key || '%' THEN 2 ELSE 10 END, CASE WHEN diary_content LIKE '%' || :key || '%' THEN 3 ELSE 10 END")
    LiveData<List<Diary>> getDiaryByKey(String key);

    @Query("SELECT * FROM diary  ORDER BY diary_time DESC")
    LiveData<List<Diary>> getAllDiary();

    @Query("SELECT * FROM diary WHERE id =:id")
    LiveData<List<Diary>> getDiaryById(int id);

    @Delete
    void deleteDiary(Diary diary);
}
