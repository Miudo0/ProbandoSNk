package com.empresa.snk.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.empresa.snk.domain.charactersDomain.Personaje


/*
 * Pantalla de detalles de un personaje.
 * Muestra un AlertDialog a pantalla completa con:
 *   • Imagen superior recortada y pager inferior con tres páginas:
 *       0 → Información básica
 *       1 → Episodios donde aparece
 *       2 → Familia y grupos
 *   • El diálogo se puede cerrar deslizando hacia arriba o tocando fuera.
 *   • No hay botón de confirmación para mantener la estética limpia.
 * Autor: (tu nombre) – Mayo 2025
 */

@Composable
fun CharacterDetailsDialog(
    personaje: Personaje,
    onDismiss: () -> Unit,
    viewModel: GetEpisodesDetailViewModel = hiltViewModel(),
    viewModelFamily: GetFamilyViewModel = hiltViewModel()
) {
    // Al crear el diálogo solicitamos los nombres de los episodios del personaje
    LaunchedEffect(personaje.episodes) {
        viewModel.getEpisodesName(personaje.episodes)
    }
    // También pedimos información de familiares si existe al menos una URL
    LaunchedEffect(personaje.relatives) {
        val familyUrls = personaje.relatives.flatMap { it.members }
        if (familyUrls.isNotEmpty()) {
            viewModelFamily.getFamily(familyUrls)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},           // Eliminamos botones: UX de diálogo tipo hoja
        shape = RoundedCornerShape(24.dp), // Bordes redondeados coherentes con Material 3
        containerColor = Color.Black.copy(alpha = 0.8f), // Fondo semitransparente para destacar contenido
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    // Permite cerrar el diálogo con gesto de swipe‑up
                    .pointerInput(Unit) {
                        detectVerticalDragGestures { _, dragAmount ->
                            if (dragAmount < -80f) onDismiss()   // cerrar al deslizar hacia arriba
                        }
                    }
            ) {
                // Imagen principal del personaje (máx. 200 dp de alto)
                personaje.img?.let {
                    CharacterImageInfo(
                        it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)            // Limita la altura de la imagen
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                            .padding(bottom = 8.dp)
                    )
                }

                // ---------- Pager circular con 3 secciones -------------------
                val pages = 3
                val infinitePages = Int.MAX_VALUE
                val startPage = (infinitePages / 2) - (infinitePages / 2) % pages
                val pagerState = rememberPagerState(
                    initialPage = startPage,
                    pageCount = { infinitePages }
                )

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 220.dp),          // altura flexible pero mínimo 220
                    pageSpacing = 0.dp,                   // sin hueco entre páginas para que no se vean las laterales
                    pageSize = PageSize.Fill,             // cada página ocupa todo el ancho disponible
                    verticalAlignment = Alignment.Top
                ) { page ->
                    when (page % pages) {                  // modulo for infinite scroll
                        0 -> BasicInfoPage(personaje)      // Datos básicos
                        1 -> EpisodesPage(viewModel)       // Lista de episodios
                        2 -> FamilyPage(personaje, viewModelFamily) // Familia / grupos
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Indicadores de página (dots)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(pages) { i ->
                        val selected = (pagerState.currentPage % pages) == i
                        val dotSize = if (selected) 10.dp else 6.dp
                        Box(
                            modifier = Modifier
                                .size(dotSize)
                                .clip(CircleShape)
                                .background(
                                    if (selected) Color.White else Color.White.copy(alpha = 0.4f)
                                )
                        )
                        if (i < pages - 1) Spacer(Modifier.width(6.dp))
                    }
                }
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

// Fila reutilizable etiqueta‑valor para mantener alineación uniforme
@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp, horizontal = 12.dp),
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
            fontWeight = FontWeight.SemiBold
        )
    }
}

/*
 * Página 0 – Información básica del personaje
 */
@Composable
private fun BasicInfoPage(personaje: Personaje) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
    ) {
        personaje.age?.let { InfoRow("Age", it.toString()) }
        personaje.gender?.let { InfoRow("Gender", it) }
        personaje.occupation?.let { InfoRow("Occupation", it) }
        personaje.birthplace?.let { InfoRow("Birthplace", it) }
        InfoRow("Species", personaje.species.joinToString(", "))
        personaje.height?.let { InfoRow("Height", it) }
        personaje.residence?.let { InfoRow("Residence", it) }
        personaje.status?.let { InfoRow("Status", it) }
    }
}

/*
 * Página 1 – Episodios en los que aparece
 */
@Composable
private fun EpisodesPage(viewModel: GetEpisodesDetailViewModel) {
    val episodeNames by viewModel.episodeName.collectAsState()
    when (val current = episodeNames) {
        is NombresEpisodeState.Success -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
            ) {
                item {
                    Text(
                        "Episodes",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(vertical = 4.dp, horizontal = 12.dp)
                    )
                }
                itemsIndexed(current.episodeNames) { idx, ep ->
                    InfoRow("Ep ${idx + 1}", ep)
                }
            }
        }
        is NombresEpisodeState.Error -> Text("Error loading episodes")
        is NombresEpisodeState.Loading -> Text("Loading...")
    }
}

/*
 * Página 2 – Información de grupos y familia
 */
@Composable
private fun FamilyPage(personaje: Personaje, viewModel: GetFamilyViewModel) {
    val familyNames by viewModel.family.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 260.dp)   // algo más alto para dar aire
            .padding(horizontal = 12.dp)
    ) {
        // --- Groups ---------------------------------------------------------
        item {
            Text("Groups", fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp))
            Text(
                personaje.groups.joinToString(", ") { group ->
                    val sub = if (group.subGroups.isNotEmpty()) {
                        " (${group.subGroups.joinToString(", ")})"
                    } else ""
                    "${group.name}$sub"
                }
            )
            Spacer(Modifier.height(8.dp))
        }

        // --- Relatives ------------------------------------------------------
        item {
            Text("Relatives", fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp))
        }
        items(personaje.relatives) { rel ->
            InfoRow("Family", rel.family ?: "Unknown")
        }

        // --- Members fetched from API --------------------------------------
        when (val current = familyNames) {
            is FamilyState.Success -> {
                item {
                    Spacer(Modifier.height(8.dp))
                    Text("Members", fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp))
                }
                items(current.family) { member ->
                    Text(member, fontSize = 14.sp, modifier = Modifier.padding(vertical = 2.dp))
                }
            }
            is FamilyState.Error -> {
                item { Text("Error loading family") }
            }
            is FamilyState.Loading -> {
                item { Text("Loading...") }
            }
        }
    }
}