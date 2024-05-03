package tw.edu.ncku.iim.rsliu.ituneplayer

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.URL

class iTuneXMLParser {
    val factory = XmlPullParserFactory.newInstance()
    val parser = factory.newPullParser()

    fun parseURL(url: String): List<SongData> {
        var title = ""
        var cover: Bitmap? = null
        var m4aURL = ""
        val songList = mutableListOf<SongData>()

        // parse xml doc
        try {
            val inputStream = URL(url).openStream()
            parser.setInput(inputStream, null)

            var eventType = parser.next()
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when(eventType) {
                    XmlPullParser.START_TAG -> {
                        if (parser.name == "title" && parser.depth == 3) {
                            title = parser.nextText()
                            Log.i("title", title)
                        } else if (parser.name == "im:image") {
                            if (parser.getAttributeValue(null, "height") == "170") {
                                val coverURL = parser.nextText()
                                Log.i("coverURL", coverURL)
                                val inputStream = URL(coverURL).openStream()
                                cover = BitmapFactory.decodeStream(inputStream)
                            }
                        } else if (parser.name == "link") {
                            if (parser.getAttributeValue(null, "type") == "audio/x-m4a") {
                                m4aURL = parser.getAttributeValue(null, "href")
                                Log.i ("m4aURL", m4aURL)
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "entry") {
                            songList.add(SongData(title, cover, m4aURL))
                        }
                    }
                }
                eventType = parser.next()
            }

        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return  songList
    }
}