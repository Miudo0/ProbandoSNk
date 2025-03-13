package com.empresa.snk.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun OrganizationsScreen(
    paddingValues: PaddingValues,
    viewModel: GetAllOrganizationsViewModel = hiltViewModel()
) {
    val state by viewModel.organizations.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getOrganizations()

    }

    when (val current = state) {
        is OrganizationsState.Success -> {
            val organizations = current.organizations
            if (organizations.isNotEmpty()) {
                LazyColumn(
                    contentPadding = paddingValues,
                    modifier = Modifier.padding(16.dp)
                ) {
                    items(organizations) { organization ->
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            organization.img?.let {
                                OrganizationImage(
                                    it,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                            }
                            Text(text = organization.name ?: "Desconocido")

                        }

                    }
                }

            }
        }

        is OrganizationsState.Error -> {
            Text(text = current.message)
        }

        is OrganizationsState.Loading -> {
            CircularIndicator()
        }
    }
}
@Composable
fun OrganizationImage(
    imageUrl: String?,
    modifier: Modifier = Modifier

) {
    imageUrl?.let {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(it)
                .crossfade(true)
                .build(),

            contentDescription = "Imagen de Organizacion",
            contentScale = ContentScale.Crop,
            modifier = modifier


        )
    }
}