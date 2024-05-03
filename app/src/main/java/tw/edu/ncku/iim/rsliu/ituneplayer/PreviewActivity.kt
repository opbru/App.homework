package tw.edu.ncku.iim.rsliu.ituneplayer

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import org.w3c.dom.Text
import tw.edu.ncku.iim.rsliu.ituneplayer.databinding.ActivityPreviewBinding

class PreviewActivity : AppCompatActivity(), MediaController.MediaPlayerControl {
    var title: String? = null
    var cover: Bitmap? = null
    var url: String? = null
    private var isPlaying = false
    private var bufferPercentage = 0



    private val mediaPlayer = MediaPlayer()
    private val mediaController by lazy {
        object : MediaController(this) {
            override fun show(timeout: Int) {
                super.show(0)
            }
            override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
                if (event!!.keyCode == KeyEvent.KEYCODE_BACK) {
                    onBackPressed()
                }
                return super.dispatchKeyEvent(event)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isPlaying", isPlaying)
        outState.putInt("currentPosition", mediaPlayer.currentPosition)
    }

    val binding: ActivityPreviewBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_preview)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        title = intent.getStringExtra("title")
        cover = intent.getParcelableExtra("cover")
        url = intent.getStringExtra("url")

        binding.title = title
        binding.cover = cover

        try{
            mediaPlayer.setDataSource(url)
            mediaPlayer.setOnPreparedListener{
                Log.i("PreviewActivity", "MediaPlayer is ready...")
                mediaPlayer.setOnCompletionListener {
                    isPlaying = false
                    mediaController.show() // force to show the play button
                }
                mediaController.setAnchorView(binding.anchorView)
                mediaController.setMediaPlayer(this)
                mediaController.show()
                if(savedInstanceState != null){
                    isPlaying = savedInstanceState.getBoolean("isPlaying")
                    val position = savedInstanceState.getInt("currentPosition")
                    mediaPlayer.seekTo(position)
                    if(isPlaying){
                        mediaPlayer.start()
                    }
                }
            }
            mediaPlayer.setOnBufferingUpdateListener { mediaPlayer, i ->
                bufferPercentage = i
            }
            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun start() {
        mediaPlayer.start()
        isPlaying = true
    }

    //first step override pause function if(isPlaying) mediaPlayer.start()
    override fun pause() {
        mediaPlayer.pause()
        isPlaying = false
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mediaController.hide()
    }

    override fun getDuration(): Int {
        return mediaPlayer.duration
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun seekTo(p0: Int) {
        mediaPlayer.seekTo(p0)
    }

    override fun isPlaying(): Boolean {
        return isPlaying
    }

    override fun getBufferPercentage(): Int {
        return bufferPercentage
    }

    override fun canPause(): Boolean {
        return true
    }

    override fun canSeekBackward(): Boolean {
        return true
    }

    override fun canSeekForward(): Boolean {
        return true
    }

    override fun getAudioSessionId(): Int {
        return mediaPlayer.audioSessionId
    }
}