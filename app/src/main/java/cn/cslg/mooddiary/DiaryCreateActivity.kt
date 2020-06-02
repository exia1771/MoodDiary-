package cn.cslg.mooddiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import cn.cslg.mooddiary.entity.Diary
import cn.cslg.mooddiary.model.DiaryViewModel
import cn.cslg.mooddiary.utils.ActivityCollector.decideWeatherImage
import cn.cslg.mooddiary.utils.ActivityCollector.getDatetime
import cn.cslg.mooddiary.utils.LogUtil
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_diary_create.*
import okhttp3.*
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.concurrent.thread

class DiaryCreateActivity : AppCompatActivity() {
    private lateinit var diaryViewModel: DiaryViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_create)
        initViews()
    }

    private fun initViews() {
        initToolbar()
        initVariable()
        getWeatherInfo()
    }

    private fun getWeatherInfo() {
        sendRequest(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LogUtil.w("DiaryCreateActivity-Info", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                response.body()?.let {
                    val weather = parseJSON(it.string())
                    LogUtil.w("DiaryCreateActivity-Info", weather)
                    runOnUiThread {
                        diaryWeather.setText(weather)
                    }
                }
            }
        })
    }

    private fun sendRequest(callback: okhttp3.Callback) {
        thread {
            val client = OkHttpClient()
            val cityName = "常熟"
            val request = Request.Builder()
                .url("http://api.k780.com:88/?app=weather.future&weaid=$cityName&&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json")
                .build()
            client.newCall(request).enqueue(callback)
        }
    }

    private fun initVariable() {
        diaryViewModel = ViewModelProvider(this).get(DiaryViewModel::class.java)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.diary_create_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.done -> {
                if (judgeEdit()) {
                    val date = getDatetime().first
                    val time = getDatetime().second
                    val diary = Diary(
                        diaryTitle.text.toString(),
                        diaryContent.text.toString(),
                        diaryMood.text.toString(),
                        diaryWeather.text.toString(),
                        "$date $time"
                    )
                    diary.weatherImage = decideWeatherImage(diary.weather)
                    thread {
                        diaryViewModel.insertDiary(diary)
                        finish()
                    }
                } else {
                    Toast.makeText(this, "请输入相应的内容!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return true
    }

    private fun judgeEdit(): Boolean {
        return !(diaryTitle.text.toString() == "" &&
                diaryMood.text.toString() == "" &&
                diaryWeather.text.toString() == "" &&
                diaryContent.text.toString() == "")
    }

    private fun parseJSON(jsonData: String): String {
        val parser = JsonParser()
        val elements = parser.parse(jsonData)
        val jsonObject = elements.asJsonObject
        val weatherJSONArray = jsonObject.getAsJsonArray("result")
        val weatherTodayJSON = weatherJSONArray[0].asJsonObject
        val weather = weatherTodayJSON.get("weather")
        return weather.asString
    }

}