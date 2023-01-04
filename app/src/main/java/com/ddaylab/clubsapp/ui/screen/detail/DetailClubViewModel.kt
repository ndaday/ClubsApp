package com.ddaylab.clubsapp.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddaylab.clubsapp.data.ClubRepository
import com.ddaylab.clubsapp.model.ClubData
import com.ddaylab.clubsapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailClubViewModel(
    private val repository: ClubRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<ClubData>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<ClubData>>
        get() = _uiState

    fun getClubById(clubId: Long) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = UiState.Success(repository.getClubById(clubId))
        }
    }
}