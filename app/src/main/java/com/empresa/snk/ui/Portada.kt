package com.empresa.snk.ui

// =========================================================================
// PORTADA PRINCIPAL
// -------------------------------------------------------------------------
// Este archivo dibuja la pantalla inicial con:
//   • Video de fondo a pantalla completa (ExoPlayer + PlayerView)
//   • Capa “scrim” (degradado) para mejorar contraste
//   • Carrusel infinito con imágenes + título y puntos indicadores
// =========================================================================

// ---------- IMPORTS -------------------------------------------------------
import androidx.annotation.OptIn
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.empresa.snk.R
import com.empresa.snk.ui.utils.PlaySoundOnPageChange
import com.empresa.snk.ui.utils.clickWithSound
import kotlin.math.abs


@OptIn(UnstableApi::class)
@Composable
private fun VideoBackground(videoResId: Int) {
    val context = LocalContext.current

    // Creamos y preparamos ExoPlayer una sola vez (remember) — ciclo de vida del Composable
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem =
                MediaItem.fromUri("android.resource://${context.packageName}/$videoResId".toUri())
            setMediaItem(mediaItem)
            repeatMode = Player.REPEAT_MODE_ALL
            playWhenReady = true
            videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            prepare()
        }
    }

    // Liberamos recursos cuando el Composable sale de composición
    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    AndroidView(
        factory = {
            // PlayerView nativo embebido dentro de Compose
            PlayerView(it).apply {
                player = exoPlayer
                useController = false
                // scale‑to‑fit con zoom; llena toda la pantalla evitando bandas negras
                // evita el re‑ajuste lento del SurfaceView
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                setShutterBackgroundColor(android.graphics.Color.TRANSPARENT)
                layoutParams = android.view.ViewGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun PortadaScreen(
    navigateToCharacters: () -> Unit,
    navigateToTitans: () -> Unit,
    navigateToEpisodes: () -> Unit,
    navigateToOrganizations: () -> Unit,
    navigateToLocations: () -> Unit
) {
    PortadaContent(
        navigateToCharacters,
        navigateToTitans,
        navigateToEpisodes,
        navigateToOrganizations,
        navigateToLocations
    )
}

@Composable
fun PortadaContent(
    navigateToCharacters: () -> Unit,
    navigateToTitans: () -> Unit,
    navigateToEpisodes: () -> Unit,
    navigateToOrganizations: () -> Unit,
    navigateToLocations: () -> Unit
) {

    // ---------- UI de Portada ------------------------------------------------
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        VideoBackground(videoResId = R.raw.fondo_portada)
        // Degradado vertical (“scrim”) sobre el vídeo
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = .55f),
                            Color.Transparent,
                            Color.Black.copy(alpha = .75f)
                        )
                    )
                )
        )
        // Carrusel + título (en una columna centrada)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter) // Pone el carrusel más arriba
                .padding(top = 160.dp) // Espacio superior en vez de altura fija
        ) {
            // Logo en la parte superior
            Image(
                painter = painterResource(id = R.drawable.logo_portada),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(80.dp)
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
            CarouselCircular(
                navigateToCharacters,
                navigateToTitans,
                navigateToEpisodes,
                navigateToOrganizations,
                navigateToLocations
            )
        }
    }
}

@Composable
fun CarouselCircular(
    navigateToCharacters: () -> Unit,
    navigateToTitans: () -> Unit,
    navigateToEpisodes: () -> Unit,
    navigateToOrganizations: () -> Unit,
    navigateToLocations: () -> Unit
) {
    val context = LocalContext.current
    // Pares (imagen, título) que se mostrarán en el carrusel
    val images = listOf(
        R.drawable.characters to "Personajes",
        R.drawable.titans to "Titanes",
        R.drawable.episodes to "Episodios",
        R.drawable.organizations to "Organizaciones",
        R.drawable.locations to "Ubicaciones"
    )
    // Pager infinito: comenzamos en la mitad de Int.MAX_VALUE para simular scroll circular
    val infinitePages = Int.MAX_VALUE // Simula páginas infinitas
    val startPage = (infinitePages / 2) - (infinitePages / 2) % images.size
    val pagerState = rememberPagerState(initialPage = startPage, pageCount = { infinitePages })

    //calcular el centro
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val padding = (screenWidth - 220.dp) / 2
    pagerState.PlaySoundOnPageChange(context)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // ---------- Indicadores (puntos) ---------------------------------
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                images.forEachIndexed { i, _ ->
                    val selected = (pagerState.currentPage % images.size) == i
                    val dotSize = if (selected) 10.dp else 6.dp

                    Box(
                        modifier = Modifier
                            .size(dotSize)
                            .clip(CircleShape)
                            .background(
                                if (selected) Color.White else Color.White.copy(alpha = 0.4f)
                            )
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            // ---------- Carrusel principal -----------------------------------
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(),
                pageSpacing = 8.dp, // Espacio entre imágenes
                contentPadding = PaddingValues(horizontal = padding),
                beyondViewportPageCount = 1,
                pageSize = PageSize.Fixed(220.dp),

                verticalAlignment = Alignment.CenterVertically,


                ) { index ->
                val (imageRes, title) = images[index % images.size]

                // Fuente de interacción para detectar si el usuario está pulsando la tarjeta
                val interactionSource = remember { MutableInteractionSource() }
                // Estado booleano que indica si la tarjeta está siendo presionada
                val pressed by interactionSource.collectIsPressedAsState()
                // Animación de escala: si está presionada, reduce el tamaño ligeramente
                val scale by animateFloatAsState(if (pressed) 0.96f else 1f, label = "scale")

                val pageOffset = (index - pagerState.currentPage).toFloat()
                val isMainPage = abs(pageOffset) < 0.5f

                val borderColor = when (index % images.size) {
                    0 -> Color(0xFF64B5F6) // Azul claro - Personajes
                    1 -> Color(0xFFE57373) // Rojo claro - Titanes
                    2 -> Color(0xFFBA68C8) // Violeta - Episodios
                    3 -> Color(0xFF81C784) // Verde - Organizaciones
                    4 -> Color(0xFFFFD54F) // Amarillo - Ubicaciones
                    else -> Color.White.copy(alpha = 0.35f)
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        colors = androidx.compose.material3.CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.15f)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .padding(8.dp)
                            .clickWithSound(context, interactionSource = interactionSource) {
                                when (index % images.size) {
                                    0 -> navigateToCharacters()
                                    1 -> navigateToTitans()
                                    2 -> navigateToEpisodes()
                                    3 -> navigateToOrganizations()
                                    4 -> navigateToLocations()
                                }
                            }
                            .graphicsLayer {
                                // Animaciones 3D (escala, rotación, alfa)
                                scaleX = scale - 0.05f * abs(pageOffset)
                                scaleY = scale - 0.05f * abs(pageOffset)
                                translationY = -abs(pageOffset) * 15f
                                alpha = 1f - 0.1f * abs(pageOffset)
                                rotationY = pageOffset * 10f
                                cameraDistance = 8f
                            }
                            .border(
                                1.5.dp,
                                borderColor,
                                RoundedCornerShape(16.dp)
                            )
                            .size(240.dp)
                    ) {
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    if (isMainPage) {
                        Text(
                            text = title.uppercase(),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            modifier = Modifier
                                .padding(top = 8.dp)
                        )
                    }

                }
            }
        }

    }
}