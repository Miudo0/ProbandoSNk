package com.empresa.snk.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
    inheritorViewModel: GetCurrentInheritorViewModel = hiltViewModel(),
    formerInheritorViewModel: GetFormerInheritorsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val currentInheritors by inheritorViewModel.currentInheritors.collectAsState()
    val formerInheritors by formerInheritorViewModel.formerInheritors.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getTitans()
    }


    when (val current = state) {
        is titansState.Success -> {
            val titanes = current.titans
            //puesteo asi porque me hacia bucle infinito en lazycolumn
            titanes.forEach { titan ->
                titan.currentInheritor?.let {
                    if (!currentInheritors.containsKey(it)) {
                        inheritorViewModel.getCurrentInheritor(it)
                    }
                }
                titan.formerInheritors.let {
                   if(!formerInheritors.containsKey(titan.id)){
                       //asi por si es nulo
                       titan.id?.let { it1 -> formerInheritorViewModel.getFormerInheritors(it1, it) }
                   }
                }
            }

            if (titanes.isNotEmpty()) {
                LazyColumn(
                    contentPadding = paddingValues,
                    modifier = Modifier.padding(16.dp)
                ) {

                    items(titanes) { titan ->
                        Card(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillParentMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),

                        ) {

                            Row(modifier = Modifier.padding(16.dp)) {
                                titan.img?.let {
                                    TitansImage(it)

                                }

                                Column(
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .align(Alignment.CenterVertically)

                                ) {

                                    Text(
                                        text = titan.name ?: "Desconocido",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    Text(
                                        text = "Height: " + (titan.height ?: "Desconocido"),
                                        style = MaterialTheme.typography.bodySmall

                                    )
                                    Text(
                                        text ="Allegiance: "+ (titan.allegiance ?: "Desconocido"),
                                        style = MaterialTheme.typography.bodySmall

                                    )
                                    Text(
                                        text = "Abilities: " + titan.abilities.joinToString(", "),
                                        style = MaterialTheme.typography.bodySmall
                                        )
                                    titan.currentInheritor?.let {
                                        val inheritor = currentInheritors[it] ?: "Desconocido"
                                        Text(
                                            text = "Current Inheritor: $inheritor",
                                            style = MaterialTheme.typography.bodySmall
                                            )

                                    }
                                    // Mostrar los herederos anteriores
                                    when (val current = formerInheritors[titan.id]) {
                                        is FormerInheritorsState.Succes -> {
                                            Text(text = "Former Inheritors: ",
                                                style = MaterialTheme.typography.bodySmall)
                                            current.inheritors.forEach { inheritor ->
                                                Text(text = inheritor,
                                                    style = MaterialTheme.typography.bodySmall
                                                    )
                                            }
                                        }
                                        is FormerInheritorsState.Error -> {
                                            Text(text = "Error al cargar los herederos anteriores")
                                        }
                                        is FormerInheritorsState.Loading -> {

                                        }
                                        null -> {}

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
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, Color.Black) // Borde negro
                .shadow(4.dp, RoundedCornerShape(16.dp))

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