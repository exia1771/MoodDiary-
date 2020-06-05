package cn.cslg.mooddiary.utils

import android.app.Activity
import cn.cslg.mooddiary.R
import java.util.*
import kotlin.collections.ArrayList

object ActivityCollector {
    private val activities = ArrayList<Activity>()
    private var cityName: String? = ""


    fun addActivity(activity: Activity) {
        activities.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activities.remove(activity)
    }

    fun finish() {
        for (activity in activities) {
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
        activities.clear()
    }

    fun getDatetime(): Pair<String, String> {
        val calendar = Calendar.getInstance()
        val date = "${calendar.get(Calendar.YEAR)}-" +
                "${calendar.get(Calendar.MONTH) + 1}-" +
                "${calendar.get(Calendar.DATE)}"
        val time = "${calendar.get(Calendar.HOUR_OF_DAY)}:" +
                "${calendar.get(Calendar.MINUTE)}:" +
                "${calendar.get(Calendar.SECOND)}"
        return Pair(date, time)
    }

    fun decideWeatherImage(weather: String) =
        when {
            weather.contains("云") -> {
                R.drawable.bg_cloudy
            }
            weather.contains("雨") -> {
                R.drawable.bg_rain
            }
            weather.contains("风") -> {
                R.drawable.bg_wind
            }
            weather.contains("雪") -> {
                R.drawable.bg_snow
            }
            weather.contains("雾") -> {
                R.drawable.bg_fog
            }
            else -> {
                R.drawable.bg_clear_day
            }
        }

    fun setCityName(city: String?) {
        cityName = city
    }

    fun getCityName(): String? {
        return cityName
    }
}