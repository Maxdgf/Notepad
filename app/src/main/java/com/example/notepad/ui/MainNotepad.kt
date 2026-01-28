package com.example.notepad.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.edit
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.notepad.core.data_management.view_models.NoteViewModel
import com.example.notepad.core.data_management.view_models.UiViewModel
import com.example.notepad.core.utils.DateTimePicker
import com.example.notepad.dataStore
import com.example.notepad.gridEnabledState
import com.example.notepad.ui.screens.NavigationRoutes
import com.example.notepad.ui.screens.MainUiScreen
import com.example.notepad.ui.screens.NoteUiCreationScreen
import com.example.notepad.ui.screens.NoteUiEditScreen
import com.example.notepad.ui.screens.NoteUiViewScreen
import com.example.notepad.ui.screens.SettingsUiScreen
import com.example.notepad.ui.utils.CurrentThemeColor
import kotlinx.coroutines.flow.map

@Composable
fun MainUiNotePad(
    uiViewModel: UiViewModel = viewModel(),
    notesViewModel: NoteViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val dataStore = context.dataStore

    val currentThemeColor = remember { CurrentThemeColor() }
    val dateTimePicker = remember { DateTimePicker() }
    val navController = rememberNavController()

    val allNotesList by notesViewModel.noteList.collectAsState()
    val notesLoadingState by notesViewModel.isNotesLoadingState.collectAsState()
    val isGridEnabledState by uiViewModel.isGridEnabledState.collectAsState()

    LaunchedEffect(Unit) {
        val state = dataStore.data.map {
            it[gridEnabledState]
        }

        state.collect {
            it?.let { uiViewModel.updateIsGridEnabledState(it) } ?: uiViewModel.updateIsGridEnabledState(false)
        }
    }

    LaunchedEffect(uiViewModel.selectedNoteUuidState) {
        if (uiViewModel.selectedNoteUuidState.isNotEmpty()) {
            val currentNote = allNotesList[allNotesList.indexOfFirst { it.uuid == uiViewModel.selectedNoteUuidState }]
            uiViewModel.updateCurrentSelectedNote(currentNote)
        }
    }

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
                    selectedNoteUuidState = uiViewModel.selectedNoteUuidState,
                    isNotesLoadingState = notesLoadingState,
                    isGridEnabledState = isGridEnabledState
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
                    currentNote = uiViewModel.currentSelectedNote!!,
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
                    isNoteNameAnContentEmptyMethod = uiViewModel::isNoteNameAnContentEmpty,
                    updateErrorOfEmptyNotAlertMessageDialogStateMethod = uiViewModel::updateErrorOfEmptyNotAlertMessageDialogState,
                    editNoteMethod = notesViewModel::editNote,
                    errorOfNoteChangesAlertMessageDialogState = uiViewModel.errorOfNoteChangesAlertMessageDialogState,
                    updateErrorOfNoteChangesAlertMessageDialogStateMethod = uiViewModel::updateErrorOfNoteChangesAlertMessageDialogState,
                    dateTimePicker = dateTimePicker,
                    currentNote = uiViewModel.currentSelectedNote!!,
                )
            }

            composable(NavigationRoutes.NoteSettingsScreen.route) {
                SettingsUiScreen(
                    isGridEnabledState = isGridEnabledState,
                    updateIsGridEnabledStateMethod = uiViewModel::updateIsGridEnabledState,
                    navigationController = navController,
                    updateIsGridEnabledDatastore = {
                        dataStore.edit {
                            it[gridEnabledState] = isGridEnabledState
                        }
                    },
                )
            }
        }
    }
}