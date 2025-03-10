package com.empresa.snk.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.empresa.snk.domain.charactersDomain.Characters

@Composable
fun PersonajesScreen(paddingValues: PaddingValues) {
    val searchText = remember { mutableStateOf("") }
    val selectedCharacter = remember { mutableStateOf<Characters?>(null) }
    Column(modifier = Modifier.fillMaxSize()) {

        SearchBar(modifier = Modifier.padding(paddingValues), searchText = searchText)

        Personajes(
            searchText = searchText,
            onCharacterClick = { character -> selectedCharacter.value = character }
        )
        selectedCharacter.value?.let { character ->
            CharacterDetailsDialog(
                character = character,
                onDismiss = { selectedCharacter.value = null })
        }
    }

}



@Composable
fun Personajes(
    searchText: MutableState<String>,
    onCharacterClick: (Characters) -> Unit,
    viewModel: GetCharactersViewModel = hiltViewModel(),
    getCharactersByNameViewmodel: GetCharactersByNameViewModel = hiltViewModel(),


) {
    val busqueda by getCharactersByNameViewmodel.state.collectAsState(GetCharactersByNameViewModel.FilterState.Loading)
    val state by viewModel.characters.collectAsState(CharacterState.Loading)


    // Realiza la búsqueda cada vez que el searchText cambia
    LaunchedEffect(searchText.value) {
        if (searchText.value.isNotEmpty()) {
            getCharactersByNameViewmodel.getCharactersFilter(searchText.value)
        } else {
            viewModel.getCharacters()
        }
    }

    val personajes = when {
        searchText.value.isEmpty() -> {
            when (val current = state) {
                is CharacterState.Success -> current.characters
                else -> emptyList()
            }
        }

        else -> {
            when (val currentBusqueda = busqueda) {
                is GetCharactersByNameViewModel.FilterState.Success -> currentBusqueda.characters
                else -> emptyList()
            }
        }
    }

    // Mostrar los personajes en una lista o grid
    LazyVerticalGrid(
        columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2)
    ) {
        items(personajes) { character ->
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        onCharacterClick(character)
                    }
            ) {
                character.img?.let {
                    CharacterImage(it)
                }
                Text(text = character.name ?: "Desconocido")
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
        Log.d("CharacterImage", "URL de la imagen: $it")
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

@Composable
fun CharacterImageInfo(imageUrl: String?) {
    imageUrl?.let {
        Log.d("CharacterImage", "URL de la imagen: $it")
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(it)
                .crossfade(true)
                .build(),

            contentDescription = "Imagen del personaje",
            contentScale = ContentScale.Crop,
            modifier = Modifier


        )
    }
}