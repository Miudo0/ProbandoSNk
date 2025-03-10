package com.empresa.snk.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.snk.domain.GetFamilyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetFamilyViewModel @Inject constructor(
    private val getFamilyUseCase: GetFamilyUseCase
) : ViewModel() {
    private val _family = MutableStateFlow<FamilyState>(FamilyState.Loading)
    val family = _family


    fun getFamily(familyUrl: List<String>) {
        viewModelScope.launch {
            try {
                val familyName = familyUrl.map { url ->
                    async {
                        Log.d("GetEpisodesDetailViewModel", "Obteniendo detalles para: $url")
                        getFamilyUseCase(url)
                    }
                }.awaitAll()
                _family.value = FamilyState.Success(familyName)
            } catch (e: Exception) {
                _family.value = FamilyState.Error("Error al cargar")
            }
        }
    }
}

sealed interface FamilyState {
    data object Loading : FamilyState
    data class Success(val family: List<String>) : FamilyState
    data class Error(val message: String) : FamilyState

}