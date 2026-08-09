package com.example.notepad.presentation.note_view

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlin.math.roundToInt
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import com.example.notepad.R
import com.example.notepad.domain.model.Note
import com.example.notepad.presentation.common.SCREEN_TRANSITION_DURATION
import com.example.notepad.presentation.common.state.NoteResult
import com.example.notepad.presentation.common.components.AlertMessageDialog
import com.example.notepad.presentation.common.components.DropdownMenuIconItem
import com.example.notepad.presentation.common.components.FakeBlurredNoteContent
import com.example.notepad.presentation.common.components.LoadingView
import com.example.notepad.presentation.common.components.NoDataDescriptionBlock
import com.example.notepad.presentation.common.components.VerifyPasswordFrame
import com.example.notepad.presentation.common.components.SwitchWithText
import com.example.notepad.presentation.common.components.TopAppBar
import com.example.notepad.presentation.common.state.LockedNoteResult
import com.example.notepad.presentation.common.state.PasswordActionState
import com.example.notepad.presentation.common.utils.ClipBoardManager
import com.example.notepad.presentation.common.utils.DateTimeFormatter
import com.example.notepad.presentation.navigation.NavigationRoutes
import kotlin.time.Duration.Companion.milliseconds

// text size selector slider steps count
private const val TEXT_SIZE_SLIDER_STEPS = 8

/**
 * Creates a dropdown menu.
 *
 * @param textWrap text wrap mode flag.
 * @param currentFontSize current text size.
 * @param onUpdateCurrentFontSize function of update text size.
 * @param onCopyNoteContent copy note content function.
 * @param onUpdateTextWrapState function of update text wrap mode state.
 */
@Composable
private fun ScreenDropdownMenu(
    textWrap: Boolean,
    currentFontSize: Int,
    onUpdateCurrentFontSize: (Int) -> Unit,
    onCopyNoteContent: () -> Unit,
    onUpdateTextWrapState: (Boolean) -> Unit
) {
    var dropdownMenuState by rememberSaveable { mutableStateOf(false) }
    var fontSizeDialogState by rememberSaveable { mutableStateOf(false) }

    Box {
        IconButton(onClick = { dropdownMenuState = true }) {
            Icon(
                painter = painterResource(R.drawable.baseline_more_vert_24),
                contentDescription = null
            )
        }

        DropdownMenu(
            expanded = dropdownMenuState,
            onDismissRequest = { dropdownMenuState = false }
        ) {
            DropdownMenuIconItem(
                onClick = {
                    dropdownMenuState = false // hide menu
                    fontSizeDialogState = true
                },
                iconPainter = painterResource(R.drawable.baseline_text_format_24),
                text = "text size",
                contentDescription = null
            )

            Box(modifier = Modifier.padding(horizontal = 10.dp)) {
                DropdownMenuIconItem(
                    onClick = {
                        dropdownMenuState = false // hide menu
                        onCopyNoteContent()
                    },
                    iconPainter = painterResource(R.drawable.baseline_content_copy_24),
                    text = "copy note",
                    contentDescription = null
                )
            }

            HorizontalDivider()

            val hideMenuScope = rememberCoroutineScope()
            SwitchWithText(
                modifier = Modifier.padding(horizontal = 5.dp),
                checked = textWrap,
                text = "text wrap",
                onCheckedChange = { state ->
                   onUpdateTextWrapState(state)
                   hideMenuScope.launch {
                       delay(250.milliseconds)     // delay 250 ms
                       dropdownMenuState = false // hide menu
                   }
                }
            )
        }

        AlertMessageDialog(
            state = fontSizeDialogState,
            onDismissRequestFunction = { fontSizeDialogState = false },
            titleIcon = painterResource(R.drawable.baseline_text_format_24),
            titleText = "Text format"
        ) {
            Text(text = "Text size $currentFontSize sp")

            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = currentFontSize / 100f,
                steps = TEXT_SIZE_SLIDER_STEPS,
                valueRange = 0.1f..0.3f,
                onValueChange = { value ->
                    onUpdateCurrentFontSize((value * 100).roundToInt())
                }
            )

            Row {
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { fontSizeDialogState = false },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Close",
                        color = Color.White
                    )
                }
            }
        }
    }
}

/**
 * Creates a note data title(note name, creation datetime, symbols count).
 * @param note current note entity.
 */
@Composable
private fun NoteTitle(note: Note) {
    val dateTimeFormatter = remember { DateTimeFormatter() }

    // note title
    Column {
        // note name
        Text(
            text = note.name,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.basicMarquee(Int.MAX_VALUE),
            fontSize = 16.sp
        )

        Row {
            // note datetime of creation
            Text(
                text = dateTimeFormatter.formatDatetimeNow(note.creationTime),
                fontWeight = FontWeight.Light,
                modifier = Modifier.basicMarquee(Int.MAX_VALUE),
                fontSize = 10.sp
            )

            // note symbols count(note content length)
            Text(
                text = "${note.content.length} symbols",
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .basicMarquee(Int.MAX_VALUE),
                fontSize = 10.sp
            )
        }
    }
}

/**
 * Creates a note content view.
 *
 * @param content note text content.
 * @param currentFontSize current note text content font size.
 * @param isTextWrapEnabled text wrap mode flag.
 * @param paddingValues ui padding values.
 */
