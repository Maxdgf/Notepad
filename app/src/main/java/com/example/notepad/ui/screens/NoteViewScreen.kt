package com.example.notepad.ui.screens

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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

import com.example.notepad.R
import com.example.notepad.ui.components.TopUiBar
import com.example.notepad.ui.components.AlertUiMessageDialog
import com.example.notepad.ui.navigation.NavigationRoutes
import com.example.notepad.ui.navigation.Navigator
import com.example.notepad.ui.view_models.NoteViewModel
import com.example.notepad.utils.ClipBoardManager

/**Creates a note view app screen.*/
@Composable
fun NoteUiViewScreen(
    navigator: Navigator,
    noteId: Long?,
    currentFontSize: Int,
    updateCurrentFontSize: (Int) -> Unit,
    textWrapState: Boolean,
    updateTextWrapStateMethod: (Boolean) -> Unit,
    noteViewModel: NoteViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val clipBoardManager = remember { ClipBoardManager(context) }
    val noteViewVerticalScrollState = rememberScrollState()

    var dropdownMenuState by rememberSaveable { mutableStateOf(false) }
    var fontSizeDialogState by rememberSaveable { mutableStateOf(false) }

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
                    currentNote?.let {
                        Column {
                            Text(
                                text = it.name,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.basicMarquee(Int.MAX_VALUE),
                                fontSize = 16.sp
                            )

                            Row {
                                Text(
                                    text = it.dateTime,
                                    fontWeight = FontWeight.Light,
                                    modifier = Modifier.basicMarquee(Int.MAX_VALUE),
                                    fontSize = 10.sp
                                )

                                Text(
                                    text = "${it.content.length} symbols",
                                    fontStyle = FontStyle.Italic,
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .basicMarquee(Int.MAX_VALUE),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                },
                barIcon = {
                    IconButton(onClick = { navigator.navigateTo(NavigationRoutes.MainScreen.route) }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_arrow_back_24),
                            contentDescription = null
                        )
                    }
                },
                barActionElements = {
                    // dropdown menu
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
                                    currentNote?.content?.let {
                                        clipBoardManager.setTextToClipboard(it)
                                    }
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

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.padding(horizontal = 5.dp)
                            ) {
                                Checkbox(
                                    checked = textWrapState,
                                    onCheckedChange = { state ->
                                        updateTextWrapStateMethod(state)
                                        dropdownMenuState = false // hide menu
                                    }
                                )

                                Text(text = "text wrap")
                            }
                        }
                    }
                }
            )
        },
        content = { innerPadding ->
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

            currentNote?.let {
                SelectionContainer {
                    if (textWrapState) {
                        Text(
                            text = it.content,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .verticalScroll(noteViewVerticalScrollState),
                            fontSize = currentFontSize.sp
                        )
                    } else {
                        val horizontalScrollState = rememberScrollState()
                        Text(
                            text = it.content,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .horizontalScroll(horizontalScrollState)
                                .verticalScroll(noteViewVerticalScrollState),
                            fontSize = currentFontSize.sp
                        )
                    }
                }
            }
        }
    )
}