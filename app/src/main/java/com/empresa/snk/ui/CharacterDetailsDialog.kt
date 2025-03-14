package com.empresa.snk.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
        title = {
            Text(
                text = personaje.name ?: "Desconocido",
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
                },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        personaje.img?.let {
                            CharacterImageInfo(
                                it,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
                item {
                    Row() {
                        personaje.age?.let {
                            Text(text = "Age: $it")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        personaje.gender?.let {
                            Text(text = "Gender: $it")
                        }
                    }
                }
                item {
                    Text(text = "Alias: ${personaje.alias.joinToString(", ")}")
                }
                item {
                    personaje.occupation?.let {
                        Text(text = "Occupation: $it")
                    }
                }
                item {
                    personaje.birthplace?.let {
                        Text(text = "Birthplace: $it")
                    }
                }
                item {
                    Text(text = "Species: ${personaje.species.joinToString(", ")}")
                }
                item {
                    personaje.height?.let {
                        Text(text = "Height: $it")
                    }
                }
                item {
                    personaje.residence?.let {
                        Text(text = "Residence: $it")
                    }
                }

                item {

                    when (val current = familyNames) {
                        is FamilyState.Success -> {
                            Row {
                                Text(text = "family:")
                                personaje.relatives.forEach { relative ->
                                    Text(text = relative.family ?: "Unknown Family")
                                }
                            }

                            Column {
                                Text(text = "Members:")
                                current.family.forEach { family ->
                                    Text(text = family)
                                }
                            }
                        }

                        is FamilyState.Error -> {
                            Text(text = "Error al cargar la familia")
                        }

                        is FamilyState.Loading -> {}
                    }

                }
                item {
                    personaje.status?.let {
                        Text(text = "Status: $it")
                    }
                }
                item {
                    Text(text = "Groups: ${personaje.groups.joinToString(", {name}")}")
                }
                item {
                    when (val current = episodeNames) {
                        is NombresEpisodeState.Success -> {
                            Column {
                                Text(text = "Episodes:")
                                current.episodeNames.forEach { episode ->
                                    Text(text = episode)

                                }
                            }
//                          Text(text = "Episodes: ${current.episodeNames.joinToString(", ")}")
                        }

                        is NombresEpisodeState.Error -> {
                            Text(text = "Error al cargar los episodios")
                        }

                        is NombresEpisodeState.Loading -> {
                            Text(text = "Cargando episodios...")
                        }
                    }
                }

            }

        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}