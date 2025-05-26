package com.empresa.snk.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.empresa.snk.R
import kotlinx.coroutines.delay

// SplashScreen.kt
@Composable
fun SplashScreen(onFinish: () -> Unit) {

    // 1. Arranca un temporizador de 2 s
    LaunchedEffect(Unit) {
        delay(2000L)          // 2000 ms
        onFinish()
    }

    // 2. Dibuja el logo centrado
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),   // o tu color corporativo
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.logo), // pon tu imagen en res/drawable
            contentDescription = null,
            modifier = Modifier.size(220.dp),               // ajusta tamaño
            contentScale = ContentScale.Fit
        )
    }
}