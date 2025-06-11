package com.empresa.snk.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.empresa.snk.R
import com.empresa.snk.domain.titansDomain.Titan
import com.empresa.snk.ui.utils.PlaySoundOnPageChange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitansScreen(paddingValues: PaddingValues) {
    Scaffold(
        topBar = {
            TopAppBar(
                // Título fijo estilo personajes
                title = {
                    Text(
                        "Titanes • SNK",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        ),
                        color = Color.White
                    )
                },
                // Colores translúcidos y degradado como en CharactersScreen
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black.copy(alpha = 0.35f),
                    scrolledContainerColor = Color.Black.copy(alpha = 0.65f),
                    titleContentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        // Línea inferior sutil con gradiente blanco → transparente
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
            TitansContent(innerPadding)
        }
    )
}

@Composable
fun TitansContent(
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            // Aplica automáticamente todos los PaddingValues del Scaffold
            .padding(paddingValues)
    ) {
        Image(
            painter = painterResource(id = R.drawable.titanesfondo),
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.6f)
        )
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
                        if (!formerInheritors.containsKey(titan.id)) {
                            //asi por si es nulo
                            titan.id?.let { it1 ->
                                formerInheritorViewModel.getFormerInheritors(
                                    it1,
                                    it
                                )
                            }
                        }
                    }
                }

                if (titanes.isNotEmpty()) {
                    LazyColumn(
                        // Solo respetamos el padding inferior del Scaffold; el superior ya lo lleva el Box
                        contentPadding = PaddingValues(bottom = paddingValues.calculateBottomPadding()),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        items(titanes) { titan ->
                            val currentInheritorName = titan.currentInheritor?.let { currentInheritors[it] }
                            val formerInheritorsList = when (val current = formerInheritors[titan.id]) {
                                is FormerInheritorsState.Succes -> current.inheritors
                                else -> emptyList()
                            }
                         TitanCard(
                                titan = titan,
                                currentInheritorName = currentInheritorName,
                                formerInheritors = formerInheritorsList
                            )
                        }
                    }
                }
            }

            is titansState.Error -> {
                Text(text = current.message)
            }

            is titansState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularIndicator()
                }
            }
        }
    }
}

/** Tarjeta al estilo glassmorphism coherente con CharacterScreen */
@Composable
fun TitanCard(
    titan: Titan,
    currentInheritorName: String?,
    formerInheritors: List<String>
) {
    val context = LocalContext.current
    // Pager interno para la tarjeta (3 páginas)
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })

    pagerState.PlaySoundOnPageChange(context)

    // Efecto de escala al presionar (igual que CharacterGlassCard)
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.96f else 1f, label = "scale")

    Card(
        onClick = {},                       // sin acción, solo informativa
        interactionSource = interactionSource,
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
            containerColor = Color.Black.copy(alpha = 0.8f),
            contentColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 0.dp)
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.05f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(24.dp.toPx(), 24.dp.toPx())
                )
            }
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            // Nombre
            Text(
                text = titan.name ?: "Unknown",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            // Imagen más grande: ocupa ~60 % del ancho de la tarjeta y mantiene proporción 1:1
            titan.img?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)   // aumenta la altura
                ) {
                    TitansImage(
                        it,
                        modifier = Modifier.fillMaxSize()   // la imagen rellena todo el Box
                    )
                }
                Spacer(Modifier.height(8.dp))
            }

            // ---------- Pager con 3 secciones -----------------
            HorizontalPager(
                state = pagerState,
                pageSize = PageSize.Fill,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp)
            ) { page ->
                when (page) {
                    0 -> {
                        Column {
                            InfoRow("Height", titan.height ?: "Unknown")
                            InfoRow("Allegiance", titan.allegiance ?: "Unknown")
                        }
                    }
                    1 -> {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Abilities",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = titan.abilities.joinToString(", "),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }
                    2 -> {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            currentInheritorName?.let {
                                InfoRow("Current", it)
                                Spacer(Modifier.height(8.dp))
                            }

                            if (formerInheritors.isNotEmpty()) {
                                Text(
                                    text = "Former",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                formerInheritors.forEach { name ->
                                    Text(
                                        text = name,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = Color.White,
                                        modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
                                    )
                                }
                            } else {
                                Text(
                                    text = "No former inheritors",
                                    fontSize = 14.sp,
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(6.dp))

            // Indicadores (dots)
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(3) { i ->
                    val selected = pagerState.currentPage == i
                    val size = if (selected) 10.dp else 6.dp
                    Box(
                        modifier = Modifier
                            .size(size)
                            .clip(CircleShape)
                            .background(if (selected) Color.White else Color.White.copy(alpha = 0.4f))
                    )
                    if (i < 2) Spacer(Modifier.width(6.dp))
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

@Composable
fun TitansImage(imageUrl: String?, modifier: Modifier = Modifier) {
    imageUrl?.let {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(it)
                .crossfade(true)
                .build(),

            contentDescription = "Imagen del personaje",
            contentScale = ContentScale.Crop,
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, Color.Black) // Borde negro
                .shadow(4.dp, RoundedCornerShape(16.dp))

        )
    }
}


