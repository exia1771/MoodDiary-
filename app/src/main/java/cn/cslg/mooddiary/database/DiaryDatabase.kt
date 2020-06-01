package cn.cslg.mooddiary.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cn.cslg.mooddiary.dao.DiaryDao
import cn.cslg.mooddiary.entity.Diary

@Database(version = 1, entities = [Diary::class])
abstract class DiaryDatabase : RoomDatabase() {

    abstract fun diaryDao(): DiaryDao

    companion object {
        private var instance: DiaryDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): DiaryDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(
                context.applicationContext,
                DiaryDatabase::class.java,
                "diary_database"
            )
                .build()
                .apply { instance = this }
        }


    }
}