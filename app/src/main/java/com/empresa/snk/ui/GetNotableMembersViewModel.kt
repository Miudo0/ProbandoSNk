package com.empresa.snk.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.snk.domain.GetNotableMembersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetNotableMembersViewModel @Inject constructor(
    private val getNotableMembersUseCase: GetNotableMembersUseCase
) : ViewModel() {

    private val _notableMembers = MutableStateFlow<Map<String, String>>(emptyMap())
    val notableMembers: StateFlow<Map<String, String>> = _notableMembers

    fun getNotableMembers(url: String) {
        viewModelScope.launch {
            val name = getNotableMembersUseCase(url)
            _notableMembers.value += (url to name)
        }
    }
}