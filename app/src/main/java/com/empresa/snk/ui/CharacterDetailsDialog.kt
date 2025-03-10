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
import com.empresa.snk.domain.charactersDomain.Characters

@Composable
fun CharacterDetailsDialog(
    character: Characters,
    onDismiss: () -> Unit,
    viewModel: GetEpisodesDetailViewModel = hiltViewModel()
) {
   val episodeNames by viewModel.episodeName.collectAsState()
    LaunchedEffect(character.episodes) {
        viewModel.getEpisodesName(character.episodes)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = character.name ?: "Desconocido") },
        text = {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    Row() {
                        character.img?.let {
                            CharacterImageInfo(it)
                        }
                    }
                }
                item {
                    Row() {
                        character.age?.let {
                            Text(text = "Age: $it")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        character.gender?.let {
                            Text(text = "Gender: $it")
                        }
                    }
                }
                item {
                    Text(text = "Alias: ${character.alias}")
                }
                item {
                    character.occupation?.let {
                        Text(text = "occupation: $it")
                    }
                }
                item {
                    character.birthplace?.let {
                        Text(text = "Birthplace: $it")
                    }
                }
                item {
                    Text(text = "Species: ${character.species}")
                }
                item {
                    character.height?.let {
                        Text(text = "Height: $it")
                    }
                }
                item {
                    character.residence?.let {
                        Text(text = "Residence: $it")
                    }
                }
                item {

                    Text(text = "Status: ${character.status}")
                }
                item {

                    Text(text = "Roles: ${character.roles}")
                }
//
//                character.relatives?.let { relativesList ->
//                    // Iterar sobre la lista de Relatives
//                    for (relative in relativesList) {
//                        // Verifica si family no es nulo y muestra
//                        relative.family?.let {
//                            Text(text = "Family: $it")
//                        }
//
//                        // Verifica si members no es nulo y tiene elementos, y muestra la lista
//                        if (!relative.members.isNullOrEmpty()) {
//                            Text(text = "Members: ${relative.members.joinToString(", ")}")
//                        }
//                    }
//                }
                item {
                    character.status?.let {
                        Text(text = "Status: $it")
                    }
                }
                item {
                    Text(text = "Groups: ${character.groups.joinToString(", {name}")}")
                }
                item {
                  when(val current = episodeNames){
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