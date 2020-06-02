package cn.cslg.mooddiary.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.cslg.mooddiary.database.DiaryDatabase
import cn.cslg.mooddiary.entity.Diary
import cn.cslg.mooddiary.utils.LogUtil
import cn.cslg.mooddiary.utils.MoodDiaryApplication
import java.util.*

class DiaryViewModel : ViewModel() {

    private val context = MoodDiaryApplication.context
    private val diaryDao = DiaryDatabase.getDatabase(context).diaryDao()

    val diaryList: LiveData<List<Diary>> get() = _diaryList
    private var _diaryList: LiveData<List<Diary>> = MutableLiveData()


    val simpleDiary: LiveData<SimpleDiary> get() = _simpleDiary
    private var _simpleDiary = MutableLiveData<SimpleDiary>()

    fun loadAllDiaries() {
        _diaryList = diaryDao.loadAllDiaries()
    }

    fun insertDiary(diary: Diary) {
        diaryDao.insertDiary(diary)
    }

    fun deleteDiaryById(id: Long) {
        diaryDao.deleteDiaryById(id)
    }

    fun setSimpleDiary(simpleDiary: SimpleDiary) {
        _simpleDiary.value = simpleDiary
    }

    fun updateDiaryById(id: Long, simpleDiary: SimpleDiary) {
        diaryDao.updateDiaryById(
            id,
            simpleDiary.title,
            simpleDiary.mood,
            simpleDiary.weather,
            simpleDiary.content,
            simpleDiary.weatherImage
        )
    }

    fun loadDiariesByKeyword(keyword: String):List<Diary> {
        return diaryDao.loadDiariesByKeyword("%$keyword%")
    }
}

data class SimpleDiary(
    var title: String,
    var mood: String,
    var weather: String,
    var content: String,
    var weatherImage: Int
)