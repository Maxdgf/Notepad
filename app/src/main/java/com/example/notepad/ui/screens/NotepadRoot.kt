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

import com.example.notepad.ui.screens.navigation.NavigationRoutes
import com.example.notepad.ui.screens.navigation.Navigator
import com.example.notepad.ui.view_models.AppDataStoreViewModel
import com.example.notepad.ui.view_models.NoteViewModel

/**Main screen root.*/
@Composable
fun MainUiNotePad(
    noteViewModel: NoteViewModel = hiltViewModel(),
    appDataStoreViewModel: AppDataStoreViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val navigator = remember { Navigator(navController) }

    val allNotesList by noteViewModel.noteList.collectAsState()

    val isGridEnabledState by appDataStoreViewModel.notesGridEnabledMode.collectAsState()
    val isOrderNumEnabledState by appDataStoreViewModel.orderNumEnabledState.collectAsState()
    val isAlternatingNoteColorsEnabledState by appDataStoreViewModel.alternatingNoteColorsEnabledState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = NavigationRoutes.MainScreen.route
        ) {
            // main screen
            composable(route = NavigationRoutes.MainScreen.route) {
                MainUiScreen(
                    onNavigateTo = navigator::navigateTo,
                    isGridEnabledState = isGridEnabledState,
                    notesList = allNotesList,
                    onDeleteAllNotes = noteViewModel::deleteAllNotes,
                    isDisplayOrderNumEnabledState = isOrderNumEnabledState,
                    isAlternatingNoteColorsEnabledState = isAlternatingNoteColorsEnabledState,
                    onDeleteNoteById = noteViewModel::deleteNote
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

                val noteTextSize by appDataStoreViewModel.noteTextSize.collectAsState()
                val textWrapState by appDataStoreViewModel.textWrapMode.collectAsState()

                NoteUiViewScreen(
                    onNavigateTo = navigator::navigateTo,
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
                    noteId = noteId,
                    onNavigateTo = navigator::navigateTo
                )
            }

            // settings screen
            composable(route = NavigationRoutes.NoteSettingsScreen.route) {
                SettingsUiScreen(
                    isGridEnabledState = isGridEnabledState,
                    updateIsGridEnabledStateMethod = appDataStoreViewModel::saveNotesGridEnabledState,
                    onNavigateTo = navigator::navigateTo,
                    isDisplayOrderNumEnabledState = isOrderNumEnabledState,
                    updateIsDisplayOrderNumEnabledStateMethod = appDataStoreViewModel::saveOrderNumEnabledState,
                    isAlternatingNoteColorsEnabledState = isAlternatingNoteColorsEnabledState,
                    updateIsAlternatingNoteColorsEnabledState = appDataStoreViewModel::saveAlternatingNoteColorsState
                )
            }
        }
    }
}