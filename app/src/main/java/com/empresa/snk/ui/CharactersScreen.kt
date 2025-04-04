package com.empresa.snk.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.empresa.snk.R
import com.empresa.snk.domain.charactersDomain.Personaje
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonjesScreen(paddingValues: PaddingValues) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Personajes de Attack on Titan") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },

        content = { innerPadding ->
            PersonajesContent(innerPadding)
        }
    )
}
@Composable
fun PersonajesContent(paddingValues: PaddingValues) {
    val searchText = remember { mutableStateOf("") }
    val selectedPersonaje = remember { mutableStateOf<Personaje?>(null) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondocharacters),
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.6f)
        )

        Column(modifier = Modifier.fillMaxSize()) {
            //  SnkTopAppBar( searchText  = searchText)
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
                searchText = searchText
            )
            //  SearchBar(searchText = searchText,  modifier = Modifier.fillMaxWidth())
            Personajes(
                searchText = searchText,

                onCharacterClick = { character -> selectedPersonaje.value = character }
            )
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
    val busqueda by getCharactersByNameViewmodel.state.collectAsState(GetCharactersByNameViewModel.FilterState.Loading)
    val state by viewModel.characters.collectAsState(CharacterState.Loading)

    // Realiza la búsqueda cada vez que el searchText cambia
    LaunchedEffect(searchText.value) {
        delay(500)
        Log.d("Personajes", "Búsqueda 2 iniciada con texto: ${searchText.value}")
        if (searchText.value.isNotEmpty()) {
            getCharactersByNameViewmodel.getCharactersFilter(searchText.value)
        } else {
            viewModel.getCharacters()
        }
    }

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

    // Mostrar los personajes en una lista o grid
    LazyVerticalGrid(
        columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.spacedBy(8.dp)

    ) {
        items(personajes) { character ->
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        onCharacterClick(character)
                    }
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center

            ) {
                character.img?.let {
                    CharacterImage(it)
                }
                Text(
                    text = character.name ?: "Desconocido",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge

                )
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp) // Añadir espaciado
                .clip(CircleShape) // Mantener la forma circular
                .border(2.dp, Color.Gray, CircleShape) // Borde sutil
                .shadow(8.dp, CircleShape, clip = true) // Sombra sutil
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
fun CharacterImageInfo(imageUrl: String?, modifier: Modifier = Modifier) {
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