package com.empresa.snk.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade


@Composable
fun LocationsScreen(
    paddingValues: PaddingValues,

    ) {
    LocationsContent()

}

@Composable
fun LocationsContent(
    viewModel: GetAllLocationsViewModel = hiltViewModel(),
    viewModelDebut: GetEpisodesDetailViewModel = hiltViewModel()
) {
    val locationsState = viewModel.locations.collectAsState()
    val episodesState by viewModelDebut.episodeName.collectAsState()



    LaunchedEffect(Unit) {
        viewModel.getLocations()
    }

    when (val current = locationsState.value) {
        is LocationsState.Success -> {
            val locations = current.locations
            if (locations.isNotEmpty()) {
                LazyColumn(

                    modifier = Modifier.padding(16.dp)
                ) {
                    items(locations) { locations ->

                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            locations.img?.let {
                                Log.d("Greeting", "Character Image URL: $it")
                                LocationsImage(
                                    it,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                            }
                        }
                        Text(text = locations.name ?: "Desconocido")
                        Text(text = locations.territory ?: "Desconocido")
                        Text(text = locations.region ?: "Desconocido")


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