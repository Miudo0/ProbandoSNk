package com.empresa.snk.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.snk.domain.episodesDomain.Episodes
import com.empresa.snk.domain.GetEpisodesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GetEpisodesViewModel @Inject constructor(
    private val getEpisodesUseCase: GetEpisodesUseCase
): ViewModel() {

    private val _episodes = MutableStateFlow<EpisodesState>(EpisodesState.Loading)
    val episodes = _episodes
    private var nextPage: String? = null

    fun getEpisodes(){
        viewModelScope.launch {
            val response = getEpisodesUseCase(nextPage) // Pasar nextPage si existe
            nextPage = response.info?.nextPage // Guardar la próxima página

            // Obtener la lista actual de episodios y agregar los nuevos
            val currentList = (_episodes.value as? EpisodesState.Success)?.episodes ?: emptyList()
            val newEpisodes = response.results ?: emptyList()

            _episodes.value = EpisodesState.Success(currentList + newEpisodes)
        }
    }

    fun hasMorePagesTitans(): Boolean = nextPage != null

}
sealed interface EpisodesState{
    object Loading: EpisodesState
    data class Success(val episodes: List<Episodes>): EpisodesState
    data class Error(val error: String): EpisodesState
}