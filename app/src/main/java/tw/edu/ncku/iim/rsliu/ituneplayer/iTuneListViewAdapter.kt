package tw.edu.ncku.iim.rsliu.ituneplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class iTuneListViewAdapter: BaseAdapter() {
    var songs = listOf<SongData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getCount(): Int {
        return songs.size
    }

    override fun getItem(position: Int): Any {
        return songs[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong() // or return 0L
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(parent!!.context)
        val itemLayout = inflater.inflate(R.layout.itune_list_item, null)
        val textView = itemLayout.findViewById<TextView>(R.id.textView)
        val imageView = itemLayout.findViewById<ImageView>(R.id.imageView)
        textView.text = songs[position].title
        imageView.setImageBitmap(songs[position].cover)

        return itemLayout
    }
}