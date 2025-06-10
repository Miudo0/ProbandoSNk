package com.empresa.snk.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.snk.domain.GetNotableFormerMembers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetNotableFormerMembersViewModel @Inject constructor(
    private val getNotableFormerMembersUseCase: GetNotableFormerMembers
) : ViewModel() {

    private val _notableFormerMembers = MutableStateFlow<Map<String, String>>(emptyMap())
    val notableFormerMembers: StateFlow<Map<String, String>> = _notableFormerMembers

    fun getNotableFormerMembers(url: String) {
        viewModelScope.launch {
            val name = getNotableFormerMembersUseCase(url)
            _notableFormerMembers.value += (url to name)
        }
    }
}