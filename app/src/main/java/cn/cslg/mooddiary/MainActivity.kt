package cn.cslg.mooddiary

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import cn.cslg.mooddiary.fragment.DiaryListFragment
import cn.cslg.mooddiary.fragment.ProfileFragment
import cn.cslg.mooddiary.model.DiaryViewModel
import cn.cslg.mooddiary.utils.ActivityCollector
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var diaryViewModel: DiaryViewModel
    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        initToolbar()
        initNavigation()
        initFragment()
    }

    private fun initFragment(){
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
                    .setMessage("学号:092217117\n姓名:徐文武\n程序功能介绍:心情日记\n程序版本:1.0")
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
        Toast.makeText(this, "更改背景", Toast.LENGTH_LONG).show()
    }

    private fun changeBackgroundMusic() {
        Toast.makeText(this, "更改背景音乐", Toast.LENGTH_LONG).show()
    }

    private fun doSearch() {
        Toast.makeText(this, "进行搜索", Toast.LENGTH_LONG).show()
    }
}