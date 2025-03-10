package com.empresa.snk.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.snk.domain.GetEpisodeDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetEpisodesDetailViewModel @Inject constructor(
    private val getEpisodeDetailUseCase: GetEpisodeDetailUseCase
) : ViewModel() {

    private val _episodeName = MutableStateFlow<NombresEpisodeState>(NombresEpisodeState.Loading)
    val episodeName = _episodeName

    fun getEpisodesName(episodeUrl: List<String>) {
        viewModelScope.launch {
            try {
                Log.d("GetEpisodesDetailViewModel", "Iniciando carga de episodios: $episodeUrl")

                val episodeNames = episodeUrl.map { url ->
                    async {
                        Log.d("GetEpisodesDetailViewModel", "Obteniendo detalles para: $url")
                        getEpisodeDetailUseCase(url)
                    }
                }.awaitAll()

                Log.d("GetEpisodesDetailViewModel", "Episodios obtenidos: $episodeNames")
                _episodeName.value = NombresEpisodeState.Success(episodeNames)

            } catch (e: Exception) {
                Log.e("GetEpisodesDetailViewModel", "Error al obtener episodios", e)
                _episodeName.value = NombresEpisodeState.Error
            }
        }
    }

}

sealed interface NombresEpisodeState {
    data class Success(val episodeNames: List<String>) : NombresEpisodeState
    object Loading : NombresEpisodeState
    object Error : NombresEpisodeState
}

