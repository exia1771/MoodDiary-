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

}