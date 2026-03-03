package com.example.notepad.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.notepad.ui.view_models.NoteViewModel
import com.example.notepad.ui.view_models.UiViewModel
import com.example.notepad.utils.DateTimePicker
import com.example.notepad.ui.navigation.NavigationRoutes
import com.example.notepad.ui.navigation.Navigator
import com.example.notepad.ui.screens.MainUiScreen
import com.example.notepad.ui.screens.NoteUiCreationScreen
import com.example.notepad.ui.screens.NoteUiEditScreen
import com.example.notepad.ui.screens.NoteUiViewScreen
import com.example.notepad.ui.screens.SettingsUiScreen
import com.example.notepad.ui.view_models.AppDataStoreViewModel
import com.example.notepad.utils.AppManager
import com.example.notepad.utils.ClipBoardManager

/**Main screen root.*/
@Composable
fun MainUiNotePad(
    uiViewModel: UiViewModel = viewModel(),
    notesViewModel: NoteViewModel = hiltViewModel(),
    appDataStoreViewModel: AppDataStoreViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = LocalActivity.current

    val dateTimePicker = remember { DateTimePicker() }
    val navController = rememberNavController()
    val navigator = remember { Navigator(navController) }
    val clipBoardManager = remember { ClipBoardManager(context) }
    val appManager = remember { AppManager(activity, context) }

    val allNotesList by notesViewModel.noteList.collectAsState()
    val notesLoadingState by notesViewModel.isNotesLoadingState.collectAsState()
    val isGridEnabledState by appDataStoreViewModel.state.collectAsState()
    val selectedNote by notesViewModel.selectedNote.collectAsState()
    val noteTextSize by appDataStoreViewModel.noteTextSize.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = NavigationRoutes.MainScreen.route
        ) {
            composable(NavigationRoutes.MainScreen.route) { 
                MainUiScreen(
                    navigator = navigator,
                    noteActionsDialogState = uiViewModel.noteActionsDialogState,
                    notesList = allNotesList,
                    updateSelectedNoteStateMethod = notesViewModel::selectNote,
                    updateNoteActionsDialogStateMethod = uiViewModel::updateNoteActionsDialogState,
                    updateNoteNameStateMethod = uiViewModel::updateEditNoteNameState,
                    updateNoteContentStateMethod = uiViewModel::updateEditNoteContentState,
                    deleteNoteMethod = notesViewModel::deleteNote,
                    deleteAllNotesAlertMessageDialogState = uiViewModel.deleteAllNotesAlertMessageDialogState,
                    updateDeleteAllNotesAlertMessageDialogStateMethod = uiViewModel::updateDeleteAllNotesAlertMessageDialogState,
                    deleteAllNotesMethod = notesViewModel::deleteAllNotes,
                    selectedNoteUuidState = selectedNote?.uuid,
                    isNotesLoadingState = notesLoadingState,
                    isGridEnabledState = isGridEnabledState,
                    state = uiViewModel.mainScreenDropdownMenuState,
                    updateStateMethod = uiViewModel::updateMainScreenDropdownMenuState,
                    exitAppMethod = appManager::breakApp
                )
            }

            composable(NavigationRoutes.NoteCreationScreen.route) { 
                NoteUiCreationScreen(
                    navigator = navigator,
                    noteNameState = uiViewModel.noteNameState,
                    noteContentState = uiViewModel.noteContentState,
                    errorOfEmptyNotAlertMessageDialogState = uiViewModel.errorOfEmptyNotAlertMessageDialogState,
                    updateNoteNameStateMethod = uiViewModel::updateNoteNameState,
                    updateNoteContentStateMethod = uiViewModel::updateNoteContentState,
                    isNoteNameAnContentEmptyMethod = uiViewModel::isNoteNameAndContentEmpty,
                    updateErrorOfEmptyNotAlertMessageDialogStateMethod = uiViewModel::updateErrorOfEmptyNotAlertMessageDialogState,
                    addNoteMethod = notesViewModel::addNote,
                    dateTimePicker = dateTimePicker
                )
            }

            composable(NavigationRoutes.NoteViewScreen.route) { 
                NoteUiViewScreen(
                    navigator = navigator,
                    currentNote = selectedNote,
                    currentFontSize = noteTextSize,
                    updateCurrentFontSize = appDataStoreViewModel::saveNoteTextSize,
                    changeFontSizeDialogState = uiViewModel.changeFontSizeDialogState,
                    updateChangeFontSizeDialogStateMethod = uiViewModel::updateChangeFontSizeDialogState,
                    clipBoardManager = clipBoardManager,
                    state = uiViewModel.noteViewScreenDropdownMenuState,
                    updateStateMethod = uiViewModel::updateNoteViewScreenDropdownMenuState
                )
            }

            composable(NavigationRoutes.NoteEditScreen.route) {
                NoteUiEditScreen(
                    navigator = navigator,
                    noteNameState = uiViewModel.editNoteNameState,
                    noteContentState = uiViewModel.editNoteContentState,
                    errorOfEmptyNotAlertMessageDialogState = uiViewModel.errorOfEmptyNotAlertMessageDialogState,
                    updateNoteNameStateMethod = uiViewModel::updateEditNoteNameState,
                    updateNoteContentStateMethod = uiViewModel::updateEditNoteContentState,
                    isNoteNameAndContentEmptyMethod = uiViewModel::isEditNoteNameAndContentEmpty,
                    updateErrorOfEmptyNotAlertMessageDialogStateMethod = uiViewModel::updateErrorOfEmptyNotAlertMessageDialogState,
                    editNoteMethod = notesViewModel::editNote,
                    errorOfNoteChangesAlertMessageDialogState = uiViewModel.errorOfNoteChangesAlertMessageDialogState,
                    updateErrorOfNoteChangesAlertMessageDialogStateMethod = uiViewModel::updateErrorOfNoteChangesAlertMessageDialogState,
                    dateTimePicker = dateTimePicker,
                    currentNote = selectedNote,
                )
            }

            composable(NavigationRoutes.NoteSettingsScreen.route) {
                SettingsUiScreen(
                    isGridEnabledState = isGridEnabledState,
                    updateIsGridEnabledStateMethod = appDataStoreViewModel::saveState,
                    navigator = navigator,
                )
            }
        }
    }
}