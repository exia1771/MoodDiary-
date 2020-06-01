package cn.cslg.mooddiary.utils

import android.app.Activity

object ActivityCollector {
    private val activities = ArrayList<Activity>()

    fun addActivity(activity: Activity) {
        activities.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activities.remove(activity)
    }

    fun finish() {
        for (activity in activities) {
            if (activity.isFinishing) {
                removeActivity(activity)
                continue
            }
            activity.finish()
        }
        activities.clear()
    }

}