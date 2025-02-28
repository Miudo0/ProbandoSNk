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

    fun getCharacters() {
        viewModelScope.launch {
            val response = getCharactersUseCase()
       if(response != null){
           _characters.value = CharacterState.Success(response)
       }else{
           _characters.value = CharacterState.Error("Error al obtener los personajes")
       }

        }
    }


}
sealed interface CharacterState {
    object Loading : CharacterState
    data class Success(val characters: List<Characters>) : CharacterState
    data class Error(val message: String) : CharacterState

}