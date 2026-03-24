package com.example.notepad.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.notepad.ui.navigation.NavigationRoutes
import com.example.notepad.ui.navigation.Navigator
import com.example.notepad.ui.view_models.AppDataStoreViewModel

/**Main screen root.*/
@Composable
fun MainUiNotePad(appDataStoreViewModel: AppDataStoreViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val navigator = remember { Navigator(navController) }

    val isGridEnabledState by appDataStoreViewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = NavigationRoutes.MainScreen.route
        ) {
            // main screen
            composable(route = NavigationRoutes.MainScreen.route) {
                MainUiScreen(
                    navigator = navigator,
                    isGridEnabledState = isGridEnabledState
                )
            }

            // note creation screen
            composable(route = NavigationRoutes.NoteCreationScreen.route) {
                NoteUiCreationScreen(navigator = navigator,)
            }

            // note view screen
            composable(
                route = "${NavigationRoutes.NoteViewScreen.route}/{noteId}",
                arguments = listOf(
                    navArgument("noteId") { type = NavType.LongType }
                )
            ) { navBackStackEntry ->
                val noteId = navBackStackEntry.arguments?.getLong("noteId")

                val noteTextSize by appDataStoreViewModel.noteTextSize.collectAsState()
                val textWrapState by appDataStoreViewModel.textWrapMode.collectAsState()

                NoteUiViewScreen(
                    navigator = navigator,
                    currentFontSize = noteTextSize,
                    updateCurrentFontSize = appDataStoreViewModel::saveNoteTextSize,
                    textWrapState = textWrapState,
                    updateTextWrapStateMethod = appDataStoreViewModel::saveTextWrapState,
                    noteId = noteId
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
                    navigator = navigator,
                    noteId = noteId
                )
            }

            // settings screen
            composable(route = NavigationRoutes.NoteSettingsScreen.route) {
                SettingsUiScreen(
                    isGridEnabledState = isGridEnabledState,
                    updateIsGridEnabledStateMethod = appDataStoreViewModel::saveState,
                    navigator = navigator,
                )
            }
        }
    }
}