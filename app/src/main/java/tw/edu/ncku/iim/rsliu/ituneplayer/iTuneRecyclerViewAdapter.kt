package tw.edu.ncku.iim.rsliu.ituneplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tw.edu.ncku.iim.rsliu.ituneplayer.databinding.ItuneListItemBinding

class iTuneRecyclerViewAdapter(data: List<SongData>,
                               val listener: RecyclerViewClickListener):
    RecyclerView.Adapter<iTuneRecyclerViewAdapter.ViewHolder>() {

    var songs = listOf<SongData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface RecyclerViewClickListener {
        fun onItemClick(position: Int)
    }

    class ViewHolder(val binding: ItuneListItemBinding):
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItuneListItemBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.songData = songs[position]
        holder.binding.root.setOnClickListener {
            listener.onItemClick(position)
        }
        holder.binding.executePendingBindings()
    }
}