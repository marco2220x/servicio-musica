package com.example.serviciomusica

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import android.content.Context

class MainActivity : AppCompatActivity() {

    private lateinit var musicService: MusicService
    private var isBound = false
    private var isPlaying = false  // Variable para controlar el estado de reproducción

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.LocalBinder
            musicService = binder.getService()
            isBound = true
            Log.d("MainActivity", "Service connected")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
            Log.d("MainActivity", "Service disconnected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnPlayPause: ImageButton = findViewById(R.id.btnPlayPause)
        val btnStop: ImageButton = findViewById(R.id.btnStop)
        val btnPrevious: ImageButton = findViewById(R.id.btnPrevious)
        val btnNext: ImageButton = findViewById(R.id.btnNext)

        // Control de Play/Pause en un solo botón
        btnPlayPause.setOnClickListener {
            val intent = Intent(this, MusicService::class.java)
            if (isPlaying) {
                intent.action = MusicService.ACTION_PAUSE
                btnPlayPause.setImageResource(R.drawable.ic_play)
            } else {
                intent.action = MusicService.ACTION_PLAY
                btnPlayPause.setImageResource(R.drawable.ic_pause)
            }
            startService(intent)
            isPlaying = !isPlaying
        }

        // Botón de Stop
        btnStop.setOnClickListener {
            val intent = Intent(this, MusicService::class.java).apply {
                action = MusicService.ACTION_STOP
            }
            startService(intent)

            // Resetear estado cuando se detiene la música
            isPlaying = false
            btnPlayPause.setImageResource(R.drawable.ic_play)
        }

        // Botón para Pista Anterior
        btnPrevious.setOnClickListener {
            val intent = Intent(this, MusicService::class.java).apply {
                action = MusicService.ACTION_PREVIOUS
            }
            startService(intent)
        }

        // Botón para Pista Siguiente
        btnNext.setOnClickListener {
            val intent = Intent(this, MusicService::class.java).apply {
                action = MusicService.ACTION_NEXT
            }
            startService(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
    }
}
