package com.empresa.snk.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
    fun EpisodesScreen(
      paddingValues: PaddingValues,
      viewModel: GetEpisodesViewModel = hiltViewModel(),
      context: Context
    ){
        val state by viewModel.episodes.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.getEpisodes()
        }

        when(val current = state){
            is EpisodesState.Success -> {
                val episodes = current.episodes
                if (episodes.isNotEmpty()) {
                    LazyColumn(
                        contentPadding = paddingValues,

                        ) {

                        items(episodes) { episodes ->

                            Column(modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .clickable {
                                    val url = "https://www.crunchyroll.com/${episodes.name}"
                                    openUrl(context, url)
                                }
                            ) {
                                episodes.img?.let {
                                    Log.d("Greeting", "Character Image URL: $it")
                                  EpisodesImage(
                                      it,
                                      modifier = Modifier
                                          .fillMaxWidth()
                                          .height(150.dp)

                                      )

                                }
                                Text(text = episodes.name ?: "Desconocido")
                                Text(text = episodes.episode ?: "Desconocido")

                            }
                        }
                        item{
                            if(viewModel.hasMorePagesEpisodes()){
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
    fun EpisodesImage(imageUrl: String?, modifier: Modifier = Modifier) {
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
// Función para abrir la URL en el navegador
fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    context.startActivity(intent)
}

