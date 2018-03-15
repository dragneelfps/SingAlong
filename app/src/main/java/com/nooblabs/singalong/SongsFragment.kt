package com.nooblabs.singalong

import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.songs_fragment_layout.view.*


class SongsFragment: Fragment(), LoaderManager.LoaderCallbacks<Cursor>{

    companion object {
        val TAG = javaClass.simpleName
        val SONGS_READ_ID = 123
    }

    lateinit var mAdapter: SongsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"Fragment onCreate Called")
        mAdapter = SongsListAdapter(context!!)

        loaderManager.initLoader(SONGS_READ_ID, null, this )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG,"Fragment view created")
        val view = inflater.inflate(R.layout.songs_fragment_layout, container, false)
        return view
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if(data != null && data.count > 0){
            mAdapter.setData(data)
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
                context!!,
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA),
                null,
                null,
                null
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.songs_recycler_view.layoutManager = LinearLayoutManager(context)
        view.songs_recycler_view.adapter = mAdapter
    }
}