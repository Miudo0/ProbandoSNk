package com.empresa.snk.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.snk.domain.GetFormerInheritorsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetFormerInheritorsViewModel @Inject constructor(
    private val getFormerInheritorsUseCase: GetFormerInheritorsUseCase
): ViewModel() {

//lo mismo que antes
    private val _formerInheritors = MutableStateFlow<Map<Int, FormerInheritorsState>>(emptyMap())
    val formerInheritors = _formerInheritors


    fun getFormerInheritors(titanId: Int, formerInheritorUrl: List<String>) {
        if (_formerInheritors.value.containsKey(titanId)) return

        viewModelScope.launch {
            try {

                _formerInheritors.value += (titanId to FormerInheritorsState.Loading)


                val inheritorsName = formerInheritorUrl.map { url ->
                    async {
                        getFormerInheritorsUseCase(url)
                    }
                }.awaitAll()

                // Actualizamos el estado con los herederos obtenidos
                _formerInheritors.value += (titanId to FormerInheritorsState.Succes(inheritorsName))
            } catch (e: Exception) {
                // Si ocurre un error, establecemos el estado de error
                _formerInheritors.value += (titanId to FormerInheritorsState.Error)
            }
        }
    }

}
sealed interface FormerInheritorsState{
    object Loading: FormerInheritorsState
    data class Succes (val inheritors: List<String>): FormerInheritorsState
    object Error: FormerInheritorsState

}