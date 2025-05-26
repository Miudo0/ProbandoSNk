package com.empresa.snk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var showSplash by remember { mutableStateOf(true) }

            SNKTheme {
                if (showSplash) {
                    // 🔸 llama al composable que ya tienes
                    com.empresa.snk.ui.SplashScreen { showSplash = false }
                } else {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.Transparent      // evita destello blanco
                    ) {
                        NavigarionWrapper()
                    }
                }
            }
        }
    }
}




