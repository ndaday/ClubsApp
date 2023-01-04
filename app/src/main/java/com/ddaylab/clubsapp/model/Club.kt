package com.ddaylab.clubsapp.model

data class Club(
    val id: Long,
    val image: Int,
    val title: String,
    val description: String,
    val isFavorite: Boolean,
)