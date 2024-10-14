package com.example.serviciomusica

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MusicService : Service() {

    companion object {
        const val ACTION_PLAY = "com.example.serviciomusica.PLAY"
        const val ACTION_PAUSE = "com.example.serviciomusica.PAUSE"
        const val ACTION_STOP = "com.example.serviciomusica.STOP"
        const val ACTION_PREVIOUS = "com.example.serviciomusica.PREVIOUS"
        const val ACTION_NEXT = "com.example.serviciomusica.NEXT"
    }

    private var mediaPlayer: MediaPlayer? = null
    private var songList = listOf(R.raw.cancion1, R.raw.cancion2, R.raw.cancion3)
    private var currentSongIndex = 0

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> playMusic()
            ACTION_PAUSE -> pauseMusic()
            ACTION_STOP -> stopMusic()
            ACTION_PREVIOUS -> previousTrack()
            ACTION_NEXT -> nextTrack()
        }
        return START_NOT_STICKY
    }

    private fun playMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, songList[currentSongIndex])
        }
        mediaPlayer?.start()
    }

    private fun pauseMusic() {
        mediaPlayer?.pause()
    }

    private fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun previousTrack() {
        if (currentSongIndex > 0) {
            currentSongIndex--
        } else {
            currentSongIndex = songList.size - 1
        }
        stopMusic()
        playMusic()
    }

    private fun nextTrack() {
        if (currentSongIndex < songList.size - 1) {
            currentSongIndex++
        } else {
            currentSongIndex = 0
        }
        stopMusic()
        playMusic()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return LocalBinder()
    }

    inner class LocalBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }
}
