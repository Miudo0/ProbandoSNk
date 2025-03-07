package com.empresa.snk.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.snk.domain.GetCharactersFilterUseCase
import com.empresa.snk.domain.charactersDomain.Characters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetCharactersByNameViewModel @Inject constructor(
    private val getCharactersFilterUseCase: GetCharactersFilterUseCase
):ViewModel(){

    private var nextPage: String? = null

    private val _state = MutableStateFlow<FilterState>(FilterState.Loading)
    val state = _state

    fun getCharactersFilter(name:String){
        viewModelScope.launch {
            Log.d("Busqueda", "Filtro aplicado con el nombre: $name")
            if(name.isNotEmpty()){
                val result = getCharactersFilterUseCase(name,null,null,null)

                Log.d("Busqueda", "Resultado de la búsqueda: $result")
                state.value = FilterState.Success(result)
            }else{
                state.value = FilterState.Error
            }
        }
    }
    fun hasMorePages(): Boolean = nextPage != null


    sealed interface FilterState{
        object Loading:FilterState
        data object Error:FilterState
        data class Success(val characters:List<Characters>):FilterState
    }

}