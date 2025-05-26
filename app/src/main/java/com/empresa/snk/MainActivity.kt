package com.empresa.snk

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.drawable.toDrawable
import com.empresa.snk.ui.navegacion.NavigarionWrapper
import com.empresa.snk.ui.theme.SNKTheme
import dagger.hilt.android.AndroidEntryPoint
import android.graphics.Color as AndroidColor


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var bgPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Evita flash blanco: el fondo de ventana será negro hasta que Compose dibuje
        window.setBackgroundDrawable(AndroidColor.BLACK.toDrawable())
        setContent {
            var showSplash by remember { mutableStateOf(true) }

            SNKTheme {
                Crossfade(targetState = showSplash) { splash ->
                    if (splash) {
                        com.empresa.snk.ui.SplashScreen { showSplash = false }
                    } else {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = Color.Black        // fondo seguro mientras el vídeo se prepara
                        ) {
                            NavigarionWrapper()
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (bgPlayer == null) {
            bgPlayer = MediaPlayer.create(this, R.raw.backgorund_music)
            bgPlayer?.isLooping = true      // reproduce en bucle
            bgPlayer?.setVolume(0.8f, 0.8f) // ajusta volumen
        }
        bgPlayer?.start()
    }

    override fun onStop() {
        super.onStop()
        bgPlayer?.pause()
    }

    override fun onDestroy() {
        bgPlayer?.release()
        bgPlayer = null
        super.onDestroy()
    }
}
