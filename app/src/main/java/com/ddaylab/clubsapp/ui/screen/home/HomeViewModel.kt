package com.ddaylab.clubsapp.ui.screen.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddaylab.clubsapp.data.ClubRepository
import com.ddaylab.clubsapp.model.Club
import com.ddaylab.clubsapp.model.ClubData
import com.ddaylab.clubsapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ClubRepository) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<ClubData>>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<ClubData>>>
        get() = _uiState

    fun getAllClub() {
        viewModelScope.launch {
            repository.getAllClub()
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { clubs ->
                    _uiState.value = UiState.Success(clubs)
                }
        }
    }

    private val _groupedClub = MutableStateFlow(
        repository.getClubs()
            .sortedBy { it.title }
            .groupBy { it.title[0] }
    )
    val groupedClub: MutableStateFlow<Map<Char, List<Club>>> get() = _groupedClub


    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    fun search(newQuery: String) {
        _query.value = newQuery
        _groupedClub.value = repository.searchClub(_query.value)
            .sortedBy { it.title }
            .groupBy { it.title[0] }
    }
}

