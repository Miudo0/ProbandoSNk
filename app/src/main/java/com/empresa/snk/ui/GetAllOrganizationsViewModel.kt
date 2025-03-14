package com.empresa.snk.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.snk.domain.GetAllOrganizationsUseCase
import com.empresa.snk.domain.organizationsDomain.Organization
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetAllOrganizationsViewModel @Inject constructor(
    private val getAllOrganizationsUseCase: GetAllOrganizationsUseCase
) : ViewModel() {

    private val _organizations = MutableStateFlow<OrganizationsState>(OrganizationsState.Loading)
    val organizations: StateFlow<OrganizationsState> = _organizations


    fun getOrganizations() {
        viewModelScope.launch {
            val response = getAllOrganizationsUseCase()
            val currentListOrganization = _organizations.value.let { state ->
                if (state is OrganizationsState.Success) {
                    state.organizations
                } else {
                    emptyList()
                }
            }
            val newOrganizations = response.results ?: emptyList()
            _organizations.value =
                OrganizationsState.Success(currentListOrganization + newOrganizations)
        }

    }
}

sealed interface OrganizationsState {
    object Loading : OrganizationsState
    data class Success(val organizations: List<Organization>) : OrganizationsState
    data class Error(val message: String) : OrganizationsState

}