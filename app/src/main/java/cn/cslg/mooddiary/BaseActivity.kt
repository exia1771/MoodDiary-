package cn.cslg.mooddiary

import android.Manifest
import android.app.Activity
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cn.cslg.mooddiary.utils.ActivityCollector
import cn.cslg.mooddiary.utils.LogUtil

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.d("BaseActivity", this.localClassName + " onCreate")
        ActivityCollector.addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.d("BaseActivity", "${this.localClassName} onDestroy")
        ActivityCollector.removeActivity(this)

    }
}