package com.example.notepad.ui.screens

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.UUID
import kotlin.random.Random

import com.example.notepad.R
import com.example.notepad.core.data_management.databases.notes_local_storage.NoteEntity
import com.example.notepad.core.utils.DateTimePicker
import com.example.notepad.ui.components.screen_components.TopUiBar
import com.example.notepad.ui.components.ui_components.AlertUiMessageDialog
import com.example.notepad.ui.components.ui_components.BasicTextFieldUiPlaceholder
import com.example.notepad.ui.components.ui_components.BottomUiSheetActionDialog
import com.example.notepad.ui.utils.CurrentThemeColor

@Composable
fun NoteUiCreationScreen(
    navigationController: NavController,
    dateTimePicker: DateTimePicker,
    noteNameState: String,
    noteContentState: String,
    errorOfEmptyNotAlertMessageDialogState: Boolean,
    updateNoteNameStateMethod: (newValue: String) -> Unit,
    updateNoteContentStateMethod: (newValue: String) -> Unit,
    clearNoteNameStateMethod: () -> Unit,
    clearNoteContentStateMethod: () -> Unit,
    isNoteNameAnContentEmptyMethod: () -> Boolean,
    updateErrorOfEmptyNotAlertMessageDialogStateMethod: (state: Boolean) -> Unit,
    addNoteMethod: (note: NoteEntity) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    val noteContentInputFieldVerticalScrollState = rememberScrollState()
    val noteContentInputFieldHorizontalScrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopUiBar(
                titleContent = {
                    Row {
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
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                                cursorColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                },
                barIcon = {
                    IconButton(onClick = {
                        navigationController.navigate(NavigationRoutes.MainScreen.route)

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
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onPrimary),
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
                        if (isNoteNameAnContentEmptyMethod()) {
                            updateErrorOfEmptyNotAlertMessageDialogStateMethod(true)
                        } else {
                            addNoteMethod(
                                NoteEntity(
                                    id = Random.nextInt(),
                                    name = noteNameState,
                                    content = noteContentState,
                                    uuid = UUID.randomUUID().toString(),
                                    dateTime = dateTimePicker.pickDateTimeNow(),
                                    lastEditDateTime = null
                                )
                            )

                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                            clearNoteNameStateMethod()
                            clearNoteContentStateMethod()

                            navigationController.navigate(NavigationRoutes.MainScreen.route)
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
                onDismissRequestFunction = { updateErrorOfEmptyNotAlertMessageDialogStateMethod(false) },
                color = MaterialTheme.colorScheme.error,
                state = errorOfEmptyNotAlertMessageDialogState
            ) {
                Column {
                    Text(
                        text = "Note couldn't be empty!\n- Check note name and content.",
                        color = Color.White
                    )

                    Button(
                        onClick = { updateErrorOfEmptyNotAlertMessageDialogStateMethod(false) },
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
