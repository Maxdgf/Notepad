package com.example.notepad.ui.navigation

import androidx.navigation.NavController

class Navigator(private val navController: NavController) {
    /**
     * Navigates to current screen by route.
     * @param route target screen route.
     */
    fun navigateTo(route: String) {
        navController.navigate(route) {
            popUpTo("main_screen") {
                inclusive = false // don't delete main screen in stack
                saveState = true // save main screen state
            }
            launchSingleTop = true // preventing screen duplication in stack
            restoreState = false // don't restore screen state
        }
    }
}