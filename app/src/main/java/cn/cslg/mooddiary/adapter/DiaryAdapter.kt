package cn.cslg.mooddiary.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.cslg.mooddiary.DiaryContentActivity
import cn.cslg.mooddiary.R
import cn.cslg.mooddiary.entity.Diary

class DiaryAdapter(val context: Context, private val diaryList: List<Diary>) :
    RecyclerView.Adapter<DiaryAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val diaryTitle: TextView = view.findViewById(R.id.diaryTitle)
        val diaryContent: TextView = view.findViewById(R.id.diaryContent)
        val diaryDatetime: TextView = view.findViewById(R.id.diaryDatetime)
        val diaryMood: TextView = view.findViewById(R.id.diaryMood)
        val diaryWeather: TextView = view.findViewById(R.id.diaryWeather)
        val diaryItemContainer:LinearLayout = view.findViewById(R.id.diaryItemContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.diary_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val diary = diaryList[viewHolder.adapterPosition]
            val intent = Intent(parent.context, DiaryContentActivity::class.java).apply {
                putExtra("id", diary.id)
                putExtra("title", diary.title)
                putExtra("mood", diary.mood)
                putExtra("weather", diary.weather)
                putExtra("content", diary.content)
                putExtra("weatherImage", diary.weatherImage)
            }
            parent.context.startActivity(intent)
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return diaryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val diary = diaryList[position]
        holder.apply {
            diaryContent.text = diary.content
            diaryDatetime.text = diary.datetime
            diaryMood.text = diary.mood
            diaryTitle.text = diary.title
            diaryWeather.text = diary.weather
            diaryItemContainer.setBackgroundResource(diary.weatherImage)
        }
    }
}