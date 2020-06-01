package cn.cslg.mooddiary.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.cslg.mooddiary.database.DiaryDatabase
import cn.cslg.mooddiary.entity.Diary
import cn.cslg.mooddiary.utils.MoodDiaryApplication

class DiaryViewModel : ViewModel() {

    private val context = MoodDiaryApplication.context
    private val diaryDao = DiaryDatabase.getDatabase(context).diaryDao()

    val diaryList: LiveData<List<Diary>> get() = _diaryList
    private var _diaryList: LiveData<List<Diary>> = MutableLiveData()


    fun loadAllDiaries() {
        _diaryList = diaryDao.loadAllDiaries()
    }

    fun insertDiary(diary: Diary) {
        diaryDao.insertDiary(diary)
    }

}