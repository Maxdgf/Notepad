package com.example.notepad.ui.screens

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.notepad.R
import com.example.notepad.core.data_management.databases.notes_local_storage.entities.NoteEntity
import com.example.notepad.ui.components.TopUiBar
import com.example.notepad.ui.components.AlertUiMessageDialog
import com.example.notepad.ui.components.BasicTextFieldUiPlaceholder
import com.example.notepad.ui.components.LoadingUiBlock
import com.example.notepad.ui.components.NoDataUiDescriptionBlock
import com.example.notepad.ui.screens.navigation.NavigationRoutes
import com.example.notepad.ui.states.NoteResult
import com.example.notepad.ui.viewmodels.screens.NoteEditScreenViewModel
import com.example.notepad.ui.viewmodels.NoteViewModel

@Composable
private fun NoteEditView(
    paddingValues: PaddingValues,
    currentNote: NoteEntity,
    onEditNote: (String, String, Long) -> Unit,
    onNavigateTo: (String) -> Unit,
    onPerformHaptic: (HapticFeedbackType) -> Unit
) {
    val noteEditScreenViewModel: NoteEditScreenViewModel = viewModel()

    var errorOfEmptyNotAlertMessageDialogState by rememberSaveable { mutableStateOf(false) }
    var errorOfNoteChangesAlertMessageDialogState by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(currentNote.name) {
        noteEditScreenViewModel.updateNoteName(currentNote.name)
    }

    LaunchedEffect(currentNote.content) {
        noteEditScreenViewModel.updateNoteContent(currentNote.content)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .imePadding()
            .padding(
                top = 5.dp,
                start = 5.dp,
                end = 5.dp
            ),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        OutlinedTextField(
            maxLines = 1,
            modifier = Modifier.fillMaxWidth(),
            value = noteEditScreenViewModel.noteName,
            onValueChange = { newValue -> noteEditScreenViewModel.updateNoteName(newValue) },
            trailingIcon = {
                IconButton(onClick = { noteEditScreenViewModel.updateNoteName("") }) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_clear_24),
                        contentDescription = null
                    )
                }
            },
            textStyle = TextStyle(fontSize = 15.sp),
            placeholder = {
                Text(
                    text = "Enter your note name here...",
                    modifier = Modifier.basicMarquee(Int.MAX_VALUE),
                    fontSize = 15.sp
                )
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                cursorColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        HorizontalDivider()

        val noteContentInputFieldVerticalScrollState = rememberScrollState()

        // text field auto scroll
        LaunchedEffect(noteContentInputFieldVerticalScrollState.maxValue) {
            noteContentInputFieldVerticalScrollState.animateScrollTo(noteContentInputFieldVerticalScrollState.maxValue)
        }
        
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(noteContentInputFieldVerticalScrollState),
            value = noteEditScreenViewModel.noteContent,
            onValueChange = { newValue -> noteEditScreenViewModel.updateNoteContent(newValue) },
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onPrimary),
            cursorBrush = SolidColor(if (isSystemInDarkTheme()) Color.White else Color.Black),
            decorationBox = @Composable { innerTextField ->
                BasicTextFieldUiPlaceholder(
                    value = noteEditScreenViewModel.noteContent,
                    placeholderText = "Write here anything...",
                    startPadding = 5.dp,
                    innerTextField = innerTextField
                )
            }
        )

        HorizontalDivider()

        Button(
            onClick = {
                onPerformHaptic(HapticFeedbackType.LongPress) // haptic

                if (noteEditScreenViewModel.isNoteNameOrContentEmpty()) {
                    errorOfEmptyNotAlertMessageDialogState = true
                } else {
                    // check changes in note
                    if (noteEditScreenViewModel.noteName != currentNote.name || noteEditScreenViewModel.noteContent != currentNote.content) {
                        onEditNote(
                            noteEditScreenViewModel.noteName,
                            noteEditScreenViewModel.noteContent,
                            currentNote.id
                        )
                        onNavigateTo(NavigationRoutes.MainScreen.route)
                    } else errorOfNoteChangesAlertMessageDialogState = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ),
            shape = RoundedCornerShape(10.dp)
        ) { Text(text = "edit note") }
    }

    AlertUiMessageDialog(
        onDismissRequestFunction = { errorOfEmptyNotAlertMessageDialogState = false },
        containerColor = MaterialTheme.colorScheme.error,
        contentColor = Color.White,
        state = errorOfEmptyNotAlertMessageDialogState,
        titleIcon = painterResource(R.drawable.outline_error_outline_24),
        titleText = "Error"
    ) {
        Text(text = "Note couldn't be empty!\n- Check note name and content.")

        Button(
            onClick = { errorOfEmptyNotAlertMessageDialogState = false },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onError),
            shape = RoundedCornerShape(10.dp)
        ) { Text(text = "Ok") }
    }

    AlertUiMessageDialog(
        onDismissRequestFunction = { errorOfNoteChangesAlertMessageDialogState = false },
        containerColor = MaterialTheme.colorScheme.error,
        contentColor = Color.White,
        state = errorOfNoteChangesAlertMessageDialogState,
        titleIcon = painterResource(R.drawable.outline_error_outline_24),
        titleText = "Error"
    ) {
        Text(text = "Changes not detected! Note cannot be edited.")

        Button(
            onClick = { errorOfNoteChangesAlertMessageDialogState = false },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onError)
        ) { Text(text = "Ok") }
    }
}

/**
 * Creates a note edit app screen.
 *
 * @param noteId current note id.
 * @param onNavigateTo function for navigate to specific screen.
 * @param noteViewModel notes viewmodel.
 */
@Composable
fun NoteUiEditScreen(
    noteId: Long?,
    onNavigateTo: (String) -> Unit,
    noteViewModel: NoteViewModel
) {
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(Unit) {
        noteId?.let { id ->
            noteViewModel.selectNote(id)
        }
    }

    Scaffold(
        topBar = {
            TopUiBar(
                titleContent = { Text(text = "Edit note") },
                barIcon = {
                    IconButton(onClick = { onNavigateTo(NavigationRoutes.MainScreen.route) }) {
                        Icon(
                            painter = painterResource(R.drawable.outline_arrow_back_24),
                            contentDescription = "Return to main screen icon button."
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            val currentNote by noteViewModel.currentNote.collectAsState()

            when (val noteState = currentNote) {
                is NoteResult.Found ->
                    NoteEditView(
                        paddingValues = innerPadding,
                        currentNote = noteState.note,
                        onEditNote = noteViewModel::editNote,
                        onNavigateTo = onNavigateTo,
                        onPerformHaptic = haptic::performHapticFeedback
                    )
                is NoteResult.Exception ->
                    NoDataUiDescriptionBlock(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        description = noteState.message
                    )
                is NoteResult.NotFound ->
                    NoDataUiDescriptionBlock(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        description = "This note, not found."
                    )
                NoteResult.Loading ->
                    LoadingUiBlock(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        description = "Loading note, please wait."
                    )
            }
        }
    )
}