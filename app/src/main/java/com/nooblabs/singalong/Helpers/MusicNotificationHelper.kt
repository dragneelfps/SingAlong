package com.nooblabs.singalong.Helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.widget.RemoteViews
import com.nooblabs.singalong.Models.Song
import com.nooblabs.singalong.R
import com.nooblabs.singalong.Receivers.MusicControlBroadcastReceiver

class MusicNotificationHelper(val mContext: Context) {
    companion object {
        private var INSTANCE: MusicNotificationHelper? = null
        fun getInstance(context: Context): MusicNotificationHelper {
            if (INSTANCE == null) {
                INSTANCE = MusicNotificationHelper(context)
            }
            return INSTANCE!!
        }

        val ACTION_INTENT_REQUEST_CODE = 12345
        val NOTIF_TITLE = "Song Playing"
        val NOTIF_ID = 123
        val NOTIF_CHANNEL_ID = "1234"
        val NOTIF_CHANNEL_NAME = "Sing Along"
        val NOTIF_CHANNEL_DESCRIPTION = "Some useful description"
    }

    var mPendingPauseIntent: PendingIntent
    var mPendingResumeIntent: PendingIntent
    var mPendingStopIntent: PendingIntent

    init {
        val pauseIntent = Intent(mContext, MusicControlBroadcastReceiver::class.java)
        pauseIntent.action = MusicControlBroadcastReceiver.ACTION_PAUSE
        mPendingPauseIntent = PendingIntent.getBroadcast(mContext, ACTION_INTENT_REQUEST_CODE, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val resumeIntent = Intent(mContext, MusicControlBroadcastReceiver::class.java)
        resumeIntent.action = MusicControlBroadcastReceiver.ACTION_RESUME
        mPendingResumeIntent = PendingIntent.getBroadcast(mContext, ACTION_INTENT_REQUEST_CODE, resumeIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val stopIntent = Intent(mContext, MusicControlBroadcastReceiver::class.java)
        stopIntent.action = MusicControlBroadcastReceiver.ACTION_STOP
        mPendingStopIntent = PendingIntent.getBroadcast(mContext, ACTION_INTENT_REQUEST_CODE, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    lateinit var mCurrentSong: Song

    /*Show Notification of the current playing song */
    fun showNotification(song: Song, playing: Boolean) {
        mCurrentSong = song
        createNotificationChannel(mContext)
        val builder = buildNotification(song.title, song.artist, playing)
        val notifManager = NotificationManagerCompat.from(mContext)
        notifManager.notify(NOTIF_ID, builder.build())
    }

    /*Returns a Builder for notification */
    fun buildNotification(songTitle: String, songArtist: String, playing: Boolean): NotificationCompat.Builder {
        val remoteView = if (playing) getPlayingNotificationView(songTitle, songArtist)
        else getPausedNotificationView(songTitle, songArtist)
        val builder = NotificationCompat.Builder(mContext, NOTIF_CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_music_note_black_36dp)
                .setContent(remoteView)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true) //Make the notification non-dismissible
        return builder
    }

    /*Create a notification channel where we show our notification */
    fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(NOTIF_CHANNEL_ID, NOTIF_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW)
        channel.description = NOTIF_CHANNEL_DESCRIPTION
        val notifManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notifManager.createNotificationChannel(channel)
    }

    fun getPlayingNotificationView(songTitle: String, songArtist: String): RemoteViews {
        val notificationView = RemoteViews(
                mContext.packageName, R.layout.music_custom_notificaiton
        )
        notificationView.setTextViewText(R.id.song_title, songTitle)
        notificationView.setTextViewText(R.id.song_artist, songArtist)
        notificationView.setImageViewResource(R.id.resume_pause_btn, R.drawable.ic_pause_black_36dp)
        notificationView.setOnClickPendingIntent(R.id.resume_pause_btn, mPendingPauseIntent)
        notificationView.setOnClickPendingIntent(R.id.stop_btn, mPendingStopIntent)
        return notificationView
    }

    fun getPausedNotificationView(songTitle: String, songArtist: String): RemoteViews {
        val notificationView = RemoteViews(
                mContext.packageName, R.layout.music_custom_notificaiton
        )
        notificationView.setTextViewText(R.id.song_title, songTitle)
        notificationView.setTextViewText(R.id.song_artist, songArtist)
        notificationView.setImageViewResource(R.id.resume_pause_btn, R.drawable.ic_play_arrow_black_36dp)
        notificationView.setOnClickPendingIntent(R.id.resume_pause_btn, mPendingResumeIntent)
        notificationView.setOnClickPendingIntent(R.id.stop_btn, mPendingStopIntent)
        return notificationView
    }
}