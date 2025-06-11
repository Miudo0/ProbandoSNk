package com.empresa.snk.ui.utils


import android.content.Context
import android.media.MediaPlayer
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import com.empresa.snk.R
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop

/**
 * Reproduce un sonido cada vez que cambia la página del Pager.
 *
 * @param context  Contexto para crear el MediaPlayer.
 * @param soundRes Recurso de sonido en res/raw (por defecto page_swipe).
 */
@Composable
fun PagerState.PlaySoundOnPageChange(
    context: Context,
    soundRes: Int = R.raw.flick
) {
    // Reutilizamos un único MediaPlayer para eliminar latencia
    val player = remember { MediaPlayer.create(context, soundRes) }

    // Liberar recursos al salir de composición
    DisposableEffect(Unit) { onDispose { player.release() } }

    // Escuchar los cambios de página
    LaunchedEffect(this) {
        snapshotFlow { currentPage }
            .drop(1) //ASi no suena la primera
            .distinctUntilChanged()
            .collect {
                if (player.isPlaying) {
                    player.seekTo(0)
                } else {
                    player.start()
                }
            }
    }
}