package com.example.music_player_foraground

import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.music_player_foraground.databinding.ActivityMainBinding
import java.io.IOException

class MainActivity : AppCompatActivity() {
    val mp = MediaPlayer()

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mp.setAudioStreamType(AudioManager.STREAM_MUSIC)

        val audioUri = Uri.parse(R.raw.music.toString())
        try{
            mp.setDataSource(this,audioUri)
            mp.prepare()
        }catch (e:IOException){
            e.printStackTrace()
            Log.e("MediaPlayerAudio", "Error preparing MediaPlayer: " + e.message)
        }
        binding.playBTN.setOnClickListener {
            mp.start()
        }
        binding.pauseBTN.setOnClickListener {
            mp.pause()
        }
        binding.stopBTN.setOnClickListener {
            mp.pause()
            mp.seekTo(0)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mp.release()
    }

}