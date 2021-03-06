package com.nooblabs.singalong.Adapters

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nooblabs.singalong.Models.Duration
import com.nooblabs.singalong.Models.Song
import com.nooblabs.singalong.MusicPlayer
import com.nooblabs.singalong.R
import kotlinx.android.synthetic.main.song_item_layout.view.*

class SongsListAdapter(var context: Context) : RecyclerView.Adapter<SongsListAdapter.VH>() {
    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mSong: Song? = null

        init {
            itemView.setOnClickListener {
                if (mSong != null)
                    MusicPlayer.getInstance().play(context, mSong!!)
            }
        }

    }

    var mCursor: Cursor? = null

    fun setData(cursor: Cursor?) {
        mCursor = cursor
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        if (mCursor != null) {
            return mCursor!!.count
        } else {
            return -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(context).inflate(R.layout.song_item_layout, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        if (mCursor != null) {
            if (mCursor!!.moveToPosition(position)) {
                val titleColumn = mCursor!!.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE)
                val artistColumn = mCursor!!.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST)
                val durationColumn = mCursor!!.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION)
                val pathColumn = mCursor!!.getColumnIndex(MediaStore.Audio.AudioColumns.DATA)

                val title = mCursor!!.getString(titleColumn)
                val artist = mCursor!!.getString(artistColumn)
                val duration = mCursor!!.getLong(durationColumn)
                val path = mCursor!!.getString(pathColumn)
                val song = Song(title = title, artist = artist, duration = Duration(duration), path = path)
                holder.itemView.title.text = song.title
                holder.mSong = song
            }
        }

    }
}