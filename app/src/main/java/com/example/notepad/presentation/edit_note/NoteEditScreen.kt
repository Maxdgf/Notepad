package com.example.notepad.presentation.edit_note

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.notepad.R
import com.example.notepad.domain.model.Note
import com.example.notepad.presentation.common.SCREEN_TRANSITION_DURATION
import com.example.notepad.presentation.common.components.TopAppBar
import com.example.notepad.presentation.common.components.AlertMessageDialog
import com.example.notepad.presentation.common.components.BasicTextFieldPlaceholder
import com.example.notepad.presentation.common.components.BorderedLineInputField
import com.example.notepad.presentation.common.components.FakeBlurredNoteContent
import com.example.notepad.presentation.common.components.LoadingView
import com.example.notepad.presentation.common.components.NoDataDescriptionBlock
import com.example.notepad.presentation.common.components.VerifyPasswordFrame
import com.example.notepad.presentation.common.state.LockedNoteResult
import com.example.notepad.presentation.navigation.NavigationRoutes
import com.example.notepad.presentation.common.state.NoteResult
import com.example.notepad.presentation.common.state.PasswordActionState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Suppress("ASSIGNED_VALUE_IS_NEVER_READ")
@Composable
private fun NoteEditView(
    paddingValues: PaddingValues,
    currentNote: Note,
    noteEditScreenState: NoteEditScreenViewModel,
    onEditNote: (String, String, Long) -> Unit,
    onNavigateTo: (String) -> Unit,
    onPerformHaptic: (HapticFeedbackType) -> Unit
) {
    // update note content state
    LaunchedEffect(Unit) {
        // check is note content edited
        if (!noteEditScreenState.isNoteContentEdited)
            noteEditScreenState.updateNoteContent(currentNote.content)
    }

    var errorOfEmptyNotAlertMessageDialogState by rememberSaveable { mutableStateOf(false) }
    var errorOfNoteChangesAlertMessageDialogState by rememberSaveable { mutableStateOf(false) }

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
        val noteContentInputFieldVerticalScrollState = rememberScrollState()

        // text field auto scroll
        LaunchedEffect(noteContentInputFieldVerticalScrollState.maxValue) {
            noteContentInputFieldVerticalScrollState.animateScrollTo(
                noteContentInputFieldVerticalScrollState.maxValue
            )
        }
        
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(noteContentInputFieldVerticalScrollState),
            value = noteEditScreenState.noteContent,
            onValueChange = { newValue ->
                noteEditScreenState.apply {
                    updateNoteContent(newValue) // update value
                    setNoteContentEdited()            // set is note content edited flag
                }
            },
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onPrimary),
            cursorBrush = SolidColor(if (isSystemInDarkTheme()) Color.White else Color.Black),
            decorationBox = @Composable { innerTextField ->
                BasicTextFieldPlaceholder(
                    value = noteEditScreenState.noteContent,
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

                if (noteEditScreenState.isNoteNameOrContentEmpty()) {
                    errorOfEmptyNotAlertMessageDialogState = true
                } else {
                    // check changes in note
                    if (
                        noteEditScreenState.noteName != currentNote.name ||
                        noteEditScreenState.noteContent != currentNote.content
                    ) {
                        onEditNote(
                            noteEditScreenState.noteName,
                            noteEditScreenState.noteContent,
                            currentNote.id
                        )

                        onNavigateTo(NavigationRoutes.MainScreen.route)
                    } else {
                        errorOfNoteChangesAlertMessageDialogState = true
                    }
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

    AlertMessageDialog(
        onDismissRequestFunction = { errorOfEmptyNotAlertMessageDialogState = false },
        containerColor = MaterialTheme.colorScheme.error,
        contentColor = Color.White,
        state = errorOfEmptyNotAlertMessageDialogState,
        titleIcon = painterResource(R.drawable.outline_error_outline_24),
        titleText = "Error"
    ) {
        Text(text = "Note couldn't be empty!\n- Check note name and content.")

        TextButton(
            onClick = { errorOfEmptyNotAlertMessageDialogState = false },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onError)
        ) { Text(text = "Ok") }
    }

    AlertMessageDialog(
        onDismissRequestFunction = { errorOfNoteChangesAlertMessageDialogState = false },
        containerColor = MaterialTheme.colorScheme.error,
        contentColor = Color.White,
        state = errorOfNoteChangesAlertMessageDialogState,
        titleIcon = painterResource(R.drawable.outline_error_outline_24),
        titleText = "Error"
    ) {
        Text(text = "Changes not detected! Note cannot be edited.")

        TextButton(
            onClick = { errorOfNoteChangesAlertMessageDialogState = false },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onError)
        ) { Text(text = "Ok") }
    }
}

/**
 * Creates a note edit app screen.
 *
 * @param noteId current note id.
 * @param onNavigateTo function for navigate to specific screen.
 */
