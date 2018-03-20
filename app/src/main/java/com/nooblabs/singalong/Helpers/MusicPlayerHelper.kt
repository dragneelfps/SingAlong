package com.nooblabs.singalong.Helpers

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log


class MusicPlayerHelper private constructor(var mContext: Context) : MediaPlayer.OnPreparedListener {

    companion object {
        private val TAG = "MusicPlayerHelper"

        enum class State {
            Stopped,
            Playing,
            Paused,
            Preparing,
            Prepared
        }

        private val DUCK_VOLUME = 0.1f
        var INSTANCE: MusicPlayerHelper? = null
        fun getInstance(context: Context): MusicPlayerHelper {
            if (INSTANCE == null) {
                INSTANCE = MusicPlayerHelper(context)
            }
            return INSTANCE!!
        }
    }


    var mMediaPlayer = MediaPlayer()

    var focusHelper = AudioFocusHelper()
    lateinit var mCurrentSongUri: String
    lateinit var mCurrentSongId: Any
    var mSongState = State.Stopped

    var mAudioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

    fun start(songId: Any, songUri: String) {
        mCurrentSongId = songId
        mCurrentSongUri = songUri
        focusHelper.requestAudioFocus(mCurrentSongUri)
    }

    fun pauseSong() {
        if (mSongState == State.Playing) {
            mSongState = State.Paused
            mMediaPlayer.pause()
        }
    }

    fun resumeSong() {
        if (mSongState == State.Paused) {
            mSongState = State.Playing
            mMediaPlayer.start()
        }
    }

    fun playSong() {
        if (mSongState == State.Prepared) {
            mSongState = State.Playing
            mMediaPlayer.start()
        }
    }

    fun releaseResources() {
        mMediaPlayer.reset()
//        mMediaPlayer.release()
    }

    fun prepareMediaPlayer() {
        mMediaPlayer.setAudioAttributes(mAudioAttributes)
        mMediaPlayer.setDataSource(mCurrentSongUri)
        mMediaPlayer.setOnPreparedListener(this)
        mSongState = State.Preparing
        mMediaPlayer.prepareAsync()
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mSongState = State.Prepared
        playSong()
    }

    inner class AudioFocusHelper : AudioManager.OnAudioFocusChangeListener {

        private val mFocusLock = Any()
        lateinit var mAudioFocusRequest: AudioFocusRequest
        var mPlaybackDelayed = false
        var mPlaybackNowAuthorized = false
        var mResumeOnAudioFocusGain = true
        val AUDIO_REQUEST_TYPE = AudioManager.AUDIOFOCUS_GAIN

        fun requestAudioFocus(songUri: String) {
            val audioManager = mContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

            mAudioFocusRequest = AudioFocusRequest.Builder(AUDIO_REQUEST_TYPE)
                    .setAudioAttributes(mAudioAttributes)
                    .setAcceptsDelayedFocusGain(true)
                    .setOnAudioFocusChangeListener(this)
                    .build()
            val focusRequest = audioManager.requestAudioFocus(mAudioFocusRequest)
            synchronized(mFocusLock) {
                when (focusRequest) {
                    AudioManager.AUDIOFOCUS_REQUEST_FAILED -> {
                        mPlaybackNowAuthorized = false
                        Log.d(TAG, "Could not get audio focus $songUri")
                    }
                    AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> {
                        mPlaybackNowAuthorized = true
                        Log.d(TAG, "Got the audio focus $songUri")
                        releaseResources()
                        prepareMediaPlayer() // And play
                    }
                    AudioManager.AUDIOFOCUS_REQUEST_DELAYED -> {
                        mPlaybackDelayed = true
                        mPlaybackNowAuthorized = false
                        Log.d(TAG, "Got audio focus request delayed $songUri")
                    }
                    else -> {
                        Log.d(TAG, "Got this: $focusRequest")
                    }
                }
            }
        }

        override fun onAudioFocusChange(focusChange: Int) {
            when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN -> if (mPlaybackDelayed || mResumeOnAudioFocusGain) {
                    synchronized(mFocusLock) {
                        mPlaybackDelayed = false
                        mResumeOnAudioFocusGain = false
                    }
                    Log.d(TAG, "Got the audio focus")
                    resumeSong()
                }
                AudioManager.AUDIOFOCUS_LOSS -> {
                    synchronized(mFocusLock) {
                        mResumeOnAudioFocusGain = false
                        mPlaybackDelayed = false
                    }
                    Log.d(TAG, "Lost the audio focus")
                    releaseResources()
                }
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                    synchronized(mFocusLock) {
                        mResumeOnAudioFocusGain = true
                        mPlaybackDelayed = false
                    }
                    Log.d(TAG, "Loss transiently the audio focus")
                    pauseSong()
                }
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                    Log.d(TAG, "Loss transiently/can duck the audio focus")
                }
            }// ... pausing or ducking depends on your app
        }
    }


}
