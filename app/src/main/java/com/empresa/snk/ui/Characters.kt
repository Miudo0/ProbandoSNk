package com.empresa.snk.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
fun PersonajesScreen(
    innerPadding: PaddingValues,
    viewModel: GetCharactersViewModel = hiltViewModel()

) {

    val state by viewModel.characters.collectAsState(CharacterState.Loading)


    LaunchedEffect(Unit) {
        viewModel.getCharacters()
    }

    when (val current = state) {

        is CharacterState.Success -> {
            val personajes = current.characters
            if (personajes.isNotEmpty()) {
                LazyVerticalGrid(contentPadding = innerPadding,
                    columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2)
                    ) {

                    items(personajes) { character ->
                        Column(modifier = Modifier.padding(16.dp)) {
                            character.img?.let {
                                Log.d("Greeting", "Character Image URL: $it")
                                CharacterImage(it)
                            }
                            Text(text = character.name ?: "Desconocido")
                        }
                    }
                    item{
                        if(viewModel.hasMorePages()){
                            viewModel.getCharacters()
                        }
                        Text(text = "Cargando más...")
                    }
                }
            }
        }

        is CharacterState.Error -> {
            Text(text = current.message)
        }

        is CharacterState.Loading -> {
            Text(text = "Cargando")
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