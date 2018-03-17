package com.nooblabs.singalong

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat


class MusicPlayer{

    companion object {
        var mMediaPlayer : MediaPlayer? = null

        var currentSong: Song? = null

        fun play(context: Context, song: Song?){
            if(song != null) {
                if (mMediaPlayer == null) {
                    mMediaPlayer = MediaPlayer()
                }
                mMediaPlayer!!.reset()
                mMediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
                mMediaPlayer!!.setDataSource(song.path)
                mMediaPlayer!!.prepare()
                mMediaPlayer!!.start()
                currentSong = song
                showNotification(context, currentSong!!)
            }
        }

        fun showNotification(context: Context, song: Song){
            createNotificationChannel(context)
//            val builder = NotificationCompat.Builder(context, "123")
//                    .setSmallIcon(R.drawable.notification_template_icon_bg)
//                    .setContentTitle("Song Playing")
//                    .setContentText(song.title)
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            val builder = NotificationCompat.Builder(context, "123")
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setSmallIcon(R.drawable.notification_template_icon_bg)
                    .addAction(R.drawable.notification_template_icon_bg, "Pause", null)
                    .addAction(R.drawable.notification_template_icon_bg, "Stop", null)
                    .addAction(R.drawable.notification_template_icon_bg, "Next", null)
                    .setStyle(android.support.v4.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(0 /* #1: pause button */))
                    .setContentTitle("Song Playing")
                    .setContentText(song.title)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            val notifManager = NotificationManagerCompat.from(context)
            notifManager.notify(123,builder.build())
        }

        fun createNotificationChannel(context: Context){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val channel = NotificationChannel("123","music",NotificationManager.IMPORTANCE_DEFAULT)
                channel.description = "its a description"
                val notifManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notifManager.createNotificationChannel(channel)
            }
        }
    }


}
