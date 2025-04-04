package com.empresa.snk.ui

import android.content.Context
import android.content.Intent
import android.util.Log
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
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.empresa.snk.domain.episodesDomain.Episodes

@Composable
fun EpisodesScreen(
    paddingValues: PaddingValues,
    viewModel: GetEpisodesViewModel = hiltViewModel(),
    viewModelBySeason: GetEpisodesBySeasonViewModel = hiltViewModel(),
    context: Context
) {
    val state by viewModel.episodes.collectAsState()
    val stateBySeason by viewModelBySeason.state.collectAsState()

    val seasons = listOf("all", "S1", "S2", "S3", "S4")
    var selectedSeason by remember { mutableStateOf(seasons.first()) }
    LaunchedEffect(Unit) {
        viewModel.getEpisodes()
    }
    LaunchedEffect(selectedSeason) {
        Log.d("EpisodesScreen", "Fetching episodes for season: $selectedSeason")
        if (selectedSeason != "all") {
            viewModelBySeason.getEpisodesBySeason(selectedSeason)
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column {
            // DropdownMenu para seleccionar la temporada
            var expanded by remember { mutableStateOf(false) }

            // Botón de selección de temporada
            Button(
                onClick = { expanded = !expanded },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text("Seleccionar Temporada: $selectedSeason")
            }

            // Dropdown para las temporadas
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                seasons.forEach { season ->
                    DropdownMenuItem(
                        onClick = {
                            selectedSeason = season
                            expanded = false // Cerrar el menú al seleccionar
                        }
                    ) {
                        Text(text = "Temporada $season")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val episodesToShow = when (selectedSeason) {
                "all" -> (state as? EpisodesState.Success)?.episodes ?: emptyList()
                else -> (stateBySeason as? GetEpisodesBySeasonViewModel.EpisodesStateBySeason.Success)?.episodes ?: emptyList()
            }


//
//            when (val current = state) {
//                is EpisodesState.Success -> {
//                    Column {
//                        val episodes = current.episodes
//                        if (episodes.isNotEmpty()) {
//
//                            LazyColumn(
//                                contentPadding = paddingValues,
//
//                                ) {
//
//                                items(episodes) { episodes ->
//
//                                    Column(
//                                        modifier = Modifier
//                                            .padding(16.dp)
//                                            .fillMaxWidth()
//
//                                    ) {
//                                        episodes.img?.let {
//                                            Log.d("Greeting", "Character Image URL: $it")
//                                            EpisodesImage(
//                                                it,
//                                                modifier = Modifier
//                                                    .fillMaxWidth()
//                                                    .height(150.dp)
//
//                                            )
//
//                                        }
//                                        Text(text = episodes.name ?: "Desconocido")
//                                        Text(text = episodes.episode ?: "Desconocido")
//
//                                    }
//                                }
//                                item {
//                                    if (viewModel.hasMorePagesEpisodes()) {
//                                        viewModel.getEpisodes()
//                                    }
//
//                                }
//
//                            }
//                        }
            when {
                episodesToShow.isNotEmpty() -> {
                    LazyColumn(contentPadding = paddingValues) {
                        items(episodesToShow) { episode ->
                            EpisodeItem(episode)
                        }
                        item {
                            if (viewModel.hasMorePagesEpisodes()) {
                              viewModel.getEpisodes()
                            }
                        }
                    }

                }

                (selectedSeason == "all" && state is EpisodesState.Loading) ||
                        (selectedSeason != "all" && stateBySeason is GetEpisodesBySeasonViewModel.EpisodesStateBySeason.Loading) -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    )
                }

                else -> {
                    Text(
                        text = "No hay episodios disponibles",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }



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

@Composable
fun EpisodeItem(episode: Episodes,context: Context = LocalContext.current) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        episode.img?.let {
            EpisodesImage(
                it,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
        }
        Text(text = episode.name ?: "Desconocido", fontWeight = FontWeight.Bold)
        Text(text = episode.episode ?: "Desconocido")
        Button(
            onClick = {
                val url =
                    "https://www.crunchyroll.com/es-es/series/GR751KNZY/attack-on-titan?irclickid=TT0xnj03TxycTQzxIFW772DEUks1NJz9DyiMX00&utm_source=impact&utm_medium=affiliate&utm_campaign=1206980&irgwc=1"
                openUrl(context, url)
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("Ver serie en Crunchyroll")
        }
    }
}

// Función para abrir la URL en el navegador
fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    context.startActivity(intent)
}

