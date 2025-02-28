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
fun TitansScreen(
    innerPadding: PaddingValues,
    viewModel: GetTitansViewModel = hiltViewModel()
){
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getTitans()
    }

    when(val current = state){
        is titansState.Success -> {
            val titanes = current.titans
            if (titanes.isNotEmpty()) {
                LazyVerticalGrid(contentPadding = innerPadding,
                    columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2)
                ) {

                    items(titanes) { titan ->
                        Log.d("Greeting", "Character Name: ${titan.name}")
                        Column(modifier = Modifier.padding(16.dp)) {
                           titan.img?.let {
                                Log.d("Greeting", "Character Image URL: $it")
                              TitansImage(it)
                            }
                            Text(text = titan.name ?: "Desconocido")
                        }
                    }
                    item{
                        if(viewModel.hasMorePagesTitans()){
                            viewModel.getTitans()
                        }
                   
                    }
                }
            }

        }
        is titansState.Error -> {Text(text = current.message)}
        is titansState.Loading -> {}
    }

}
@Composable
fun TitansImage(imageUrl: String?) {
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