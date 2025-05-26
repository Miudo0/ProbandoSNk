package com.empresa.snk

import android.graphics.drawable.ColorDrawable
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
import com.empresa.snk.ui.navegacion.NavigarionWrapper
import com.empresa.snk.ui.theme.SNKTheme
import dagger.hilt.android.AndroidEntryPoint
import android.graphics.Color as AndroidColor


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Evita flash blanco: el fondo de ventana será negro hasta que Compose dibuje
        window.setBackgroundDrawable(ColorDrawable(AndroidColor.BLACK))
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
}
