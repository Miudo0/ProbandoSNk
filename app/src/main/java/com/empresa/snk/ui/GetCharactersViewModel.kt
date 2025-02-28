package com.empresa.snk.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.snk.domain.Characters
import com.empresa.snk.domain.Info
import com.empresa.snk.domain.getCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetCharactersViewModel @Inject constructor(
    private val getCharactersUseCase: getCharactersUseCase
) : ViewModel() {
    private val _characters = MutableStateFlow<CharacterState>(CharacterState.Loading)
    val characters = _characters

    private var nextPage: String? = null


    fun getCharacters() {
        viewModelScope.launch {
            val response = getCharactersUseCase(nextPage) // Pasar nextPage si existe
            nextPage = response.info?.nextPage // Guardar la próxima página

            val currentList = _characters.value.let { state ->
                if (state is CharacterState.Success) {
                    state.characters
                } else {
                    emptyList()
                }
            }
            val newCharacters = response.results ?: emptyList()
            _characters.value = CharacterState.Success(currentList + newCharacters)
        }
    }

    fun hasMorePages(): Boolean = nextPage != null

}

sealed interface CharacterState {
    object Loading : CharacterState
    data class Success(val characters: List<Characters>) : CharacterState
    data class Error(val message: String) : CharacterState

}