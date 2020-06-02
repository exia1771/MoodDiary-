package cn.cslg.mooddiary.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cn.cslg.mooddiary.entity.Diary

@Dao
interface DiaryDao {

    @Query("select * from Diary")
    fun loadAllDiaries(): LiveData<List<Diary>>

    @Insert
    fun insertDiary(diary: Diary)

    @Query("delete from Diary where id = :id")
    fun deleteDiaryById(id: Long)

    @Query("update Diary set title = :title, mood = :mood, weather = :weather, content =:content, weatherImage =:weatherImage where id= :id")
    fun updateDiaryById(
        id: Long,
        title: String,
        mood: String,
        weather: String,
        content: String,
        weatherImage: Int
    )

    @Query(
        "select * from Diary where title like :keyword or mood like :keyword or weather like :keyword or content like :keyword"
    )
    fun loadDiariesByKeyword(keyword: String): List<Diary>

}