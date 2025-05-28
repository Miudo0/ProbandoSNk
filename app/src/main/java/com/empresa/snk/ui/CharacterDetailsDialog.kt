package com.empresa.snk.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.empresa.snk.domain.charactersDomain.Personaje

@Composable
fun CharacterDetailsDialog(
    personaje: Personaje,
    onDismiss: () -> Unit,
    viewModel: GetEpisodesDetailViewModel = hiltViewModel(),
    viewModelFamily: GetFamilyViewModel = hiltViewModel()
) {

    val episodeNames by viewModel.episodeName.collectAsState()
    val familyNames by viewModelFamily.family.collectAsState()
    //voltear la carta
    var flipped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(targetValue = if (flipped) 180f else 0f, label = "rotation")

    LaunchedEffect(personaje.episodes) {
        viewModel.getEpisodesName(personaje.episodes)
    }
    LaunchedEffect(personaje.relatives) {
        val familyUrls = personaje.relatives.flatMap { it.members }
        if (familyUrls.isNotEmpty()) {
            viewModelFamily.getFamily(familyUrls)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.Black.copy(alpha = 0.8f),
        titleContentColor = Color.White,
        textContentColor = Color.White,
        title = {
            Text(
                text = personaje.name ?: "Desconocido",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .graphicsLayer {
                                rotationY = rotation
                                cameraDistance = 8 * density
                                if (flipped) {
                                    scaleX = -1f
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (flipped) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                MostrarGrupos(personaje)
                                Text(text = "Family:")
                                when (val current = familyNames) {
                                    is FamilyState.Success -> {
                                        personaje.relatives.forEach { relative ->
                                            Text(text = relative.family ?: "Unknown Family")
                                        }
                                        Text(text = "Members:")
                                        current.family.forEach { family ->
                                            Text(text = family)
                                        }
                                    }

                                    is FamilyState.Error -> {
                                        Text(text = "Error al cargar la familia")
                                    }

                                    is FamilyState.Loading -> {}
                                }
                                //  Text(text = "Groups: ${personaje.groups.joinToString(", ")}")

                                when (val current = episodeNames) {
                                    is NombresEpisodeState.Success -> {
                                        Column {
                                            Text(text = "Episodes:")
                                            current.episodeNames.forEach { episode ->
                                                Text(text = episode)
                                            }
                                        }
                                    }

                                    is NombresEpisodeState.Error -> {
                                        Text(text = "Error al cargar los episodios")
                                    }

                                    is NombresEpisodeState.Loading -> {
                                        Text(text = "Cargando episodios...")
                                    }
                                }
                            }
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                personaje.img?.let {
                                    CharacterImageInfo(
                                        it,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    )
                                }
                                Row {
                                    personaje.age?.let { Text(text = "Age: $it") }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    personaje.gender?.let { Text(text = "Gender: $it") }
                                }
                                personaje.occupation?.let { Text(text = "Occupation: $it") }
                                personaje.birthplace?.let { Text(text = "Birthplace: $it") }
                                Text(text = "Species: ${personaje.species.joinToString(", ")}")
                                personaje.height?.let { Text(text = "Height: $it") }
                                personaje.residence?.let { Text(text = "Residence: $it") }
                                personaje.status?.let { Text(text = "Status: $it") }
                            }
                        }
                    }
                }
                item {
                    Button(onClick = { flipped = !flipped }) {
                        Text(if (flipped) "Mostrar información principal" else "Mostrar mas información")
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        },
        tonalElevation = 0.dp,
    )
}

@Composable
fun MostrarGrupos(personaje: Personaje) {
    Text(
        text = "Groups: ${
            personaje.groups.joinToString(", ") { group ->
                // Mostrar el nombre del grupo
                val subGroupsText = if (group.subGroups.isNotEmpty()) {
                    // Si el grupo tiene subgrupos, los mostramos también
                    " (${group.subGroups.joinToString(", ")})"
                } else {
                    "" // Si no tiene subgrupos, solo mostramos el nombre del grupo
                }
                "${group.name}$subGroupsText"
            }
        }"
    )
}