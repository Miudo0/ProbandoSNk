package com.empresa.snk.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.empresa.snk.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(
    paddingValues: PaddingValues = PaddingValues()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Localizaciones • SNK",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        ),
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black.copy(alpha = 0.35f),
                    scrolledContainerColor = Color.Black.copy(alpha = 0.65f),
                    titleContentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.15f),
                                    Color.Transparent
                                )
                            ),
                            size = androidx.compose.ui.geometry.Size(size.width, 2.dp.toPx())
                        )
                    }
            )
        },
        content = { innerPadding ->
            LocationsContent(innerPadding)
        }
    )
}

@Composable
fun LocationsContent(
    paddingValues: PaddingValues,
    viewModel: GetAllLocationsViewModel = hiltViewModel(),
    viewModelDebut: GetEpisodesDetailViewModel = hiltViewModel()
) {
    val locationsState = viewModel.locations.collectAsState()
    val episodesState by viewModelDebut.episodeName.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getLocations()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_locations),
            contentDescription = "Fondo de localizaciones",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.6f)
        )

        when (val current = locationsState.value) {
            is LocationsState.Success -> {
                val locations = current.locations
                if (locations.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        items(locations) { location ->
                            Card(
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    Brush.linearGradient(
                                        listOf(
                                            Color.White.copy(alpha = 0.4f),
                                            Color.White.copy(alpha = 0.2f)
                                        )
                                    )
                                ),
                                colors = androidx.compose.material3.CardDefaults.cardColors(
                                    containerColor = Color.Black.copy(alpha = 0.75f),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .drawBehind {
                                        drawRoundRect(
                                            color = Color.White.copy(alpha = 0.05f),
                                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(24.dp.toPx(), 24.dp.toPx())
                                        )
                                    }
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = location.name ?: "Desconocido",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    location.img?.let {
                                        LocationsImage(
                                            it,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(200.dp)
                                                .clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = "Región: ${location.region ?: "Desconocida"}",
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "Territorio: ${location.territory ?: "Desconocido"}",

                                        style = MaterialTheme.typography.bodySmall.copy(color = Color.White)

                                    )
                                }
                            }
                        }
                        item {
                            if (viewModel.hasMorePages()) {
                                viewModel.getLocations()
                            }
                        }
                    }
                }
            }
            is LocationsState.Error -> {
                Text(text = "Error")
            }
            is LocationsState.Loading -> {
            }
        }
    }
}

@Composable
fun LocationsImage(imageUrl: String?, modifier: Modifier = Modifier) {
    imageUrl?.let {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(it)
                .crossfade(true)
                .build(),

            contentDescription = "Imagen del personaje",
            contentScale = ContentScale.Crop,
            modifier = modifier

        )
    }
}