package com.example.music_player_foraground

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat

class MusicService : Service() {

    private var myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null
    private lateinit var mediaSession: MediaSessionCompat
    override fun onBind(p0: Intent?): IBinder {
        mediaSession = MediaSessionCompat(baseContext, "My music")
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun curruntService(): MusicService {
            return this@MusicService
        }
    }

    fun showNotification() {
        val playIntent =
            Intent(baseContext, NotificationReceiver::class.java).setAction(RunningApp.PLAY)
        val playPendingIntent =
            PendingIntent.getBroadcast(baseContext, 0, playIntent, PendingIntent.FLAG_IMMUTABLE)

        val pauseIntent =
            Intent(baseContext, NotificationReceiver::class.java).setAction(RunningApp.PAUSE)
        val pausePendingIntent =
            PendingIntent.getBroadcast(baseContext, 0, pauseIntent, PendingIntent.FLAG_IMMUTABLE)

        val exitIntent =
            Intent(baseContext, NotificationReceiver::class.java).setAction(RunningApp.EXIT)
        val exitPendingIntent =
            PendingIntent.getBroadcast(baseContext, 0, exitIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification =
            NotificationCompat.Builder(baseContext, RunningApp.CHANNEL_ID).setContentTitle("music")
                .setSmallIcon(R.drawable.ic_launcher_foreground).setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.sessionToken)
                ).setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setOnlyAlertOnce(true)
                .addAction(R.drawable.play, "Play", playPendingIntent)
                .addAction(R.drawable.pause, "Pause", pausePendingIntent)
                .addAction(R.drawable.exit, "Exit", exitPendingIntent).build()

        startForeground(13, notification)
    }

}
