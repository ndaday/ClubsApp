package com.ddaylab.clubsapp.ui.navigation

sealed class Screen(val route: String) {
    object Favorite : Screen("favorite")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object DetailClub : Screen("home/{clubId}") {
        fun createRoute(clubId: Long) = "home/$clubId"
    }
}