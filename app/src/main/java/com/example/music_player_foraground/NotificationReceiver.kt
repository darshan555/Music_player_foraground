package com.example.music_player_foraground

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import kotlin.system.exitProcess

@Suppress("DEPRECATION")
class NotificationReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action){
            RunningApp.PLAY-> if(MainActivity.isPlaing) Toast.makeText(context, "already Playing", Toast.LENGTH_SHORT)
                .show() else playMusic()
            RunningApp.PAUSE-> if(MainActivity.isPlaing) pauseMusic() else Toast.makeText(
                context,
                "Already Paused", Toast.LENGTH_SHORT).show()
            RunningApp.EXIT-> {
                MainActivity.musicService!!.stopForeground(true)
                MainActivity.musicService = null
                exitProcess(1)
            }
        }
    }
    private fun playMusic(){
        MainActivity.isPlaing = true
        MainActivity.musicService!!.mediaPlayer!!.start()
    }
    private fun pauseMusic(){
        MainActivity.isPlaing = false
        MainActivity.musicService!!.mediaPlayer!!.pause()
    }
}