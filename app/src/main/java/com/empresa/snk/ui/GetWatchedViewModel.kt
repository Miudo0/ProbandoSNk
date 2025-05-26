package com.empresa.snk.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.snk.domain.GetIsWatchedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetWatchedViewModel @Inject constructor(
    private val getIsWatchedUseCase: GetIsWatchedUseCase

): ViewModel() {
    private val _watchedMap = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val watchedMap = _watchedMap.asStateFlow()

    fun getWatched(episodeId: Int) {
        viewModelScope.launch {
            val watched = getIsWatchedUseCase(episodeId)
            _watchedMap.update { current ->
                current + (episodeId to watched)
            }
        }
    }

    fun isEpisodeWatched(id: Int): Boolean =
        _watchedMap.value[id] ?: false

}