@Composable
private fun NoteContentView(
    content: String,
    currentFontSize: Int,
    isTextWrapEnabled: Boolean,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        val haptic = LocalHapticFeedback.current
        val verticalScrollState = rememberScrollState()

        LaunchedEffect(Unit) {
            snapshotFlow { verticalScrollState.value }
                .collect { value ->
                    // when scroll value reached max value -> perform haptic feedback
                    if (value == verticalScrollState.maxValue)
                        haptic.performHapticFeedback(HapticFeedbackType.GestureEnd)
                }
        }

        // note content scroll value indicator
        LinearProgressIndicator(
            progress = {
                verticalScrollState.run { this.value / this.maxValue.toFloat() }
            },
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onPrimary,
            drawStopIndicator = {} // without stop indicator
        )

        SelectionContainer {
            // note text content view
            if (isTextWrapEnabled) {
                // with only vertical scroll
                Text(
                    text = content,
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(verticalScrollState),
                    fontSize = currentFontSize.sp
                )
            } else {
                // with both vertical and horizontal scroll
                val horizontalScrollState = rememberScrollState()
                Text(
                    text = content,
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(horizontalScrollState)
                        .verticalScroll(verticalScrollState),
                    fontSize = currentFontSize.sp
                )
            }
        }
    }
}

/**Creates a note view app screen.*/
@Composable
fun NoteAppViewScreen(
    noteId: Long?,
    onNavigateTo: (String) -> Unit
) {
    val noteViewModel: ViewNoteViewModel = hiltViewModel()
    val settingsViewModel: NoteViewSettingsViewModel = hiltViewModel()

    val context = LocalContext.current

    val currentNote by noteViewModel.currentNote.collectAsState()
    val noteViewSettings by settingsViewModel.noteViewSettings.collectAsState()

    // select note by id
    LaunchedEffect(Unit) {
        // Wait 500 ms for the screen transition effect to finish
        delay(SCREEN_TRANSITION_DURATION.milliseconds)

        noteId?.let { id ->
            noteViewModel.selectNote(id)
        }
    }

    val decryptedNote by noteViewModel.decryptedNoteContent.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                titleContent = {
                    // matching note state
                    when (val noteState = currentNote) {
                        is NoteResult.Found -> NoteTitle(note = noteState.note)
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
                            // matching locked note state
                            when (val textState = decryptedNote) {
                                is LockedNoteResult.Decrypted -> NoteTitle(note = textState.decryptedNote)
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
                            painter = painterResource(R.drawable.baseline_arrow_back_24),
                            contentDescription = null
                        )
                    }
                },
                barActionElements = {
                    val clipBoardManager = remember { ClipBoardManager(context) }

                    // matching note state
                    when (val noteState = currentNote) {
                        is NoteResult.Found -> {
                            ScreenDropdownMenu(
                                textWrap = noteViewSettings.isTextWrapEnabled,
                                currentFontSize = noteViewSettings.noteTextSize,
                                onUpdateCurrentFontSize = { size ->
                                    settingsViewModel.performEvent(
                                        NoteViewSettingsEvent.UpdateTextSizeState(size)
                                    )
                                },
                                onCopyNoteContent = {
                                    clipBoardManager.setTextToClipboard(noteState.note.content)
                                },
                                onUpdateTextWrapState = { state ->
                                    settingsViewModel.performEvent(
                                        NoteViewSettingsEvent.UpdateTextWrapState(state)
                                    )
                                }
                            )
                        }
                        is NoteResult.Locked -> {
                            val decrypted = decryptedNote
                            if (decrypted is LockedNoteResult.Decrypted) {
                                ScreenDropdownMenu(
                                    textWrap = noteViewSettings.isTextWrapEnabled,
                                    currentFontSize = noteViewSettings.noteTextSize,
                                    onUpdateCurrentFontSize = { size ->
                                        settingsViewModel.performEvent(
                                            NoteViewSettingsEvent.UpdateTextSizeState(size)
                                        )
                                    },
                                    onCopyNoteContent = {
                                        clipBoardManager.setTextToClipboard(decrypted.decryptedNote.content)
                                    },
                                    onUpdateTextWrapState = { state ->
                                        settingsViewModel.performEvent(
                                            NoteViewSettingsEvent.UpdateTextWrapState(state)
                                        )
                                    }
                                )
                            }
                        }
                        else -> {} // nothing show
                    }
                }
            )
        },
        content = { innerPadding ->
            // matching note state
            when (val noteState = currentNote) {
                is NoteResult.Found -> {
                    NoteContentView(
                        content = noteState.note.content,
                        currentFontSize = noteViewSettings.noteTextSize,
                        isTextWrapEnabled = noteViewSettings.isTextWrapEnabled,
                        paddingValues = innerPadding
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
                is NoteResult.Locked -> {
                    // matching locked note state
                    when (val textState = decryptedNote) {
                        is LockedNoteResult.Decrypted -> {
                            NoteContentView(
                                content = textState.decryptedNote.content,
                                currentFontSize = noteViewSettings.noteTextSize,
                                isTextWrapEnabled = noteViewSettings.isTextWrapEnabled,
                                paddingValues = innerPadding
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
            }
        }
    )
}