package cn.cslg.mooddiary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.cslg.mooddiary.utils.ActivityCollector
import cn.cslg.mooddiary.utils.LogUtil

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.d("BaseActivity", this.localClassName + " onCreate")
        ActivityCollector.addActivity(this)
    }


    override fun onDestroy() {
        LogUtil.d("BaseActivity", "${this.localClassName} onDestroy")
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }
}