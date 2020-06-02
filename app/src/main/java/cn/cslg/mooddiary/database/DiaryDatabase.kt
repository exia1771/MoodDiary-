package cn.cslg.mooddiary.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import cn.cslg.mooddiary.dao.DiaryDao
import cn.cslg.mooddiary.entity.Diary

@Database(version = 3, entities = [Diary::class])
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
                .fallbackToDestructiveMigration()
                .build()
                .apply { instance = this }
        }


    }
}