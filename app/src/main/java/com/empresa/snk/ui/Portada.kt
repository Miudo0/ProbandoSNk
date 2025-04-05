package com.empresa.snk.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.empresa.snk.R
import kotlin.math.abs

@Composable
fun PortadaScreen(
    navigateToCharacters: () -> Unit,
    navigateToTitans: () -> Unit,
    navigateToEpisodes: () -> Unit,
    navigateToOrganizations: () -> Unit,
    navigateToLocations: () -> Unit
) {
    PortadaContent(
        navigateToCharacters,
        navigateToTitans,
        navigateToEpisodes,
        navigateToOrganizations,
        navigateToLocations
    )
}

@Composable
fun PortadaContent(
    navigateToCharacters: () -> Unit,
    navigateToTitans: () -> Unit,
    navigateToEpisodes: () -> Unit,
    navigateToOrganizations: () -> Unit,
    navigateToLocations: () -> Unit
) {

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
            CarouselCircular(
                navigateToCharacters,
                navigateToTitans,
                navigateToEpisodes,
                navigateToOrganizations,
                navigateToLocations
            )
        }
    }
}

@Composable
fun CarouselCircular(
    navigateToCharacters: () -> Unit,
    navigateToTitans: () -> Unit,
    navigateToEpisodes: () -> Unit,
    navigateToOrganizations: () -> Unit,
    navigateToLocations: () -> Unit

) {
    val images = listOf(
        R.drawable.characters to "Personajes",
        R.drawable.titans to "Titanes",
        R.drawable.episodes to "Episodios",
        R.drawable.organizations to "Organizaciones",
        R.drawable.locations to "Ubicaciones"
    )
    val infinitePages = Int.MAX_VALUE // Simula páginas infinitas
    val startPage = infinitePages / 2 // Para empezar en el centro
    val pagerState = rememberPagerState(initialPage = startPage, pageCount = { infinitePages })

    //calcular el centro
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val padding = (screenWidth - 220.dp) / 2


    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.TopCenter

    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()

            ,
            pageSpacing = 16.dp, // Espacio entre imágenes
            contentPadding = PaddingValues(horizontal = padding),
            beyondViewportPageCount = 1,
            pageSize = PageSize.Fixed(220.dp),

            verticalAlignment = Alignment.CenterVertically,



            ) { index ->
            val (imageRes, title) = images[index % images.size]
            val pageOffset = (index - pagerState.currentPage).toFloat()
            val isMainPage = abs(pageOffset) < 0.5f

            Column (
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),

                    elevation = androidx.compose.material3.CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            when (index % images.size) {
                                0 -> navigateToCharacters()
                                1 -> navigateToTitans()
                                2 -> navigateToEpisodes()
                                3 -> navigateToOrganizations()
                                4 -> navigateToLocations()
                            }
                        }
                        .graphicsLayer(
                            scaleX = 1f - 0.05f * abs(pageOffset),
                            scaleY = 1f - 0.05f * abs(pageOffset),
                            translationY = -abs(pageOffset) * 15f,
                            alpha = 1f - 0.1f * abs(pageOffset),
                            rotationY = pageOffset * 10f,
                            cameraDistance = 8f
                        )
                        .size(240.dp)
                ) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                }
                if (isMainPage) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier
                            .padding(top = 8.dp)
                    )
                }
            }
        }
    }

}