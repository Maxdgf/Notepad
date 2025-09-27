package com.example.notepad.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.notepad.core.data_management.view_models.NoteViewModel
import com.example.notepad.core.data_management.view_models.UiViewModel
import com.example.notepad.ui.screens.NavigationRoutes
import com.example.notepad.ui.screens.MainUiScreen
import com.example.notepad.ui.screens.NoteUiCreationScreen
import com.example.notepad.ui.screens.NoteUiEditScreen
import com.example.notepad.ui.screens.NoteUiViewScreen

@Composable
fun MainUiNotePad(
    uiViewModel: UiViewModel = viewModel(),
    notesViewModel: NoteViewModel = hiltViewModel()
) {
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
                    selectedNoteState = uiViewModel.selectedNoteState,
                    updateSelectedNoteStateMethod = uiViewModel::updateSelectedNoteState,
                    updateNoteActionsDialogStateMethod = uiViewModel::updateNoteActionsDialogState,
                    updateNoteNameStateMethod = uiViewModel::updateNoteNameState,
                    updateNoteContentStateMethod = uiViewModel::updateNoteContentState,
                    deleteNoteMethod = notesViewModel::deleteNote,
                    deleteAllNotesAlertMessageDialogState = uiViewModel.deleteAllNotesAlertMessageDialogState,
                    updateDeleteAllNotesAlertMessageDialogStateMethod = uiViewModel::updateDeleteAllNotesAlertMessageDialogState,
                    deleteAllNotesMethod = notesViewModel::deleteAllNotes
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
                )
            }

            composable(NavigationRoutes.NoteViewScreen.route) { 
                NoteUiViewScreen(
                    navigationController = navController,
                    selectedNoteState = uiViewModel.selectedNoteState,
                    //clearSelectedNoteStateMethod = uiViewModel::clearSelectedNoteState
                )
            }

            composable(NavigationRoutes.NoteEditScreen.route) {
                NoteUiEditScreen(
                    navigationController = navController,
                    noteNameState = uiViewModel.noteNameState,
                    noteContentState = uiViewModel.noteContentState,
                    selectedNoteState = uiViewModel.selectedNoteState,
                    errorOfEmptyNotAlertMessageDialogState = uiViewModel.errorOfEmptyNotAlertMessageDialogState,
                    updateNoteNameStateMethod = uiViewModel::updateNoteNameState,
                    updateNoteContentStateMethod = uiViewModel::updateNoteContentState,
                    clearNoteNameStateMethod = uiViewModel::clearNoteNameState,
                    clearNoteContentStateMethod = uiViewModel::clearNoteContentState,
                    isNoteNameAnContentEmptyMethod = uiViewModel::isNoteNameAnContentEmpty,
                    updateErrorOfEmptyNotAlertMessageDialogStateMethod = uiViewModel::updateErrorOfEmptyNotAlertMessageDialogState,
                    editNoteMethod = notesViewModel::editNote,
                    clearSelectedNoteStateMethod = uiViewModel::clearSelectedNoteState,
                    errorOfNoteChangesAlertMessageDialogState = uiViewModel.errorOfNoteChangesAlertMessageDialogState,
                    updateErrorOfNoteChangesAlertMessageDialogStateMethod = uiViewModel::updateErrorOfNoteChangesAlertMessageDialogState,
                )
            }
        }
    }
}