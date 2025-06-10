package com.empresa.snk.ui.utils


import android.content.Context
import android.media.MediaPlayer
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier
import com.empresa.snk.R

fun Modifier.clickWithSound(
    context: Context,
    interactionSource: MutableInteractionSource? = null,
    onClick: () -> Unit
): Modifier {
    return this.then(
        Modifier.clickable(
         interactionSource= interactionSource,
            indication = null
        ) {
            // Puedes elegir entre un sonido del sistema o un sonido personalizado:

            // 1. Sonido del sistema
//            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
//            audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK)

            // 2. Sonido personalizado
             try {
                 val afd = context.resources.openRawResourceFd(R.raw.click)
                 val player = MediaPlayer()
                 player.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                 afd.close()
                 player.setOnCompletionListener { it.release() }
                 player.prepare()
                 player.start()
             } catch (e: Exception) {
                 e.printStackTrace()
             }

            onClick()
        }
    )
}