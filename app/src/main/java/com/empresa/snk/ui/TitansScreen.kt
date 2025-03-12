package com.empresa.snk.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
    paddingValues: PaddingValues,
    viewModel: GetTitansViewModel = hiltViewModel(),
    inheritorViewModel: GetCurrentInheritorViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val currentInheritors by inheritorViewModel.currentInheritors.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getTitans()

    }

    when (val current = state) {
        is titansState.Success -> {
            val titanes = current.titans
            titanes.forEach { titan ->
                titan.currentInheritor?.let {
                    if (!currentInheritors.containsKey(it)) {
                        inheritorViewModel.getCurrentInheritor(it)
                    }
                }
            }

            if (titanes.isNotEmpty()) {
                LazyColumn(
                    contentPadding = paddingValues,
                ) {

                    items(titanes) { titan ->


                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillParentMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(16.dp)) {
                                titan.img?.let {
                                    TitansImage(it)

                                }
                                Column(
                                    modifier = Modifier.padding(start = 16.dp)

                                ) {
                                    Text(text = titan.name ?: "Desconocido")
                                    Text(text = titan.height ?: "Desconocido")
                                    Text(text = titan.allegiance ?: "Desconocido")
                                    titan.currentInheritor?.let {
                                        val inheritor = currentInheritors[it] ?: "Desconocido"
                                        Text(text = inheritor)

                                    }
                                }

                            }
                        }
                    }
                }
            }
        }

        is titansState.Error -> {
            Text(text = current.message)
        }

        is titansState.Loading -> {
            CircularIndicator()
        }
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
            modifier = Modifier
                .clip(CircleShape)

        )
    }
}


@Composable
fun CircularIndicator() {
    CircularProgressIndicator(
        modifier = Modifier.width(64.dp),
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}