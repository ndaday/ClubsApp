package com.ddaylab.clubsapp.di

import com.ddaylab.clubsapp.data.ClubRepository

object Injection {
    fun provideRepository(): ClubRepository {
        return ClubRepository.getInstance()
    }
}