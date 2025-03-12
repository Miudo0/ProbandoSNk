package com.empresa.snk.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.snk.domain.GetCurrentInheritorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetCurrentInheritorViewModel @Inject constructor(
    private val getCurrentInheritorUseCase: GetCurrentInheritorUseCase
) : ViewModel() {
//puesto asi con Map porque sino en el lazyColumn me hacia un bucle infinito
    private val _currentInheritors = MutableStateFlow<Map<String, String>>(emptyMap())
    val currentInheritors = _currentInheritors

    fun getCurrentInheritor(currentInheritorUrl: String) {
        viewModelScope.launch {
            val nombre = getCurrentInheritorUseCase(currentInheritorUrl)
            _currentInheritors.value += (currentInheritorUrl to nombre)
            Log.d("GetCurrentInheritorViewModel", "Nombre: $nombre")
        }
    }

}
