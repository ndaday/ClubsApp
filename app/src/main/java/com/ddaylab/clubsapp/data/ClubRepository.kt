package com.ddaylab.clubsapp.data

import com.ddaylab.clubsapp.model.Club
import com.ddaylab.clubsapp.model.ClubData
import com.ddaylab.clubsapp.model.ClubDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ClubRepository {

    private val club = mutableListOf<ClubData>()

    init {
        if (club.isEmpty()) {
            ClubDataSource.clubs.forEach {
                club.add(ClubData(it, false))
            }
        }
    }

    fun getAllClub(): Flow<List<ClubData>> {
        return flowOf(club)
    }

    fun getClubs(): List<Club> {
        return ClubDataSource.clubs
    }

    fun getClubById(clubId: Long): ClubData {
        return club.first {
            it.club.id == clubId
        }
    }

    fun searchClub(query: String): List<Club>{
        return ClubDataSource.clubs.filter {
            it.title.contains(query, ignoreCase = true)
        }
    }

    companion object {
        @Volatile
        private var instance: ClubRepository? = null

        fun getInstance(): ClubRepository =
            instance ?: synchronized(this) {
                ClubRepository().apply {
                    instance = this
                }
            }
    }
}