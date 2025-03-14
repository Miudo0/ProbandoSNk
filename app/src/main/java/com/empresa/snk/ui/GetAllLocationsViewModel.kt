package com.empresa.snk.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.snk.domain.GetAllLocationsUseCase
import com.empresa.snk.domain.locationsDomain.Location
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetAllLocationsViewModel @Inject constructor(
    private val getAllLocationsUseCase: GetAllLocationsUseCase
): ViewModel(){
    private val _locations = MutableStateFlow<LocationsState>(LocationsState.Loading)
    val locations = _locations

    private var nextPage: String? = null
    fun getLocations(){
        viewModelScope.launch {
            val response = getAllLocationsUseCase(nextPage)
            nextPage = response.info?.nextPage

            val currentList = _locations.value.let { state ->
                if (state is LocationsState.Success) {
                    state.locations
                } else {
                    emptyList()
                }
            }
            val newLocations = response.results ?: emptyList()
          _locations.value = LocationsState.Success(currentList + newLocations)

        }

    }

    fun hasMorePages(): Boolean = nextPage != null


}
sealed interface LocationsState{
    object Loading: LocationsState
    data class Success(val locations: List<Location>): LocationsState
    data class Error(val message: String): LocationsState

}