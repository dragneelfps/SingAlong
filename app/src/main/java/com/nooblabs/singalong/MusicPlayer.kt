package com.nooblabs.singalong

import android.content.Context
import com.nooblabs.singalong.Helpers.MusicNotificationHelper
import com.nooblabs.singalong.Helpers.MusicPlayerHelper
import com.nooblabs.singalong.Models.Song


class MusicPlayer {

    companion object {
        private var INSTANCE: MusicPlayer? = null
        fun getInstance(): MusicPlayer {
            if (INSTANCE == null) {
                INSTANCE = MusicPlayer()
            }
            return INSTANCE!!
        }
    }

    fun play(context: Context, song: Song) {
        val player = MusicPlayerHelper.getInstance(context)
        player.start(Any(), song.path)
        MusicNotificationHelper.getInstance(context).showNotification(song)
    }
}
