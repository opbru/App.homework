package tw.edu.ncku.iim.rsliu.ituneplayer

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tw.edu.ncku.iim.rsliu.ituneplayer.databinding.ActivitySwipeRefreshBinding

class MainActivity : AppCompatActivity(),
    iTuneRecyclerViewAdapter.RecyclerViewClickListener {

    val adapter by lazy {
//        iTuneListViewAdapter()
        iTuneRecyclerViewAdapter(listOf<SongData>(), this)
    }
    val swipeRefreshLayout by lazy {
        binding.swipeRefreshLayout
    }

    val binding: ActivitySwipeRefreshBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_swipe_refresh)
    }


//    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
//        super.onListItemClick(l, v, position, id)
//        Toast.makeText(this,
//            adapter.songs[position].title,
//            Toast.LENGTH_LONG).show()
//    }

    fun loadList() {
        GlobalScope.launch(Dispatchers.Main) {
            var songs = listOf<SongData>()

            swipeRefreshLayout.isRefreshing = true
            withContext(Dispatchers.IO) {
                songs = iTuneXMLParser().parseURL(
                    "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=25/xml")
            }

            adapter.songs = songs
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(
            this,
            DividerItemDecoration.VERTICAL
        ))

        swipeRefreshLayout.setOnRefreshListener {
            adapter.songs = listOf<SongData>()
            loadList()
        }

        loadList()
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(this, adapter.songs[position].title, Toast.LENGTH_LONG).show()
        val intent = Intent(this, PreviewActivity::class.java)
        val song = adapter.songs[position]
        intent.putExtra("title", song.title)
        intent.putExtra("cover", song.cover)
        intent.putExtra("url", song.url)
        startActivity(intent)
    }
}