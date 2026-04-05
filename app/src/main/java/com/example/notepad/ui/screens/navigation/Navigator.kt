package com.example.notepad.ui.screens.navigation

import androidx.navigation.NavController

class Navigator(private val navController: NavController) {
    /**
     * Navigates to current screen by route.
     * @param route target screen route.
     */
    fun navigateTo(route: String) {
        navController.navigate(route) {
            // delete screens before main screen in stack
            popUpTo("main_screen") {
                inclusive = false // don't delete main screen in stack
            }
            launchSingleTop = true // preventing screen duplication in stack
        }
    }
}