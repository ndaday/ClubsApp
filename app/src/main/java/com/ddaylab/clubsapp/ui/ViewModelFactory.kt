package com.ddaylab.clubsapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ddaylab.clubsapp.data.ClubRepository
import com.ddaylab.clubsapp.ui.screen.detail.DetailClubViewModel
import com.ddaylab.clubsapp.ui.screen.home.HomeViewModel

class ViewModelFactory (private val repository: ClubRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(DetailClubViewModel::class.java)) {
            return DetailClubViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}
