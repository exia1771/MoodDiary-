package cn.cslg.mooddiary

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import cn.cslg.mooddiary.fragment.DiaryListFragment
import cn.cslg.mooddiary.fragment.ProfileFragment
import cn.cslg.mooddiary.model.DiaryViewModel
import cn.cslg.mooddiary.utils.ActivityCollector
import cn.cslg.mooddiary.utils.LogUtil
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val mediaPlayer = MediaPlayer()
    private val fragmentManager = supportFragmentManager
    private val refreshReceiver = RefreshReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        initVariable()
        initToolbar()
        initNavigation()
        restoreInfo()
        initFragment()
        initMediaPlayer()
    }

    private fun initVariable() {
        val intentFilter = IntentFilter()
        intentFilter.addAction("cn.cslg.mooddiary.REFRESH")
        registerReceiver(refreshReceiver, intentFilter)
    }

    private fun initMediaPlayer() {
        val assetManager = assets
        val fd = assetManager.openFd("bg_music.mp3")
        mediaPlayer.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
        mediaPlayer.prepare()
    }

    private fun restoreInfo() {
        val navView: NavigationView = findViewById(R.id.navView)
        val navHeaderLayout = navView.getHeaderView(0)
        val avatar: CircleImageView = navHeaderLayout.findViewById(R.id.avatar)
        val nameTextView: TextView = navHeaderLayout.findViewById(R.id.nameText)
        val emailTextView: TextView = navHeaderLayout.findViewById(R.id.emailText)
        val cityTextView: TextView = navHeaderLayout.findViewById(R.id.cityText)
        val prefs = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        if (prefs != null) {
            val savedUri = prefs.getString("avatar", "default")
            val name = prefs.getString("name", "未定义用户名")
            val email = prefs.getString("email", "未定义邮箱")
            val city = prefs.getString("city", "未定义城市名")
            ActivityCollector.setCityName(city)
            LogUtil.d("MainActivity-Restore", city.toString())
            if (savedUri != "default") {
                Glide.with(this).load(Uri.parse(savedUri.toString())).into(avatar)
            } else {
                Glide.with(this).load(R.drawable.nav_icon).into(avatar)
            }
            nameTextView.text = name
            emailTextView.text = email
            cityTextView.text = city
        }
    }

    private fun initFragment() {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(
            R.id.contentLayout,
            DiaryListFragment()
        )
        transaction.commit()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
        }
    }

    private fun initNavigation() {
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navProfile -> {
                    val transaction = fragmentManager.beginTransaction()
                    transaction.replace(
                        R.id.contentLayout,
                        ProfileFragment()
                    )
                    transaction.commit()
                }
                R.id.navQuit -> {
                    ActivityCollector.finish()
                }
                R.id.diaryList -> {
                    val transaction = fragmentManager.beginTransaction()
                    transaction.replace(
                        R.id.contentLayout,
                        DiaryListFragment()
                    )
                    transaction.commit()
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
            }
            R.id.appAbout -> {
                AlertDialog.Builder(this)
                    .setTitle("关于")
                    .setMessage("学号:092217117\n姓名:XXX\n程序功能介绍:记录日记并能够自动获得设定城市所在的当天天气情况(可自定义)\n程序版本:1.0.0")
                    .setNegativeButton("取消") { _, _ ->
                    }
                    .show()
            }
            R.id.appChangeBackground -> {
                changeBackground()
            }
            R.id.appChangeMusic -> {
                changeBackgroundMusic()
            }
            R.id.search -> {
                doSearch()
            }
        }
        return true
    }

    private fun changeBackground() {
        val backgroundList =
            listOf<Int>(
                R.drawable.bg_01, R.drawable.bg_02, R.drawable.bg_03,
                R.drawable.bg_04, R.drawable.bg_05
            )
        val index = (backgroundList.indices).random()
        coordinator.setBackgroundResource(backgroundList[index])
    }

    private fun changeBackgroundMusic() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        } else {
            mediaPlayer.pause()
        }
    }

    private fun doSearch() {
        val intent = Intent(this, DiarySearchActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()
        unregisterReceiver(refreshReceiver)
    }

    inner class RefreshReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            restoreInfo()
        }
    }

}