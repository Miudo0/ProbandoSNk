package com.empresa.snk.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.empresa.snk.R
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun PortadaScreen() {
    PortadaContent()
}

@Composable
fun PortadaContent() {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.portada),
            contentDescription = "Portada de la App",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center) // Pone el carrusel en la parte inferior
                .height(300.dp)
        ) {
            CarouselCircular()
        }
    }
}

@Composable
fun CarouselCircular(){
    val images = listOf(
        R.drawable.characters,
        R.drawable.titans,
        R.drawable.episodes
    )
    val infinitePages = Int.MAX_VALUE // Simula páginas infinitas
    val startPage = infinitePages / 2 // Para empezar en el centro
    val pagerState = rememberPagerState(initialPage = startPage, pageCount = { infinitePages })
    val coroutineScope = rememberCoroutineScope()

    Column {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { index ->
            val imageRes = images[index % images.size]  // Hace el bucle circular
            val pageOffset = (index - pagerState.currentPage).toFloat()

            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier

                    .width(200.dp)
                    .padding(8.dp)
                    .clickable {  }
                    .graphicsLayer(
                        scaleX = 1f - 0.2f * abs(pageOffset), // Escala según la distancia del centro
                        scaleY = 1f - 0.2f * abs(pageOffset),
                    // Ajusta la posición horizontal
                        translationY = -abs(pageOffset) * 50f, // Agrega profundidad en 3D
                        rotationY = pageOffset * 20f



                    )
                    .offset(x = (index - startPage) * 50.dp)

            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }

    // Corrige la posición cuando el usuario se desliza demasiado hacia un extremo
    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage < images.size * 2 || pagerState.currentPage > infinitePages - images.size * 2) {
            coroutineScope.launch {
                pagerState.scrollToPage(startPage)
            }
        }
    }


}