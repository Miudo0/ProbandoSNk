package com.empresa.snk.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade


@Composable
    fun EpisodesScreen(
      paddingValues: PaddingValues,
      viewModel: GetEpisodesViewModel = hiltViewModel()
    ){
        val state by viewModel.episodes.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.getEpisodes()
        }

        when(val current = state){
            is EpisodesState.Success -> {
                val episodes = current.episodes
                if (episodes.isNotEmpty()) {
                    LazyVerticalGrid(contentPadding = paddingValues,
                        columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2)
                    ) {

                        items(episodes) { Episodes ->

                            Column(modifier = Modifier.padding(16.dp)) {
                                Episodes.img?.let {
                                    Log.d("Greeting", "Character Image URL: $it")
                                  EpisodesImage(it)
                                }
                                Text(text = Episodes.name ?: "Desconocido")
                            }
                        }
                        item{
                            if(viewModel.hasMorePagesTitans()){
                                viewModel.getEpisodes()
                            }

                        }
                    }
                }

            }
            is EpisodesState.Error -> {
                Text(text = "Error")
            }
            is EpisodesState.Loading -> {


            }
        }

    }


    @Composable
    fun EpisodesImage(imageUrl: String?) {
        imageUrl?.let {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(it)
                    .crossfade(true)
                    .build(),

                contentDescription = "Imagen del personaje",
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(CircleShape),
            )
        }
    }

