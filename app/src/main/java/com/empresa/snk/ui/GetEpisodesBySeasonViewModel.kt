package com.empresa.snk.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.snk.domain.GetEpisodesBySeasonUseCase
import com.empresa.snk.domain.episodesDomain.Episodes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetEpisodesBySeasonViewModel @Inject constructor(
    private val getEpisodesBySeasonUseCase: GetEpisodesBySeasonUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<EpisodesStateBySeason>(EpisodesStateBySeason.Loading)
    val state = _state

     fun getEpisodesBySeason(season: String) {
        viewModelScope.launch {
            try {
                val response = getEpisodesBySeasonUseCase(season)
                Log.d("GetEpisodesBySeason", "Episodes received: ${response.results}")
                _state.value = EpisodesStateBySeason.Success(response.results)
            } catch (e: Exception) {
                Log.e("GetEpisodesBySeason", "Error fetching episodes", e)
                _state.value = EpisodesStateBySeason.Error(e.message ?: "Error desconocido")

            }


        }
    }

    sealed interface EpisodesStateBySeason {
        object Loading : EpisodesStateBySeason
        data class Success(val episodes: List<Episodes>) : EpisodesStateBySeason
        data class Error(val error: String) : EpisodesStateBySeason
    }

}