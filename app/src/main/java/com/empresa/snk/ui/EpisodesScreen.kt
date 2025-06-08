// Archivo: EpisodesScreen.kt
// Este archivo contiene la pantalla principal de episodios para la app SNK usando Jetpack Compose.
// Aquí se gestionan la visualización de episodios, selección de temporada, e interacción de usuario
// como marcar episodios como vistos y abrir enlaces externos.

package com.empresa.snk.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.empresa.snk.R
import com.empresa.snk.domain.episodesDomain.Episodes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
/**
 * Composable principal que representa la pantalla de episodios.
 * Incluye una barra superior y el contenido principal.
 *
 * @param paddingValues Espaciado de padding proporcionado por el Scaffold padre.
 */
fun EpisodesScreen(paddingValues: PaddingValues) {
    // Scaffold proporciona la estructura visual de la pantalla (barra superior y contenido)
    Scaffold(
        topBar = {
            // Barra superior personalizada con título centrado
            TopAppBar(
                title = {
                    Text(
                        text = "Episodios • SNK",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            letterSpacing = 0.5.sp,
                            color = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                },
                // Colores personalizados para la barra superior
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black.copy(alpha = 0.65f),
                    scrolledContainerColor = Color.Black.copy(alpha = 0.95f),
                    titleContentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        // Dibuja un degradado sutil debajo de la barra
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
        // El contenido principal de la pantalla se delega a EpisodesContent
        content = { innerPadding->
            EpisodesContent(
                paddingValues = innerPadding,
            )
        }
    )
}



/**
 * Composable que representa el contenido principal de la pantalla de episodios.
 * Incluye la lógica de selección de temporada, carga y visualización de episodios,
 * y muestra el estado de carga o mensajes de error.
 *
 * @param paddingValues Espaciado de padding externo.
 * @param viewModel ViewModel para obtener todos los episodios.
 * @param viewModelBySeason ViewModel para obtener episodios por temporada.
 * @param setWatchedViewModel ViewModel para marcar episodios como vistos.
 */
@Composable
fun EpisodesContent(
    paddingValues: PaddingValues,
    viewModel: GetEpisodesViewModel = hiltViewModel(),
    viewModelBySeason: GetEpisodesBySeasonViewModel = hiltViewModel(),
    setWatchedViewModel: SetWatchedEpisodeViewModel = hiltViewModel(),
) {
    // Estado de episodios generales y por temporada
    val state by viewModel.episodes.collectAsState()
    val stateBySeason by viewModelBySeason.state.collectAsState()

    // Lista de temporadas disponibles (all = todas)
    val seasons = listOf("all", "S1", "S2", "S3", "S4")
    var selectedSeason by remember { mutableStateOf(seasons.first()) }

    // Efecto lanzado al entrar en la pantalla: carga todos los episodios
    LaunchedEffect(Unit) {
        viewModel.getEpisodes()
    }
    // Efecto lanzado al cambiar la temporada seleccionada
    LaunchedEffect(selectedSeason) {
        Log.d("EpisodesScreen", "Fetching episodes for season: $selectedSeason")
        if (selectedSeason != "all") {
            viewModelBySeason.getEpisodesBySeason(selectedSeason)
        }
    }

    // Box para superponer el fondo y el contenido principal
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        // Imagen de fondo difuminada
        Image(
            painter = painterResource(id = R.drawable.fondo_episodes),
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.6f)
        )
        // Columna para organizar el menú de temporada y la lista de episodios
        Column {
            // DropdownMenu para seleccionar la temporada
            var expanded by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
            ) {
                // Card que actúa como botón de selección de temporada
                Card(
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Brush.linearGradient(
                            listOf(
                                Color.White.copy(alpha = 0.4f),
                                Color.White.copy(alpha = 0.2f)
                            )
                        )
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black.copy(alpha = 0.6f),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clickable { expanded = true }
                ) {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Temporada: $selectedSeason",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        )
                    }
                }

                // Menú desplegable para elegir temporada
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.9f))
                ) {
                    seasons.forEach { season ->
                        DropdownMenuItem(
                            onClick = {
                                selectedSeason = season
                                expanded = false
                            }
                        ) {
                            Text(
                                text = "Temporada $season",
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Determina qué episodios mostrar según la temporada seleccionada
            val episodesToShow = when (selectedSeason) {
                "all" -> (state as? EpisodesState.Success)?.episodes ?: emptyList()
                else -> (stateBySeason as? GetEpisodesBySeasonViewModel.EpisodesStateBySeason.Success)?.episodes ?: emptyList()
            }

            // Lógica condicional para mostrar la lista, el indicador de carga o un mensaje vacío
            when {
                // Si hay episodios, muestra la lista
                episodesToShow.isNotEmpty() -> {
                    // LazyColumn para mostrar la lista de episodios de forma eficiente
                    LazyColumn(contentPadding = paddingValues) {
                        items(episodesToShow) { episode ->
                            // Composable individual para cada episodio
                            EpisodeItem(
                                episode = episode,
                                setWatchedViewModel = setWatchedViewModel
                            )
                        }
                        // Soporte para paginación: carga más episodios si hay más páginas
                        item {
                            if (viewModel.hasMorePagesEpisodes()) {
                                viewModel.getEpisodes()
                            }
                        }
                    }
                }
                // Si está cargando episodios, muestra un indicador de progreso
                (selectedSeason == "all" && state is EpisodesState.Loading) ||
                        (selectedSeason != "all" && stateBySeason is GetEpisodesBySeasonViewModel.EpisodesStateBySeason.Loading) -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    )
                }
                // Si no hay episodios, muestra un mensaje informativo
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


/**
 * Composable para mostrar una imagen de un episodio usando Coil.
 *
 * @param imageUrl URL de la imagen a mostrar.
 * @param modifier Modificador opcional para personalizar el diseño.
 */
@Composable
fun EpisodesImage(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    imageUrl?.let {
        // AsyncImage carga la imagen de forma asíncrona con efecto crossfade
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

/**
 * Composable que representa un ítem individual de episodio.
 * Muestra la imagen, nombre, código, botón para ver en Crunchyroll y toggle de visto/no visto.
 *
 * @param episode Episodio a mostrar.
 * @param context Contexto para abrir URLs externas.
 * @param viewModel ViewModel para consultar si el episodio está visto.
 * @param setWatchedViewModel ViewModel para actualizar el estado de visto.
 */
@Composable
fun EpisodeItem(
    episode: Episodes,
    context: Context = LocalContext.current,
    viewModel: GetWatchedViewModel = hiltViewModel(),
    setWatchedViewModel: SetWatchedEpisodeViewModel
) {
    // Efecto lanzado al montar el ítem: consulta si el episodio está visto
    LaunchedEffect(episode.id) {
        episode.id?.let { viewModel.getWatched(it) }
    }

    // Estado que indica si el episodio está marcado como visto
    val watchedMap by viewModel.watchedMap.collectAsState()
    val isWatched = watchedMap[episode.id] ?: false

    // Card estilizada para el episodio
    Card(
        shape = RoundedCornerShape(24.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Brush.linearGradient(
                listOf(
                    Color.White.copy(alpha = 0.4f),
                    Color.White.copy(alpha = 0.2f)
                )
            )
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.8f),
            contentColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .drawBehind {
                // Sombra sutil alrededor de la tarjeta
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.05f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(24.dp.toPx(), 24.dp.toPx())
                )
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Imagen del episodio (si existe)
            episode.img?.let {
                EpisodesImage(
                    it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }
            // Nombre del episodio
            Text(
                text = episode.name ?: "Desconocido",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            )
            // Código o número del episodio
            Text(
                text = episode.episode ?: "Desconocido",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            )
            // Botón para abrir el episodio en Crunchyroll
            Button(
                onClick = {
                    val url =
                        "https://www.crunchyroll.com/es-es/series/GR751KNZY/attack-on-titan?irclickid=TT0xnj03TxycTQzxIFW772DEUks1NJz9DyiMX00&utm_source=impact&utm_medium=affiliate&utm_campaign=1206980&irgwc=1"
                    openUrl(context, url)
                },
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = Color.Black.copy(alpha = 0.6f),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Text(
                    text = "Ver en Crunchyroll",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }
            // Fila con el toggle para marcar como visto
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconToggleButton(
                    checked = isWatched,
                    onCheckedChange = { checked ->
                        episode.id?.let { id ->
                            setWatchedViewModel.SetWatchedEpisode(id, checked)
                            viewModel.getWatched(id)
                        }
                    }
                ) {
                    // Icono cambia de color si está visto
                    val icon = if (isWatched) Icons.Filled.CheckCircle else Icons.Filled.CheckCircle
                    val tint = if (isWatched) Color(0xFF4CAF50) else Color.White
                    Icon(imageVector = icon, contentDescription = "Visto", tint = tint)
                }
            }
        }
    }
}

/**
 * Función auxiliar para abrir una URL en el navegador externo.
 *
 * @param context Contexto de la aplicación.
 * @param url URL a abrir.
 */
fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    context.startActivity(intent)
}
