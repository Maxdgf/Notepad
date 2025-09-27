package com.example.notepad.ui.screens

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import com.example.notepad.core.data_models.SelectedNote
import com.example.notepad.core.utils.DateTimePicker
import com.example.notepad.ui.components.screen_components.TopUiBar
import com.example.notepad.ui.components.ui_components.AlertUiMessageDialog
import com.example.notepad.ui.components.ui_components.BasicTextFieldUiPlaceholder
import com.example.notepad.ui.utils.CurrentThemeColor

@Composable
fun NoteUiEditScreen(
    navigationController: NavController,
    noteNameState: String,
    noteContentState: String,
    selectedNoteState: SelectedNote,
    errorOfEmptyNotAlertMessageDialogState: Boolean,
    updateNoteNameStateMethod: (newValue: String) -> Unit,
    updateNoteContentStateMethod: (newValue: String) -> Unit,
    clearNoteNameStateMethod: () -> Unit,
    clearNoteContentStateMethod: () -> Unit,
    clearSelectedNoteStateMethod: () -> Unit,
    isNoteNameAnContentEmptyMethod: () -> Boolean,
    errorOfNoteChangesAlertMessageDialogState: Boolean,
    updateErrorOfEmptyNotAlertMessageDialogStateMethod: (state: Boolean) -> Unit,
    editNoteMethod: (
        name: String,
        content: String,
        lastEditDateTime: String,
        uuid: String
    ) -> Unit,
    updateErrorOfNoteChangesAlertMessageDialogStateMethod: (state: Boolean) -> Unit
) {
    val dateTimePicker = DateTimePicker()
    val currentThemeColor = CurrentThemeColor()

    val haptic = LocalHapticFeedback.current

    val noteContentInputFieldVerticalScrollState = rememberScrollState()
    val noteContentInputFieldHorizontalScrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopUiBar(
                titleContent = {
                    OutlinedTextField(
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth(),
                        value = noteNameState,
                        onValueChange = { newValue -> updateNoteNameStateMethod(newValue) },
                        trailingIcon = {
                            IconButton(onClick = { clearNoteNameStateMethod() }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Note name input field clear text icon."
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
                        }
                    )
                },
                barIcon = {
                    IconButton(onClick = {
                        navigationController.navigate(NavigationRoutes.MainScreen.route)

                        clearSelectedNoteStateMethod()
                        clearNoteNameStateMethod()
                        clearNoteContentStateMethod()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Return to main screen icon button."
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(noteContentInputFieldVerticalScrollState)
                        .horizontalScroll(noteContentInputFieldHorizontalScrollState),
                    value = noteContentState,
                    onValueChange = { newValue -> updateNoteContentStateMethod(newValue) },
                    textStyle = TextStyle(color = currentThemeColor.getAdaptedCurrentThemeColor(false)),
                    decorationBox = @Composable { innerTextField ->
                        BasicTextFieldUiPlaceholder(
                            value = noteContentState,
                            placeholderText = "Write here anything...",
                            startPadding = 5.dp,
                            innerTextField = innerTextField
                        )
                    }
                )

                Button(
                    onClick = {
                        if (isNoteNameAnContentEmptyMethod()) {
                            updateErrorOfEmptyNotAlertMessageDialogStateMethod(true)
                        } else {
                            //checking changes in note content.
                            if (noteContentState != selectedNoteState.content || noteNameState != selectedNoteState.name) {
                                editNoteMethod(
                                    noteNameState,
                                    noteContentState,
                                    dateTimePicker.pickDateTimeNow(),
                                    selectedNoteState.uuid
                                )

                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                                clearNoteNameStateMethod()
                                clearNoteContentStateMethod()

                                navigationController.navigate(NavigationRoutes.MainScreen.route)
                            } else {
                                updateErrorOfNoteChangesAlertMessageDialogStateMethod(true)
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

            AlertUiMessageDialog(
                onDismissRequestFunction = { updateErrorOfEmptyNotAlertMessageDialogStateMethod(false) },
                color = MaterialTheme.colorScheme.error,
                state = errorOfEmptyNotAlertMessageDialogState
            ) {
                Column {
                    Text(text = "Note couldn't be empty!")

                    Button(
                        onClick = { updateErrorOfEmptyNotAlertMessageDialogStateMethod(false) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onError)
                    ) {
                        Text(
                            text = "Ok",
                            color = Color.White
                        )
                    }
                }
            }

            AlertUiMessageDialog(
                onDismissRequestFunction = { updateErrorOfNoteChangesAlertMessageDialogStateMethod(false) },
                color = MaterialTheme.colorScheme.error,
                state = errorOfNoteChangesAlertMessageDialogState
            ) {
                Column {
                    Text(
                        text = "Changes not detected!",
                        color = Color.White
                    )

                    Button(
                        onClick = { updateErrorOfNoteChangesAlertMessageDialogStateMethod(false) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onError)
                    ) {
                        Text(
                            text = "Ok",
                            color = Color.White
                        )
                    }
                }
            }
        }
    )
}