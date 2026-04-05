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
import androidx.compose.material3.Checkbox
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlin.math.roundToInt
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import com.example.notepad.R
import com.example.notepad.ui.components.TopUiBar
import com.example.notepad.ui.components.AlertUiMessageDialog
import com.example.notepad.ui.components.CheckBoxWithUiText
import com.example.notepad.ui.components.LoadingUiBlock
import com.example.notepad.ui.components.NoDataUiDescriptionBlock
import com.example.notepad.ui.screens.navigation.NavigationRoutes
import com.example.notepad.ui.states.NoteResult
import com.example.notepad.ui.view_models.NoteViewModel
import com.example.notepad.utils.ClipBoardManager
import com.example.notepad.utils.DateTimeFormatter

@Composable
private fun ScreenDropdownMenu(
    textWrapState: Boolean,
    currentFontSize: Int,
    updateCurrentFontSize: (Int) -> Unit,
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
                checked = textWrapState,
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
                    updateCurrentFontSize((value * 100).roundToInt())
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

@Composable
private fun NoteContentView(
    content: String,
    currentFontSize: Int,
    textWrapState: Boolean,
    paddingValues: PaddingValues
) {
    SelectionContainer {
        val verticalScrollState = rememberScrollState()
        if (textWrapState) {
            Text(
                text = content,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(verticalScrollState),
                fontSize = currentFontSize.sp
            )
        } else {
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
    onNavigateTo: (String) -> Unit,
    noteId: Long?,
    currentFontSize: Int,
    updateCurrentFontSize: (Int) -> Unit,
    textWrapState: Boolean,
    updateTextWrapStateMethod: (Boolean) -> Unit,
    noteViewModel: NoteViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val clipBoardManager = remember { ClipBoardManager(context) }
    val dateTimeFormatter = remember { DateTimeFormatter() }

    val currentNote by noteViewModel.currentNote.collectAsState()

    LaunchedEffect(Unit) {
        noteId?.let {
            noteViewModel.selectNote(it)
        }
    }

    Scaffold(
        topBar = {
            TopUiBar(
                titleContent = {
                    when (val noteState = currentNote) {
                        is NoteResult.SuccessfullyLoaded ->
                            Column {
                                Text(
                                    text = noteState.note.name,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.basicMarquee(Int.MAX_VALUE),
                                    fontSize = 16.sp
                                )

                                Row {
                                    Text(
                                        text = dateTimeFormatter.formatDatetimeNow(noteState.note.dateTime),
                                        fontWeight = FontWeight.Light,
                                        modifier = Modifier.basicMarquee(Int.MAX_VALUE),
                                        fontSize = 10.sp
                                    )

                                    Text(
                                        text = "${noteState.note.content.length} symbols",
                                        fontStyle = FontStyle.Italic,
                                        modifier = Modifier
                                            .padding(start = 10.dp)
                                            .basicMarquee(Int.MAX_VALUE),
                                        fontSize = 10.sp
                                    )
                                }
                            }
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
                            LoadingUiBlock(description = "Note loading...")
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
                    when (val noteState = currentNote) {
                        is NoteResult.SuccessfullyLoaded ->
                            ScreenDropdownMenu(
                                textWrapState = textWrapState,
                                currentFontSize = currentFontSize,
                                updateCurrentFontSize = updateCurrentFontSize,
                                onCopyNoteContent = {
                                    clipBoardManager.setTextToClipboard(noteState.note.content)
                                },
                                onUpdateTextWrapState = updateTextWrapStateMethod
                            )
                        else -> {}
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
                        textWrapState = textWrapState,
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
                NoteResult.NoteLoading -> LoadingUiBlock()
            }
        }
    )
}