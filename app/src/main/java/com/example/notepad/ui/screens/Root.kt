package com.example.notepad.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.notepad.ui.screens.navigation.NavigationRoutes
import com.example.notepad.ui.screens.navigation.Navigator
import com.example.notepad.ui.viewmodels.AppDataStoreViewModel
import com.example.notepad.ui.viewmodels.NoteViewModel

/**Main screen root.*/
@Composable
fun MainUiNotePad() {
    val noteViewModel: NoteViewModel = hiltViewModel()
    val appDataStoreViewModel: AppDataStoreViewModel = hiltViewModel()

    val navController = rememberNavController()
    val navigator = remember { Navigator(navController) }

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = NavigationRoutes.MainScreen.route
        ) {
            // main screen
            composable(route = NavigationRoutes.MainScreen.route) {
                MainUiScreen(
                    onNavigateTo = navigator::navigateTo,
                    noteViewModel = noteViewModel,
                    appDataStoreViewModel = appDataStoreViewModel
                )
            }

            // note creation screen
            composable(route = NavigationRoutes.NoteCreationScreen.route) {
                NoteUiCreationScreen(
                    onNavigateTo = navigator::navigateTo,
                    onAddNote = noteViewModel::addNote
                )
            }

            // note view screen
            composable(
                route = "${NavigationRoutes.NoteViewScreen.route}/{noteId}",
                arguments = listOf(
                    navArgument("noteId") { type = NavType.LongType }
                )
            ) { navBackStackEntry ->
                val noteId = navBackStackEntry.arguments?.getLong("noteId")

                NoteUiViewScreen(
                    onNavigateTo = navigator::navigateTo,
                    noteId = noteId,
                    noteViewModel = noteViewModel,
                    appDataStoreViewModel = appDataStoreViewModel,
                )
            }

            // note edit screen
            composable(
                route = "${NavigationRoutes.NoteEditScreen.route}/{noteId}",
                arguments = listOf(
                    navArgument("noteId") { type = NavType.LongType }
                )
            ) { navBackStackEntry ->
                val noteId = navBackStackEntry.arguments?.getLong("noteId")

                NoteUiEditScreen(
                    onNavigateTo = navigator::navigateTo,
                    noteId = noteId,
                    noteViewModel = noteViewModel
                )
            }

            // settings screen
            composable(route = NavigationRoutes.NoteSettingsScreen.route) {
                SettingsUiScreen(
                    onNavigateTo = navigator::navigateTo,
                    appDataStoreViewModel = appDataStoreViewModel,
                )
            }
        }
    }
}