package cn.cslg.mooddiary.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Diary(
    val title: String,
    val content: String,
    val mood: String,
    val weather: String,
    val datetime: String
) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}