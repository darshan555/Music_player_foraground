package com.example.music_player_foraground

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.widget.SeekBar
import androidx.core.app.ActivityCompat
import com.example.music_player_foraground.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),ServiceConnection {
    companion object{
        var musicService:MusicService? = null
        var isPlaing:Boolean = false
    }
    private lateinit var binding: ActivityMainBinding
    private var curruntMusic:MutableList<Int> = mutableListOf(R.raw.music)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    android.Manifest.permission.POST_NOTIFICATIONS
                ),0
            )
        }
        setContentView(binding.root)
        //For starting service

        val intent = Intent(this, MusicService::class.java)
        bindService(intent,this, BIND_AUTO_CREATE)
        startService(intent)

        binding.playBTN.setOnClickListener{
            if(musicService!!.mediaPlayer == null) {
                musicService!!.mediaPlayer = MediaPlayer.create(this, curruntMusic[0])
                initializeSeekBar()
            }
            musicService!!.mediaPlayer?.start()
            isPlaing  = true
            musicService!!.showNotification()


        }
        binding.pauseBTN.setOnClickListener{
            if(musicService!!.mediaPlayer != null) {
                isPlaing = false
                musicService!!.mediaPlayer?.pause()
            }
        }
        binding.stopBTN.setOnClickListener{
            if(musicService!!.mediaPlayer != null) {
                isPlaing = false
                musicService!!.mediaPlayer?.stop()
                musicService!!.mediaPlayer?.reset()
                musicService!!.mediaPlayer?.release()
                musicService!!.mediaPlayer = null
            }
        }

        binding.musicSB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if(p2) musicService!!.mediaPlayer?.seekTo(p1)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

    }

    private fun initializeSeekBar() {
        binding.musicSB.max  = musicService!!.mediaPlayer!!.duration
        val handler = Handler()
        handler.postDelayed(object :Runnable{
            override fun run() {
                try{
                    binding.musicSB.progress = musicService!!.mediaPlayer!!.currentPosition
                    handler.postDelayed(this,1000)
                }catch (e:Exception){
                    binding.musicSB.progress = 0
                }
            }

        },0)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        musicService = binder.curruntService()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        musicService = null
    }

    override fun onDestroy() {
        super.onDestroy()
//        musicService!!.mediaPlayer?.stop()
//        musicService!!.mediaPlayer?.reset()
//        musicService!!.mediaPlayer?.release()
//        musicService!!.mediaPlayer = null
    }
}
