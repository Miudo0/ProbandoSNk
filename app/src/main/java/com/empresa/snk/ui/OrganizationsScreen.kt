package com.empresa.snk.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.empresa.snk.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganizationsScreen(
    paddingValues: PaddingValues,
    viewModel: GetAllOrganizationsViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Organizaciones • SNK",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        ),
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black.copy(alpha = 0.35f),
                    scrolledContainerColor = Color.Black.copy(alpha = 0.65f),
                    titleContentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.15f),
                                    Color.Transparent
                                )
                            ),
                            size = androidx.compose.ui.geometry.Size(size.width, 2.dp.toPx())
                        )
                    }
            )
        },
        bottomBar = {},
        content = { innerPadding ->
            OrganizationsContent(innerPadding, viewModel)
        }
    )
}

@Composable
fun OrganizationsContent(
    paddingValues: PaddingValues,
    viewModel: GetAllOrganizationsViewModel
) {
    val notableViewModel: GetNotableMembersViewModel = hiltViewModel()
    val notableMembers by notableViewModel.notableMembers.collectAsState()
    val state by viewModel.organizations.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getOrganizations()
    }

    Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
        Image(
            painter = painterResource(id = R.drawable.fondo_organizations),
            contentDescription = "Fondo de organizaciones",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.6f)
        )
        when (val current = state) {
            is OrganizationsState.Success -> {
                val organizations = current.organizations
                if (organizations.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        items(organizations) { organization ->
                            LaunchedEffect(organization.notableMembers) {
                                organization.notableMembers.forEach { url ->
                                    if (!notableMembers.containsKey(url)) {
                                        notableViewModel.getNotableMembers(url)
                                    }
                                }
                            }
                            Card(
                                shape = RoundedCornerShape(24.dp),
                                border = BorderStroke(
                                    1.dp,
                                    Brush.linearGradient(
                                        listOf(
                                            Color.White.copy(alpha = 0.4f),
                                            Color.White.copy(alpha = 0.2f)
                                        )
                                    )
                                ),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Black.copy(alpha = 0.75f),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .drawBehind {
                                        drawRoundRect(
                                            color = Color.White.copy(alpha = 0.05f),
                                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(24.dp.toPx(), 24.dp.toPx())
                                        )
                                    }
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = organization.name ?: "Desconocido",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))
                                    organization.img?.let {
                                        OrganizationImage(
                                            it,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(200.dp)
                                                .clip(RoundedCornerShape(16.dp))
                                        )

                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = organization.occupations.joinToString(", "),
                                        style = MaterialTheme.typography.bodySmall
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = organization.affiliation ?: "Desconocido",
                                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.7f))
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Miembros destacados:",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White.copy(alpha = 0.8f)
                                        )
                                    )
                                    if (organization.notableMembers.isEmpty()) {
                                        Text(
                                            text = "- Sin datos",
                                            style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.5f))
                                        )
                                    } else {
                                        organization.notableMembers.forEach { url ->
                                            val nombre = notableMembers[url] ?: "Cargando..."
                                            Text(
                                                text = "- $nombre",
                                                style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
                                            )
                                        }
                                    }
                                }
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