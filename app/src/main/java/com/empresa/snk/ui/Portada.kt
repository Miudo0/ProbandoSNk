package com.empresa.snk.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.empresa.snk.R
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
            painter = painterResource(id = R.drawable.fondosnk),
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
fun CarouselCircular() {
    val images = listOf(
        R.drawable.characters,
        R.drawable.titans,
        R.drawable.episodes
    )
    val infinitePages = Int.MAX_VALUE // Simula páginas infinitas
    val startPage = infinitePages / 2 // Para empezar en el centro
    val pagerState = rememberPagerState(initialPage = startPage, pageCount = { infinitePages })
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            beyondViewportPageCount = PagerDefaults.BeyondViewportPageCount,
            pageSize = PageSize.Fixed(220.dp),
            pageSpacing = 8.dp, // Espacio entre imágenes
            verticalAlignment = Alignment.CenterVertically,

        ) { index ->
            val imageRes = images[index % images.size]  // Hace el bucle circular
            val pageOffset = (index - pagerState.currentPage).toFloat()

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { }
                        .graphicsLayer(
                            scaleX = 1f - 0.05f * abs(pageOffset),
                            scaleY = 1f - 0.05f * abs(pageOffset),
                            translationY = -abs(pageOffset) * 15f,
                            alpha = 1f - 0.1f * abs(pageOffset),
                            rotationY = pageOffset * 10f,
                            cameraDistance = 8f
                        )
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
    }




//    // Corrige la posición cuando el usuario se desliza demasiado hacia un extremo
//    LaunchedEffect(pagerState.currentPage) {
//        if (pagerState.currentPage < 0) {
//            pagerState.scrollToPage(0)
//        }
//    }


}