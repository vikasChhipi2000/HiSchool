package com.example.hischool

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_browser_video_view.*

class BrowserVideoView : AppCompatActivity() {
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser_video_view)

        url = intent.getStringExtra("url");
        videoView.setVideoURI(Uri.parse(url))
        videoView.start()
    }

//    fun playPauseClick(view :View){
//        if(play){
//            play = false
//            imageView2.setImageResource(android.R.drawable.ic_media_pause)
//            videoView.stopPlayback()
//        }else{
//            play = true
//            imageView2.setImageResource(android.R.drawable.ic_media_play)
//            videoView.start()
//        }
//    }
}