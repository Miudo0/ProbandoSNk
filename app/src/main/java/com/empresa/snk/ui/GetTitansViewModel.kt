package com.empresa.snk.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.snk.domain.GetTitansUseCase

import com.empresa.snk.domain.titansDomain.Titan
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetTitansViewModel @Inject constructor(
    private val getTitansUseCase: GetTitansUseCase
): ViewModel(){

    private val  _state = MutableStateFlow<titansState>(titansState.Loading)
    val state: StateFlow<titansState> = _state

    private var nextPage: String? = null

    fun getTitans() {
        viewModelScope.launch {
            val response = getTitansUseCase(nextPage)
            nextPage = response.info?.nextPage

            val currentList = _state.value.let { state ->
                if (state is titansState.Success) {
                    state.titans
                } else {
                    emptyList()
                }
            }
            val newTitans = response.results ?: emptyList()
           _state.value = titansState.Success(currentList + newTitans)
        }
    }

    fun hasMorePagesTitans(): Boolean = nextPage != null

}

sealed interface titansState{
    object Loading: titansState
    data class Error(val message: String): titansState
    data class Success(val titans: List<Titan>): titansState

}