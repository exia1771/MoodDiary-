package cn.cslg.mooddiary

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.cslg.mooddiary.adapter.DiaryAdapter
import cn.cslg.mooddiary.model.DiaryViewModel
import cn.cslg.mooddiary.utils.LogUtil
import kotlinx.android.synthetic.main.activity_diary_search.*
import kotlin.concurrent.thread

class DiarySearchActivity : AppCompatActivity() {

    private lateinit var diaryViewModel: DiaryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_search)
        initViews()
    }


    private fun initViews() {
        initVariable()
        backBtn.setOnClickListener {
            finish()
        }
        observeEditText(this)
        clearTextBtn.setOnClickListener {
            searchEditText.setText("")
        }
    }

    private fun initVariable() {
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        diaryViewModel = ViewModelProvider(this).get(DiaryViewModel::class.java)
    }

    private fun observeEditText(context: Context) {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    recyclerView.adapter = null
                    clearTextBtn.setImageResource(R.drawable.ic_cannot_clear)
                    clearTextBtn.isEnabled = false
                } else {
                    thread {
                        val list = diaryViewModel.loadDiariesByKeyword(s.toString())
                        runOnUiThread {
                            LogUtil.d("DiarySearchActivity", list.toString())
                            recyclerView.adapter = DiaryAdapter(context, list)
                        }
                    }
                    clearTextBtn.setImageResource(R.drawable.ic_clear)
                    clearTextBtn.isEnabled = true
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }
}