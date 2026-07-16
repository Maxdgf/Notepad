package com.example.notepad.presentation.create_note

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.notepad.R
import com.example.notepad.domain.model.Note
import com.example.notepad.presentation.common.components.TopAppBar
import com.example.notepad.presentation.common.components.AlertMessageDialog
import com.example.notepad.presentation.common.components.BasicTextFieldPlaceholder
import com.example.notepad.presentation.common.components.BorderedLineInputField
import com.example.notepad.presentation.navigation.NavigationRoutes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Creates a note creation app screen.
 *
 * @param onNavigateTo navigate to specific screen function.
 * @param onAddNote add note to database function.
 */
@Composable
fun NoteAppCreationScreen(
    onNavigateTo: (String) -> Unit,
    onAddNote: suspend (note: Note) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val noteCreationScreenViewModel: NoteCreationScreenViewModel = viewModel()

    val noteContentInputFieldVerticalScrollState = rememberScrollState()
    var errorOfEmptyNoteAlertMessageDialogState by rememberSaveable { mutableStateOf(false) }

    // text field auto scroll
    LaunchedEffect(noteContentInputFieldVerticalScrollState.maxValue) {
        noteContentInputFieldVerticalScrollState.animateScrollTo(
            noteContentInputFieldVerticalScrollState.maxValue
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                titleContent = {
                    BorderedLineInputField(
                        state = noteCreationScreenViewModel.noteName,
                        placeholder = "Enter note name...",
                        buttonContentDescription = null,
                        onUpdateState = { newValue ->
                            noteCreationScreenViewModel.updateNoteName(newValue)
                        },
                        onClearContent = {
                            noteCreationScreenViewModel.updateNoteName("")
                        }
                    )
                },
                barIcon = {
                    IconButton(onClick = { onNavigateTo(NavigationRoutes.MainScreen.route) }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_arrow_back_24),
                            contentDescription = null
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .imePadding()
                    .fillMaxSize()
                    .padding(
                        top = 5.dp,
                        start = 5.dp,
                        end = 5.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(noteContentInputFieldVerticalScrollState),
                    value = noteCreationScreenViewModel.noteContent,
                    onValueChange = { newValue -> noteCreationScreenViewModel.updateNoteContent(newValue) },
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onPrimary),
                    cursorBrush = SolidColor(if (isSystemInDarkTheme()) Color.White else Color.Black),
                    decorationBox = @Composable { innerTextField ->
                        BasicTextFieldPlaceholder(
                            value = noteCreationScreenViewModel.noteContent,
                            placeholderText = "Write here anything...",
                            startPadding = 5.dp,
                            innerTextField = innerTextField
                        )
                    }
                )

                HorizontalDivider()

                val coroutineScope = rememberCoroutineScope()
                Button(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress) // haptic

                        if (noteCreationScreenViewModel.isNoteNameOrContentEmpty()) {
                            errorOfEmptyNoteAlertMessageDialogState = true
                        } else {
                            coroutineScope.launch {
                                // add note to database
                                onAddNote(
                                    Note(
                                        name = noteCreationScreenViewModel.noteName,
                                        content = noteCreationScreenViewModel.noteContent
                                    )
                                )

                                withContext(Dispatchers.Main) {
                                    // navigate to main screen
                                    onNavigateTo(NavigationRoutes.MainScreen.route)
                                }
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
                ) { Text(text = "create note") }
            }

            AlertMessageDialog(
                onDismissRequestFunction = { errorOfEmptyNoteAlertMessageDialogState = false },
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = Color.White,
                state = errorOfEmptyNoteAlertMessageDialogState,
                titleIcon = painterResource(R.drawable.outline_error_outline_24),
                titleText = "Error"
            ) {
                Text(text = "Note couldn't be empty!\n- Check note name and content.")

                Button(
                    onClick = { errorOfEmptyNoteAlertMessageDialogState = false },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onError)
                ) { Text(text = "Ok") }
            }
        }
    )
}