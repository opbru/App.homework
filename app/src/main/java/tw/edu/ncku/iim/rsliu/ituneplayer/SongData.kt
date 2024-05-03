package tw.edu.ncku.iim.rsliu.ituneplayer

import android.graphics.Bitmap

data class SongData(
    val title: String, val cover: Bitmap? = null, val url: String = "")
