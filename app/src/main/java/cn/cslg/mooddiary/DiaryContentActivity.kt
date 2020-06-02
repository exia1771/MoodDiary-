package cn.cslg.mooddiary

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.cslg.mooddiary.entity.Diary
import cn.cslg.mooddiary.model.DiaryViewModel
import cn.cslg.mooddiary.model.SimpleDiary
import cn.cslg.mooddiary.utils.ActivityCollector.decideWeatherImage
import cn.cslg.mooddiary.utils.LogUtil
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_diary_content.*

import kotlin.concurrent.thread

class DiaryContentActivity : BaseActivity() {

    private lateinit var diaryViewModel: DiaryViewModel
    private var doneMenuItem: MenuItem? = null

    private var id: Long = 0
    private var title: String? = ""
    private var mood: String? = ""
    private var weather: String? = ""
    private var content: String? = ""
    private var weatherImage: Int = 0
    private lateinit var simpleDiary: SimpleDiary

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_content)
        initViews()
    }

    private fun initViews() {
        initEditText()
        initToolbar()
        initVariable()
        textChanged()
    }

    private fun textChanged() {
        diaryTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                simpleDiary.title = s.toString()
                diaryViewModel.setSimpleDiary(simpleDiary)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        diaryMood.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                simpleDiary.mood = s.toString()
                diaryViewModel.setSimpleDiary(simpleDiary)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        diaryWeather.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                simpleDiary.weather = s.toString()
                simpleDiary.weatherImage = decideWeatherImage(simpleDiary.weather)
                diaryViewModel.setSimpleDiary(simpleDiary)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        diaryContent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                simpleDiary.content = s.toString()
                diaryViewModel.setSimpleDiary(simpleDiary)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }


    private fun initVariable() {
        diaryViewModel = ViewModelProvider(this).get(DiaryViewModel::class.java)
        diaryViewModel.setSimpleDiary(simpleDiary)
        diaryViewModel.simpleDiary.observe(this, Observer {
            if (judgeEdit()) {
                doneMenuItem?.isEnabled = false
                doneMenuItem?.setIcon(R.drawable.ic_cannot_done)
            } else {
                doneMenuItem?.isEnabled = true
                doneMenuItem?.setIcon(R.drawable.ic_done)
            }
        })
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        collapsingToolbarLayout.title = intent.getStringExtra("title")
        if (weatherImage != 0) {
            Glide.with(this).load(weatherImage).into(diaryBackgroundImage)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_content_menu, menu)
        doneMenuItem = menu?.findItem(R.id.done)
        doneMenuItem?.isEnabled = false
        doneMenuItem?.setIcon(R.drawable.ic_cannot_done)
        return true
    }

    private fun judgeEdit(): Boolean {
        return (simpleDiary.title == title &&
                simpleDiary.mood == mood &&
                simpleDiary.weather == weather &&
                simpleDiary.content == content)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.delete -> {
                AlertDialog.Builder(this)
                    .setMessage("确认删除此日记?")
                    .setNegativeButton("取消") { dialog, _ ->
                        dialog.cancel()
                    }
                    .setPositiveButton("确认") { dialog, _ ->
                        if (id.toInt() != 0) {
                            thread {
                                diaryViewModel.deleteDiaryById(intent.getLongExtra("id", 0))
                            }
                            dialog.dismiss()
                            finish()
                        }
                    }
                    .show()
            }
            R.id.done -> {
                thread {
                    diaryViewModel.updateDiaryById(id, simpleDiary)
                }
                title = simpleDiary.title
                mood = simpleDiary.mood
                weather = simpleDiary.weather
                content = simpleDiary.content
                diaryViewModel.setSimpleDiary(simpleDiary)
            }
        }
        return true
    }

    private fun initEditText() {
        intent.apply {
            id = getLongExtra("id", 0)
            title = getStringExtra("title")
            mood = getStringExtra("mood")
            weather = getStringExtra("weather")
            weatherImage = getIntExtra("weatherImage", 0)
            content = getStringExtra("content")
            simpleDiary = SimpleDiary(title!!, mood!!, weather!!, content!!, weatherImage)
            diaryTitle.setText(title)
            diaryMood.setText(mood)
            diaryWeather.setText(weather)
            diaryContent.setText(content)
        }
    }


}