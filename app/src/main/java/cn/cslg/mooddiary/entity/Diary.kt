package cn.cslg.mooddiary.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
data class Diary(
    val title: String,
    val content: String,
    val mood: String,
    val weather: String,
    val datetime: String,
    var weatherImage: Int = 0
) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

}