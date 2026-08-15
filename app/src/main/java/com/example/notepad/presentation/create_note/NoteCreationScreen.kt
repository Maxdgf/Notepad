package com.example.notepad.presentation.create_note

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import com.example.notepad.R
import com.example.notepad.domain.model.Note
import com.example.notepad.presentation.common.components.TopAppBar
import com.example.notepad.presentation.common.components.AlertMessageDialog
import com.example.notepad.presentation.common.components.BasicTextFieldPlaceholder
import com.example.notepad.presentation.common.components.BorderedLineInputField
import com.example.notepad.presentation.common.components.DescriptionField
import com.example.notepad.presentation.common.components.ErrorField
import com.example.notepad.presentation.common.theme.arlekin
import com.example.notepad.presentation.navigation.NavigationRoutes

/**
 * Creates a note creation app screen.
 *
 * @param onNavigateTo navigate to specific screen function.
 * @param onAddNote add note to database function.
 */
@Composable
fun NoteAppCreationScreen(
    onNavigateTo: (String) -> Unit,
    onAddNote: suspend (password: String?, note: Note) -> Unit
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

    var passwordInputDialogState by remember { mutableStateOf(false) }

    val passwordString by noteCreationScreenViewModel.passwordString.collectAsState()
    val passwordValidationState by noteCreationScreenViewModel.passwordValidationState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                titleContent = {
                    BorderedLineInputField(
                        state = noteCreationScreenViewModel.noteName,
                        placeholder = "Enter note name...",
                        buttonContentDescription = null,
                        onUpdateState = { newValue ->
                            noteCreationScreenViewModel.noteName = newValue
                        },
                        onClearContent = {
                            noteCreationScreenViewModel.noteName = ""
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
                },
                barActionElements = {
                    IconButton(onClick = { passwordInputDialogState = true }) {
                        Icon(
                            painter = painterResource(R.drawable.outline_lock_24),
                            contentDescription = null,
                            tint = if (passwordString.isNotBlank() && passwordValidationState == PasswordValidation.Valid) {
                                arlekin
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
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
                    onValueChange = { newValue -> noteCreationScreenViewModel.noteContent = newValue },
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
                                    passwordString.ifBlank { null },
                                    Note(
                                        name = noteCreationScreenViewModel.noteName.trim(),
                                        content = noteCreationScreenViewModel.noteContent,
                                        passwordHint = noteCreationScreenViewModel.passwordHint.ifBlank { null }
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
                state = passwordInputDialogState,
                onDismissRequestFunction = {},
                titleIcon = painterResource(R.drawable.outline_lock_24),
                titleText = "Set password to note"
            ) {
                var isPasswordVisible by remember { mutableStateOf(false) }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = passwordString,
                    onValueChange = { newValue ->
                        noteCreationScreenViewModel.updatePasswordString(newValue)
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(mask = '*'),
                    label = { Text(text = "Password") },
                    leadingIcon = {
                        IconToggleButton(
                            checked = isPasswordVisible,
                            onCheckedChange = { state -> isPasswordVisible = state }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.outline_visibility_24),
                                contentDescription = null
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = { noteCreationScreenViewModel.updatePasswordString("") }) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_clear_24),
                                contentDescription = null
                            )
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (passwordValidationState == PasswordValidation.Valid) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            Color.Red
                        },
                        unfocusedBorderColor = if (passwordValidationState == PasswordValidation.Valid) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            Color.Red
                        }.copy(alpha = 0.5f),
                        focusedLabelColor = if (passwordValidationState == PasswordValidation.Valid) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            Color.Red
                        },
                        unfocusedLabelColor = if (passwordValidationState == PasswordValidation.Valid) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            Color.Red
                        }.copy(alpha = 0.5f)
                    )
                )

                // show password validation state
                when (passwordValidationState) {
                    PasswordValidation.IsEmpty -> ErrorField("password couldn't be empty!")
                    PasswordValidation.IsBlank -> ErrorField("password couldn't be blank!")
                    PasswordValidation.WhiteSpacesAtStartAndEndFound -> ErrorField("password must not contain spaces at the beginning or the end.")
                    PasswordValidation.ToShort -> ErrorField("password to short! Min length is $MIN_PASSWORD_LENGTH chars.")
                    PasswordValidation.ToLong -> ErrorField("password to long! Max length is $MAX_PASSWORD_LENGTH chars.")
                    PasswordValidation.EngLettersDigitsSpecCharsExcepted -> ErrorField("only eng letters, digits and spec chars excepted!")
                    PasswordValidation.FewLetters -> ErrorField("few letters in password! Min letters count is $MIN_LETTERS_COUNT.")
                    PasswordValidation.FewDigits -> ErrorField("few digits in password! Min digits count is $MIN_DIGITS_COUNT.")
                    PasswordValidation.FewSpecialChars -> ErrorField("few spec chars in password! Min spec chars count is $MIN_SPEC_CHARS_COUNT.")
                    PasswordValidation.Valid -> {}
                }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = noteCreationScreenViewModel.passwordHint,
                    onValueChange = { newValue -> noteCreationScreenViewModel.passwordHint = newValue },
                    label = { Text(text = "Password hint") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                    )
                )

                DescriptionField(
                    description = "Don't write a real password into password hint field!",
                    iconPainter = painterResource(R.drawable.outline_error_outline_24)
                )

                Row {
                    Spacer(modifier = Modifier.weight(1.0f))

                    TextButton(
                        onClick = {
                            passwordInputDialogState = false
                            noteCreationScreenViewModel.updatePasswordString("")
                        }
                    ) { Text(text = "Cancel") }

                    TextButton(
                        onClick = { passwordInputDialogState = false },
                        enabled = passwordValidationState == PasswordValidation.Valid
                    ) { Text(text = "Apply") }
                }
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