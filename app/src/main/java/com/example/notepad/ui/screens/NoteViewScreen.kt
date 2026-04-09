package com.example.notepad.ui.screens

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import com.example.notepad.R
import com.example.notepad.core.data_management.databases.notes_local_storage.entities.NoteEntity
import com.example.notepad.ui.components.TopUiBar
import com.example.notepad.ui.components.AlertUiMessageDialog
import com.example.notepad.ui.components.CheckBoxWithUiText
import com.example.notepad.ui.components.LoadingUiBlock
import com.example.notepad.ui.components.NoDataUiDescriptionBlock
import com.example.notepad.ui.screens.navigation.NavigationRoutes
import com.example.notepad.ui.states.NoteResult
import com.example.notepad.ui.viewmodels.AppDataStoreViewModel
import com.example.notepad.ui.viewmodels.NoteViewModel
import com.example.notepad.utils.ClipBoardManager
import com.example.notepad.utils.DateTimeFormatter

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
            DropdownMenuItem(
                onClick = {
                    dropdownMenuState = false // hide menu
                    fontSizeDialogState = true
                },
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_text_format_24),
                            contentDescription = null
                        )
                        Text(text = "text size")
                    }
                }
            )

            DropdownMenuItem(
                onClick = {
                    dropdownMenuState = false // hide menu
                    onCopyNoteContent()
                },
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_content_copy_24),
                            contentDescription = null
                        )
                        Text(text = "copy")
                    }
                }
            )

            HorizontalDivider()

            val hideMenuScope = rememberCoroutineScope()
            CheckBoxWithUiText(
                checked = textWrap,
                text = "text wrap",
                onCheckedChange = { state ->
                    onUpdateTextWrapState(state)
                   hideMenuScope.launch {
                       delay(250)
                       dropdownMenuState = false // hide menu
                   }
                }
            )
        }

        AlertUiMessageDialog(
            state = fontSizeDialogState,
            onDismissRequestFunction = { fontSizeDialogState = false },
            titleIcon = painterResource(R.drawable.baseline_text_format_24),
            titleText = "Text format"
        ) {
            Text(text = "Text size $currentFontSize sp")

            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = currentFontSize / 100f,
                steps = 8,
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
private fun NoteTitle(note: NoteEntity) {
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
                text = dateTimeFormatter.formatDatetimeNow(note.dateTime),
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
    println("note view!")
    SelectionContainer {
        val verticalScrollState = rememberScrollState()

        // note text content view
        if (isTextWrapEnabled) {
            // with only vertical scroll
            Text(
                text = content,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(verticalScrollState),
                fontSize = currentFontSize.sp
            )
        } else {
            // with both vertical and horizontal scroll
            val horizontalScrollState = rememberScrollState()
            Text(
                text = content,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .horizontalScroll(horizontalScrollState)
                    .verticalScroll(verticalScrollState),
                fontSize = currentFontSize.sp
            )
        }
    }
}

/**Creates a note view app screen.*/
@Composable
fun NoteUiViewScreen(
    noteId: Long?,
    onNavigateTo: (String) -> Unit,
    noteViewModel: NoteViewModel,
    appDataStoreViewModel: AppDataStoreViewModel
) {
    val context = LocalContext.current

    val currentNote by noteViewModel.currentNote.collectAsState()
    val textWrapState by appDataStoreViewModel.textWrapMode.collectAsState()
    val currentFontSize by appDataStoreViewModel.noteTextSize.collectAsState()

    LaunchedEffect(Unit) {
        noteId?.let { id ->
            noteViewModel.selectNote(id)
        }
    }

    Scaffold(
        topBar = {
            TopUiBar(
                titleContent = {
                    when (val noteState = currentNote) {
                        is NoteResult.SuccessfullyLoaded ->
                            NoteTitle(note = noteState.note)
                        is NoteResult.LoadedWithException ->
                            Text(
                                text = "Error",
                                fontWeight = FontWeight.Bold
                            )
                        is NoteResult.NotFounded ->
                            Text(
                                text = "Not founded",
                                fontWeight = FontWeight.Bold
                            )
                        NoteResult.NoteLoading ->
                            LoadingUiBlock(
                                showLoadingBar = false,
                                description = "Loading note..."
                            )
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

                    when (val noteState = currentNote) {
                        is NoteResult.SuccessfullyLoaded ->
                            ScreenDropdownMenu(
                                textWrap = textWrapState,
                                currentFontSize = currentFontSize,
                                onUpdateCurrentFontSize = appDataStoreViewModel::saveNoteTextSize,
                                onCopyNoteContent = {
                                    clipBoardManager.setTextToClipboard(noteState.note.content)
                                },
                                onUpdateTextWrapState = appDataStoreViewModel::saveTextWrapState
                            )
                        else -> {} // nothing show
                    }
                }
            )
        },
        content = { innerPadding ->
            when (val noteState = currentNote) {
                is NoteResult.SuccessfullyLoaded ->
                    NoteContentView(
                        content = noteState.note.content,
                        currentFontSize = currentFontSize,
                        isTextWrapEnabled = textWrapState,
                        paddingValues = innerPadding
                    )
                is NoteResult.LoadedWithException ->
                    NoDataUiDescriptionBlock(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        description = noteState.message
                    )
                is NoteResult.NotFounded ->
                    NoDataUiDescriptionBlock(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        description = noteState.description
                    )
                NoteResult.NoteLoading ->
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