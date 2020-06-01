package cn.cslg.mooddiary.utils

import android.app.Application
import android.content.Context

class MoodDiaryApplication : Application() {

    companion object {
        lateinit var context:Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}