package cn.cslg.mooddiary.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.cslg.mooddiary.R
import cn.cslg.mooddiary.adapter.DiaryAdapter
import cn.cslg.mooddiary.entity.Diary
import cn.cslg.mooddiary.model.DiaryViewModel
import cn.cslg.mooddiary.utils.MoodDiaryApplication
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.concurrent.thread

class DiaryListFragment : Fragment() {

    private lateinit var diaryViewModel: DiaryViewModel
    private lateinit var fragView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragView = inflater.inflate(R.layout.diary_list_frag, container, false)
        initVariable()
        diaryCRUD()
        return fragView
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun initVariable() {
        val recyclerView: RecyclerView = fragView.findViewById(R.id.recyclerView)
        diaryViewModel = ViewModelProvider(this).get(DiaryViewModel::class.java)
        diaryViewModel.loadAllDiaries()
        diaryViewModel.diaryList.observe(activity!!, androidx.lifecycle.Observer { list ->
            val diaryAdapter = DiaryAdapter(MoodDiaryApplication.context, list)
            recyclerView.adapter = diaryAdapter
        })

        val layoutManager = LinearLayoutManager(MoodDiaryApplication.context)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
    }

    private fun diaryCRUD() {
        val addDiaryButton:FloatingActionButton = fragView.findViewById(R.id.addDiaryButton)
        addDiaryButton.setOnClickListener {
            val date = getDatetime().first
            val time = getDatetime().second
            thread {
                diaryViewModel.insertDiary(
                    Diary(
                        "标题1",
                        "内容2".repeat(100),
                        "心情3",
                        "天气4",
                        "$date $time"
                    )
                )
            }
        }
    }

    private fun getDatetime(): Pair<String, String> {
        val calendar = Calendar.getInstance()
        val date = "${calendar.get(Calendar.YEAR)}-" +
                "${calendar.get(Calendar.MONTH) + 1}-" +
                "${calendar.get(Calendar.DATE)}"
        val time = "${calendar.get(Calendar.HOUR_OF_DAY)}:" +
                "${calendar.get(Calendar.MINUTE)}:" +
                "${calendar.get(Calendar.SECOND)}"
        return Pair(date, time)
    }

}