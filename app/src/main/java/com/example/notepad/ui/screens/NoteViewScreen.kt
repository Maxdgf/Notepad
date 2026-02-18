package com.example.notepad.ui.screens

import androidx.compose.foundation.basicMarquee
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

import com.example.notepad.R
import com.example.notepad.core.data_management.databases.notes_local_storage.entities.NoteEntity
import com.example.notepad.ui.components.screen_components.TopUiBar
import com.example.notepad.ui.components.ui_components.AlertUiMessageDialog
import com.example.notepad.ui.navigation.NavigationRoutes
import com.example.notepad.ui.navigation.Navigator
import com.example.notepad.utils.ClipBoardManager

@Composable
fun NoteUiViewScreen(
    navigator: Navigator,
    currentNote: NoteEntity?,
    currentFontSize: Int,
    updateCurrentFontSize: (Int) -> Unit,
    changeFontSizeDialogState: Boolean,
    updateChangeFontSizeDialogStateMethod: (Boolean) -> Unit,
    clipBoardManager: ClipBoardManager
) {
    val haptic = LocalHapticFeedback.current
    val noteViewVerticalScrollState = rememberScrollState()

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
                    IconButton(
                        onClick = {
                            currentNote?.content?.let {
                                clipBoardManager.setTextToClipboard(it)
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_content_copy_24),
                            contentDescription = null
                        )
                    }

                    IconButton(onClick = { updateChangeFontSizeDialogStateMethod(true) }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_text_format_24),
                            contentDescription = null
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            AlertUiMessageDialog(
                state = changeFontSizeDialogState,
                onDismissRequestFunction = { updateChangeFontSizeDialogStateMethod(false) },
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
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress) // haptic
                    }
                )

                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = { updateChangeFontSizeDialogStateMethod(false) },
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
                    Text(
                        text = it.content,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .verticalScroll(noteViewVerticalScrollState),
                        fontSize = currentFontSize.sp
                    )
                }
            }
        }
    )
}