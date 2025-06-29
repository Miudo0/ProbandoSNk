/*
 * Pantalla principal de personajes de Shingeki no Kyojin.
 * Incluye:
 *   • AppBar translúcido con degradado inferior.
 *   • Barra de búsqueda reactiva (debounce 500 ms).
 *   • Grid 2×N con tarjetas “glass” animadas.
 *   • Al pulsar una tarjeta se abre un diálogo con detalles.
 * Autor: (tu nombre) – Mayo 2025
 */
package com.empresa.snk.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
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
import com.empresa.snk.domain.charactersDomain.Personaje
import com.empresa.snk.ui.utils.clickWithSound
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonjesScreen(paddingValues: PaddingValues) {
    // Scaffold que aloja la TopAppBar y el contenido de personajes

    Scaffold(
        topBar = {
            TopAppBar(
                // Título fijo
                title = {
                    Text(
                        "Personajes • SNK",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        ),
                        color = Color.White
                    )
                },
                // Colores translúcidos y degradado al hacer scroll
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black.copy(alpha = 0.35f),
                    scrolledContainerColor = Color.Black.copy(alpha = 0.65f),
                    titleContentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    // Línea sutil inferior mediante drawRect
                    .drawBehind {
                        // subtle bottom gradient separator
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.15f),
                                    Color.Transparent
                                ),
                                tileMode = TileMode.Clamp
                            ),
                            size = androidx.compose.ui.geometry.Size(size.width, 2.dp.toPx())
                        )
                    }
            )
        },
        content = { innerPadding ->
            // Contenido principal: búsqueda + grid de personajes
            PersonajesContent(innerPadding)
        }
    )
}
@Composable
fun PersonajesContent(paddingValues: PaddingValues) {
    // Estado para búsqueda y personaje seleccionado
    val searchText = remember { mutableStateOf("") }
    val selectedPersonaje = remember { mutableStateOf<Personaje?>(null) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Imagen de fondo con ligera transparencia
        Image(
            painter = painterResource(id = R.drawable.fondocharacters),
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.6f)
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // Barra de búsqueda reactiva
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
                searchText = searchText
            )
            // Grid de tarjetas filtrado por texto
            Personajes(
                searchText = searchText,
                onCharacterClick = { character -> selectedPersonaje.value = character }
            )
            // Diálogo con detalles cuando hay un personaje seleccionado
            selectedPersonaje.value?.let { character ->
                CharacterDetailsDialog(
                    personaje = character,
                    onDismiss = { selectedPersonaje.value = null })
            }
        }
    }
}


@Composable
fun Personajes(
    searchText: MutableState<String>,
    onCharacterClick: (Personaje) -> Unit,
    viewModel: GetCharactersViewModel = hiltViewModel(),
    getCharactersByNameViewmodel: GetCharactersByNameViewModel = hiltViewModel(),


    ) {
    // ViewModels para lista completa y filtrada
    val busqueda by getCharactersByNameViewmodel.state.collectAsState(GetCharactersByNameViewModel.FilterState.Loading)
    val state by viewModel.characters.collectAsState(CharacterState.Loading)

    // Debounce de 500 ms: evita llamadas excesivas mientras se escribe
    LaunchedEffect(searchText.value) {
        delay(500)
        Log.d("Personajes", "Búsqueda 2 iniciada con texto: ${searchText.value}")
        if (searchText.value.isNotEmpty()) {
            getCharactersByNameViewmodel.getCharactersFilter(searchText.value)
        } else {
            viewModel.getCharacters()
        }
    }

    if ((searchText.value.isEmpty() && state is CharacterState.Loading) ||
        (searchText.value.isNotEmpty() && busqueda is GetCharactersByNameViewModel.FilterState.Loading)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularIndicator()
        }
        return
    }

    // Decide qué lista mostrar: completa o filtrada
    val personajes = when {
        searchText.value.isEmpty() -> {
            when (val current = state) {
                is CharacterState.Success -> current.personajes
                else -> emptyList()
            }
        }
        else -> {
            when (val currentBusqueda = busqueda) {
                is GetCharactersByNameViewModel.FilterState.Success -> currentBusqueda.personajes
                else -> emptyList()
            }
        }
    }

    // Grid 2 × N con espaciado uniforme
    LazyVerticalGrid(
        columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 8.dp,
            end = 8.dp,
            top = 0.dp,
            bottom = 8.dp
        ),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.Top)
    ) {
        items(personajes) { character ->
            // Animación de fade + scale al aparecer cada tarjeta
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(300)) +
                        scaleIn(initialScale = 0.9f, animationSpec = tween(300))
            ) {
                CharacterGlassCard(character = character) { onCharacterClick(character) }
            }
        }
        item {
            if (viewModel.hasMorePages()) {
                viewModel.getCharacters()
            }
        }
    }
}


@Composable
fun CharacterImage(imageUrl: String?) {
    imageUrl?.let {
        // Imagen circular con borde y fondo sutil
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp) // Añadir espaciado
                .clip(CircleShape) // Mantener la forma circular
                .border(2.dp, Color.Gray, CircleShape) // Borde sutil
                .background(Color.Black.copy(alpha = 0.1f)) // Fondo sutil
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(it)
                    .crossfade(true)
                    .build(),

                contentDescription = "Imagen del personaje",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape), // Mantener la forma circular en la imagen también

                alignment = androidx.compose.ui.Alignment.Center

            )
        }
    }
}

@Composable
fun CharacterGlassCard(
    character: Personaje,
    onClick: () -> Unit
) {
    // Tarjeta estilo "glassmorphism" con escala al presionar
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.96f else 1f, label = "scale")

    val context = LocalContext.current

    // Wrapper Card con borde degradado y fondo semitransparente
    Card(
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(
            1.dp,
            Brush.linearGradient(
                listOf(
                    Color.White.copy(alpha = 0.4f),
                    Color.White.copy(alpha = 0.2f)
                )
            )
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.5f),
            contentColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 0.dp)
            .fillMaxWidth()
            .aspectRatio(1f)
            .drawBehind {
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.05f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(24.dp.toPx(), 24.dp.toPx())
                )
            }
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clickWithSound(context, interactionSource) { onClick() }

    ) {
        // Contenido de la tarjeta: imagen + nombre
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            character.img?.let {
                Box(modifier = Modifier.size(96.dp)) {
                    CharacterImage(it)
                }
            }
            Text(
                text = character.name ?: "Desconocido",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,

                style = MaterialTheme.typography.titleMedium.copy(
                    letterSpacing = 0.5.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color.White,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun CharacterImageInfo(imageUrl: String?, modifier: Modifier = Modifier) {
    // Versión rectangular de la imagen con sombra y borde
    imageUrl?.let {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)) // Bordes redondeados
                .border(2.dp, Color.Gray, RoundedCornerShape(16.dp)) // Borde sutil
                .shadow(8.dp, RoundedCornerShape(16.dp), clip = true) // Sombra sutil
        ) {

            Log.d("CharacterImage", "URL de la imagen: $it")
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(it)
                    .crossfade(true)

                    .build(),

                contentDescription = "Imagen del personaje",
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .fillMaxWidth()


            )
        }
    }
}
