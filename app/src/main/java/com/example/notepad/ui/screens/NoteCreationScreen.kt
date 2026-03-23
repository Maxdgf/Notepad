package com.example.notepad.ui.screens

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.notepad.R

import com.example.notepad.ui.components.TopUiBar
import com.example.notepad.ui.components.AlertUiMessageDialog
import com.example.notepad.ui.components.BasicTextFieldUiPlaceholder
import com.example.notepad.ui.navigation.NavigationRoutes
import com.example.notepad.ui.navigation.Navigator
import com.example.notepad.ui.view_models.NoteViewModel

/**Creates a note creation app screen.*/
@Composable
fun NoteUiCreationScreen(
    navigator: Navigator,
    noteViewModel: NoteViewModel = hiltViewModel()
) {
    val haptic = LocalHapticFeedback.current
    val noteContentInputFieldVerticalScrollState = rememberScrollState()

    var noteNameState by rememberSaveable { mutableStateOf("") }
    var noteContentState by rememberSaveable { mutableStateOf("") }
    var errorOfEmptyNoteAlertMessageDialogState by rememberSaveable { mutableStateOf(false) }

    // text field auto scroll
    LaunchedEffect(noteContentInputFieldVerticalScrollState.maxValue) {
        noteContentInputFieldVerticalScrollState.animateScrollTo(noteContentInputFieldVerticalScrollState.maxValue)
    }

    Scaffold(
        topBar = {
            TopUiBar(
                titleContent = {
                    Row {
                        OutlinedTextField(
                            maxLines = 1,
                            modifier = Modifier.fillMaxWidth(),
                            value = noteNameState,
                            onValueChange = { newValue -> noteNameState = newValue },
                            trailingIcon = {
                                IconButton(onClick = { noteNameState = "" }) {
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
                    }
                },
                barIcon = {
                    IconButton(onClick = { navigator.navigateTo(NavigationRoutes.MainScreen.route) }) {
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
            ) {
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(noteContentInputFieldVerticalScrollState),
                    value = noteContentState,
                    onValueChange = { newValue -> noteContentState = newValue },
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onPrimary),
                    cursorBrush = SolidColor(if (isSystemInDarkTheme()) Color.White else Color.Black),
                    decorationBox = @Composable { innerTextField ->
                        BasicTextFieldUiPlaceholder(
                            value = noteContentState,
                            placeholderText = "Write here anything...",
                            startPadding = 5.dp,
                            innerTextField = innerTextField
                        )
                    }
                )

                HorizontalDivider()

                Button(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress) // haptic

                        if (noteNameState.isEmpty() || noteContentState.isEmpty()) {
                            errorOfEmptyNoteAlertMessageDialogState = true
                        } else {
                            // add note to database
                            noteViewModel.addNote(
                                name = noteNameState,
                                content = noteContentState,
                            )

                            // navigate to main screen
                            navigator.navigateTo(NavigationRoutes.MainScreen.route)
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

            AlertUiMessageDialog(
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