package com.empresa.snk.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SearchBar(

    modifier: Modifier = Modifier,
    getCharactersByNameViewmodel: GetCharactersByNameViewModel = hiltViewModel(),
    searchText: MutableState<String>
) {



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Barra de búsqueda
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically, // Asegúrate de que el contenido esté alineado verticalmente
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = searchText.value,
                onValueChange = { searchText.value = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },

                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                placeholder = {
                    Text(text = "Busqueda")
                },
                modifier = modifier
                    .weight(2f)
                    .heightIn(min = 56.dp)
            )
//            Button(
//                onClick = {
//                    Log.d("Busqueda", "Texto de búsqueda: ${searchText.value}")
//                    getCharactersByNameViewmodel.getCharactersFilter(searchText.value)
//
//                },
//                modifier = Modifier
//                    .height(56.dp)
//                    .weight(1f),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.tertiary,
//                    contentColor = MaterialTheme.colorScheme.onSecondary
//                )
//            ) {
//                Text(text = "Buscar")
//
//            }
        }
    }
}