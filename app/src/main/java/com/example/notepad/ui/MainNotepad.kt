package com.example.notepad.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.notepad.core.data_management.view_models.NoteViewModel
import com.example.notepad.core.data_management.view_models.UiViewModel
import com.example.notepad.core.utils.DateTimePicker
import com.example.notepad.ui.screens.NavigationRoutes
import com.example.notepad.ui.screens.MainUiScreen
import com.example.notepad.ui.screens.NoteUiCreationScreen
import com.example.notepad.ui.screens.NoteUiEditScreen
import com.example.notepad.ui.screens.NoteUiViewScreen
import com.example.notepad.ui.utils.CurrentThemeColor

@Composable
fun MainUiNotePad(
    uiViewModel: UiViewModel = viewModel(),
    notesViewModel: NoteViewModel = hiltViewModel()
) {
   val currentThemeColor = remember { CurrentThemeColor() }
   val dateTimePicker = remember { DateTimePicker() }

    val navController = rememberNavController()

    val allNotesList by notesViewModel.noteList.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = NavigationRoutes.MainScreen.route
        ) {
            composable(NavigationRoutes.MainScreen.route) { 
                MainUiScreen(
                    navigationController = navController,
                    noteActionsDialogState = uiViewModel.noteActionsDialogState,
                    notesList = allNotesList,
                    updateSelectedNoteUuidStateMethod = uiViewModel::updateSelectedNoteUuidState,
                    updateNoteActionsDialogStateMethod = uiViewModel::updateNoteActionsDialogState,
                    updateNoteNameStateMethod = uiViewModel::updateNoteNameState,
                    updateNoteContentStateMethod = uiViewModel::updateNoteContentState,
                    deleteNoteMethod = notesViewModel::deleteNote,
                    deleteAllNotesAlertMessageDialogState = uiViewModel.deleteAllNotesAlertMessageDialogState,
                    updateDeleteAllNotesAlertMessageDialogStateMethod = uiViewModel::updateDeleteAllNotesAlertMessageDialogState,
                    deleteAllNotesMethod = notesViewModel::deleteAllNotes,
                    currentThemeColor = currentThemeColor,
                    selectedNoteUuidState = uiViewModel.selectedNoteUuidState
                )
            }

            composable(NavigationRoutes.NoteCreationScreen.route) { 
                NoteUiCreationScreen(
                    navigationController = navController,
                    noteNameState = uiViewModel.noteNameState,
                    noteContentState = uiViewModel.noteContentState,
                    errorOfEmptyNotAlertMessageDialogState = uiViewModel.errorOfEmptyNotAlertMessageDialogState,
                    updateNoteNameStateMethod = uiViewModel::updateNoteNameState,
                    updateNoteContentStateMethod = uiViewModel::updateNoteContentState,
                    clearNoteNameStateMethod = uiViewModel::clearNoteNameState,
                    clearNoteContentStateMethod = uiViewModel::clearNoteContentState,
                    isNoteNameAnContentEmptyMethod = uiViewModel::isNoteNameAnContentEmpty,
                    updateErrorOfEmptyNotAlertMessageDialogStateMethod = uiViewModel::updateErrorOfEmptyNotAlertMessageDialogState,
                    addNoteMethod = notesViewModel::addNote,
                    dateTimePicker = dateTimePicker
                )
            }

            composable(NavigationRoutes.NoteViewScreen.route) { 
                NoteUiViewScreen(
                    navigationController = navController,
                    selectedNoteUuidState = uiViewModel.selectedNoteUuidState,
                    notesList = allNotesList,
                )
            }

            composable(NavigationRoutes.NoteEditScreen.route) {
                NoteUiEditScreen(
                    navigationController = navController,
                    noteNameState = uiViewModel.noteNameState,
                    noteContentState = uiViewModel.noteContentState,
                    errorOfEmptyNotAlertMessageDialogState = uiViewModel.errorOfEmptyNotAlertMessageDialogState,
                    updateNoteNameStateMethod = uiViewModel::updateNoteNameState,
                    updateNoteContentStateMethod = uiViewModel::updateNoteContentState,
                    clearNoteNameStateMethod = uiViewModel::clearNoteNameState,
                    clearNoteContentStateMethod = uiViewModel::clearNoteContentState,
                    isNoteNameAnContentEmptyMethod = uiViewModel::isNoteNameAnContentEmpty,
                    updateErrorOfEmptyNotAlertMessageDialogStateMethod = uiViewModel::updateErrorOfEmptyNotAlertMessageDialogState,
                    editNoteMethod = notesViewModel::editNote,
                    errorOfNoteChangesAlertMessageDialogState = uiViewModel.errorOfNoteChangesAlertMessageDialogState,
                    updateErrorOfNoteChangesAlertMessageDialogStateMethod = uiViewModel::updateErrorOfNoteChangesAlertMessageDialogState,
                    currentThemeColor = currentThemeColor,
                    dateTimePicker = dateTimePicker,
                    selectedNoteUuidState = uiViewModel.selectedNoteUuidState,
                    notesList = allNotesList,
                )
            }
        }
    }
}