@Composable
fun NoteAppEditScreen(
    noteId: Long?,
    onNavigateTo: (String) -> Unit
) {
    val noteViewModel: EditNoteViewModel = hiltViewModel()

    // select note by id
    LaunchedEffect(Unit) {
        // Wait 500 ms for the screen transition effect to finish
        delay(SCREEN_TRANSITION_DURATION.milliseconds)

        noteId?.let { id ->
            noteViewModel.selectNote(id)
        }
    }

    val currentNote by noteViewModel.currentNote.collectAsState()
    val noteEditScreenState: NoteEditScreenViewModel = viewModel()

    val decryptedNote by noteViewModel.decryptedNoteContent.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                titleContent = {
                    when (val noteState = currentNote) {
                        is NoteResult.Found -> {
                            // update note name state
                            LaunchedEffect(Unit) {
                                // check is note name edited
                                if (!noteEditScreenState.isNoteNameEdited)
                                    noteEditScreenState.updateNoteName(noteState.note.name)
                            }

                            BorderedLineInputField(
                                state = noteEditScreenState.noteName,
                                placeholder = "Edit note name",
                                buttonContentDescription = null,
                                onUpdateState = { newValue ->
                                    noteEditScreenState.apply {
                                        updateNoteName(newValue)
                                        setNoteNameEdited()
                                    }
                                },
                                onClearContent = {
                                    noteEditScreenState.updateNoteName("")
                                }
                            )
                        }
                        is NoteResult.Exception -> {
                            Text(
                                text = "Error",
                                fontWeight = FontWeight.Bold
                            )
                        }
                        NoteResult.NotFound -> {
                            Text(
                                text = "Not founded",
                                fontWeight = FontWeight.Bold
                            )
                        }
                        NoteResult.Loading -> {
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        is NoteResult.Locked -> {
                            when (val decryptedState = decryptedNote) {
                                is LockedNoteResult.Decrypted -> {
                                    // update note name state
                                    LaunchedEffect(Unit) {
                                        // check is note name edited
                                        if (!noteEditScreenState.isNoteNameEdited)
                                            noteEditScreenState.updateNoteName(decryptedState.decryptedNote.name)
                                    }

                                    BorderedLineInputField(
                                        state = noteEditScreenState.noteName,
                                        placeholder = "Edit note name",
                                        buttonContentDescription = null,
                                        onUpdateState = { newValue ->
                                            noteEditScreenState.apply {
                                                updateNoteName(newValue)
                                                setNoteNameEdited()
                                            }
                                        },
                                        onClearContent = {
                                            noteEditScreenState.updateNoteName("")
                                        }
                                    )
                                }
                                LockedNoteResult.Decrypting -> {
                                    LinearProgressIndicator(
                                        modifier = Modifier.fillMaxWidth(),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                                LockedNoteResult.Encrypted -> {
                                    Text(
                                        text = "The note is locked.",
                                        modifier = Modifier.blur(10.dp),
                                    )
                                }
                            }
                        }
                    }
                },
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
            val haptic = LocalHapticFeedback.current

            when (val noteState = currentNote) {
                is NoteResult.Found -> {
                    NoteEditView(
                        paddingValues = innerPadding,
                        currentNote = noteState.note,
                        onEditNote = noteViewModel::editNote,
                        onNavigateTo = onNavigateTo,
                        onPerformHaptic = haptic::performHapticFeedback,
                        noteEditScreenState = noteEditScreenState
                    )
                }
                is NoteResult.Exception -> {
                    NoDataDescriptionBlock(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        description = noteState.message
                    )
                }
                NoteResult.NotFound -> {
                    NoDataDescriptionBlock(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        description = "This note, not found."
                    )
                }
                NoteResult.Loading -> {
                    LoadingView(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        description = "Loading note, please wait."
                    )
                }
                is NoteResult.Locked -> {
                    // matching locked note state
                    when (val textState = decryptedNote) {
                        is LockedNoteResult.Decrypted -> {
                            NoteEditView(
                                paddingValues = innerPadding,
                                currentNote = textState.decryptedNote,
                                onEditNote = noteViewModel::editNote,
                                onNavigateTo = onNavigateTo,
                                onPerformHaptic = haptic::performHapticFeedback,
                                noteEditScreenState = noteEditScreenState
                            )
                        }
                        LockedNoteResult.Decrypting -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            ) {
                                LoadingView(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding),
                                    description = "Decrypting note..."
                                )
                            }
                        }
                        LockedNoteResult.Encrypted -> {
                            FakeBlurredNoteContent(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            )

                            val password by noteViewModel.passwordString.collectAsState()
                            val passwordState by noteViewModel.passwordState.collectAsState()

                            var dialogState by rememberSaveable { mutableStateOf(true) }

                            AlertMessageDialog(
                                state = dialogState,
                                onDismissRequestFunction = {},
                                titleIcon = painterResource(R.drawable.outline_lock_open_24),
                                titleText = "Enter password of note"
                            ) {
                                VerifyPasswordFrame(
                                    passwordValue = password,
                                    onPasswordValueChanged = { newValue ->
                                        noteViewModel.updatePassword(newValue)
                                    },
                                    passwordHint = noteState.lockedNote.passwordHint,
                                    currentPasswordState = passwordState
                                )

                                Row {
                                    Spacer(modifier = Modifier.weight(1.0f))

                                    TextButton(
                                        onClick = {
                                            dialogState = false
                                            onNavigateTo(NavigationRoutes.MainScreen.route)
                                        }
                                    ) {
                                        Text(text = "Cancel")
                                    }

                                    TextButton(onClick = { noteViewModel.updatePasswordState(PasswordActionState.Submit) }) {
                                        Text(text = "Unlock")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}