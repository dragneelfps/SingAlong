package com.nooblabs.singalong.Receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.nooblabs.singalong.Helpers.MusicNotificationHelper
import com.nooblabs.singalong.Helpers.MusicPlayerHelper

class MusicControlBroadcastReceiver : BroadcastReceiver() {

    companion object {
        val TAG = "MusicControlBroadcastReceiver"
        val ACTION_PAUSE = "pause it"
        val ACTION_RESUME = "resume it"
        val ACTION_STOP = "stop it"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "Received control.")
        val player = MusicPlayerHelper.getInstance(context)
        val notifHelper = MusicNotificationHelper.getInstance(context)
        val action = intent.action
        Log.d(TAG, "action : $action")
        when (action) {
            ACTION_PAUSE -> {
                player.pauseSong()
                notifHelper.showNotification(notifHelper.mCurrentSong, false)
            }
            ACTION_RESUME -> {
                player.resumeSong()
                notifHelper.showNotification(notifHelper.mCurrentSong, true)
            }
            ACTION_STOP -> {
                player.releaseResources()
                val notifManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notifManager.cancel(MusicNotificationHelper.NOTIF_ID)
            }
        }
    }
}
