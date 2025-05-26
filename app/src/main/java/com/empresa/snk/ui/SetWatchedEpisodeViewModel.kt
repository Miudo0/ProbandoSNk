package com.empresa.snk.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.snk.domain.SetWatchedEpisodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetWatchedEpisodeViewModel @Inject constructor(
    private val setWatchedEpisodeUseCase: SetWatchedEpisodeUseCase
):ViewModel(){

    private val _isWatched = MutableStateFlow(false)
    val isWatched = _isWatched

    fun SetWatchedEpisode(episodeId: Int, isWatched: Boolean) {
      viewModelScope.launch {
          setWatchedEpisodeUseCase(episodeId, isWatched)
          _isWatched.value = isWatched
      }
    }


}