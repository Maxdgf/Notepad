package com.example.notepad.ui.screens

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.notepad.R

import com.example.notepad.core.data_management.databases.notes_local_storage.NoteEntity
import com.example.notepad.ui.components.screen_components.SimpleFloatingUiIconButton
import com.example.notepad.ui.components.screen_components.TopUiBar
import com.example.notepad.ui.components.ui_components.AlertUiMessageDialog
import com.example.notepad.ui.components.ui_components.BottomUiSheetActionDialog
import com.example.notepad.ui.components.ui_components.CardUiItem
import com.example.notepad.ui.components.ui_components.ScrollableUiItemsList
import com.example.notepad.ui.navigation.NavigationRoutes
import com.example.notepad.ui.navigation.Navigator
import com.example.notepad.ui.utils.CurrentThemeColor

@Composable
fun MainUiScreen(
    navigator: Navigator,
    currentThemeColor: CurrentThemeColor,
    noteActionsDialogState: Boolean,
    isNotesLoadingState: Boolean,
    notesList: List<NoteEntity>,
    isGridEnabledState: Boolean,
    selectedNoteUuidState: String?,
    deleteAllNotesAlertMessageDialogState: Boolean,
    updateDeleteAllNotesAlertMessageDialogStateMethod: (state: Boolean) -> Unit,
    updateSelectedNoteUuidStateMethod: (uuid: String?) -> Unit,
    updateNoteActionsDialogStateMethod: (state: Boolean) -> Unit,
    updateNoteNameStateMethod: (newValue: String) -> Unit,
    updateNoteContentStateMethod: (newValue: String) -> Unit,
    deleteNoteMethod: (uuid: String) -> Unit,
    deleteAllNotesMethod: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            TopUiBar(
                titleContent = { Text(text = "Notepad") },
                barActionElements = {
                    IconButton(onClick = { updateDeleteAllNotesAlertMessageDialogStateMethod(true) }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Delete all notes icon button."
                        )
                    }

                    IconButton(onClick = { navigator.navigateTo(NavigationRoutes.NoteSettingsScreen.route) }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_settings_24),
                            contentDescription = null
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            SimpleFloatingUiIconButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    updateNoteNameStateMethod("")
                    updateNoteContentStateMethod("")
                    navigator.navigateTo(NavigationRoutes.NoteCreationScreen.route)
                },
                imageVector = Icons.Default.Add,
                iconDescription = "Icon of simple icon add action floating button.",
                buttonShape = FloatingActionButtonDefaults.shape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                if (isNotesLoadingState) {
                    Row {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterVertically))
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Loading...",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                } else {
                    ScrollableUiItemsList(
                        //paddingValues = innerPadding,
                        contentPaddingValues = PaddingValues(
                            start = 5.dp,
                            end = 5.dp
                        ),
                        isGridEnabled = isGridEnabledState,
                        verticalArrangementValue = Arrangement.spacedBy(10.dp),
                        scrollableContent = {
                            item {
                                Text(
                                    text = "notes count: ${notesList.size}", //test
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                HorizontalDivider(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            items(
                                items = notesList,
                                key = { note -> note.uuid }
                            ) { note ->
                                CardUiItem(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .combinedClickable(
                                            onClick = {
                                                updateSelectedNoteUuidStateMethod(note.uuid)

                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                navigator.navigateTo(NavigationRoutes.NoteViewScreen.route)
                                            },
                                            onLongClick = {
                                                updateNoteActionsDialogStateMethod(true)
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                                                updateSelectedNoteUuidStateMethod(note.uuid)
                                            }
                                        )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 5.dp)
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = note.name,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.basicMarquee(Int.MAX_VALUE)
                                            )

                                            Text(
                                                text = note.dateTime,
                                                fontWeight = FontWeight.Light,
                                                fontSize = 10.sp,
                                                modifier = Modifier.basicMarquee(Int.MAX_VALUE)
                                            )

                                            note.lastEditDateTime?.let { dateTime ->
                                                HorizontalDivider(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    color = currentThemeColor.getAdaptedCurrentThemeColor(
                                                        false
                                                    )
                                                )

                                                Row {
                                                    Icon(
                                                        imageVector = Icons.Default.Edit,
                                                        contentDescription = "Last note edit decoration icon."
                                                    )

                                                    Text(
                                                        text = "last edit:",
                                                        fontStyle = FontStyle.Italic,
                                                        fontSize = 10.sp,
                                                        modifier = Modifier
                                                            .padding(start = 3.dp)
                                                            .basicMarquee(Int.MAX_VALUE)
                                                    )

                                                    Text(
                                                        text = dateTime,
                                                        fontWeight = FontWeight.Light,
                                                        fontSize = 10.sp,
                                                        modifier = Modifier
                                                            .padding(start = 5.dp)
                                                            .basicMarquee(Int.MAX_VALUE)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        scrollableGridContent = {
                            item(span = { GridItemSpan(2) }) {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    Text(
                                        text = "notes count: ${notesList.size}", //test
                                        fontSize = 20.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    HorizontalDivider(
                                        modifier = Modifier.fillMaxWidth(),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            items(
                                items = notesList,
                                key = { note -> note.uuid }
                            ) { note ->
                                CardUiItem(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 5.dp)
                                        .combinedClickable(
                                            onClick = {
                                                updateSelectedNoteUuidStateMethod(note.uuid)

                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                navigator.navigateTo(NavigationRoutes.NoteViewScreen.route)
                                            },
                                            onLongClick = {
                                                updateNoteActionsDialogStateMethod(true)
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                                                updateSelectedNoteUuidStateMethod(note.uuid)
                                            }
                                        )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 5.dp)
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = note.name,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.basicMarquee(Int.MAX_VALUE)
                                            )

                                            Text(
                                                text = note.dateTime,
                                                fontWeight = FontWeight.Light,
                                                fontSize = 10.sp,
                                                modifier = Modifier.basicMarquee(Int.MAX_VALUE)
                                            )

                                            note.lastEditDateTime?.let { dateTime ->
                                                HorizontalDivider(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    color = currentThemeColor.getAdaptedCurrentThemeColor(
                                                        false
                                                    )
                                                )

                                                Row {
                                                    Icon(
                                                        imageVector = Icons.Default.Edit,
                                                        contentDescription = "Last note edit decoration icon."
                                                    )

                                                    Text(
                                                        text = "last edit:",
                                                        fontStyle = FontStyle.Italic,
                                                        fontSize = 10.sp,
                                                        modifier = Modifier
                                                            .padding(start = 3.dp)
                                                            .basicMarquee(Int.MAX_VALUE)
                                                    )

                                                    Text(
                                                        text = dateTime,
                                                        fontWeight = FontWeight.Light,
                                                        fontSize = 10.sp,
                                                        modifier = Modifier
                                                            .padding(start = 5.dp)
                                                            .basicMarquee(Int.MAX_VALUE)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
            }

            AlertUiMessageDialog(
                onDismissRequestFunction = { updateDeleteAllNotesAlertMessageDialogStateMethod(false) },
                color = MaterialTheme.colorScheme.error,
                state = deleteAllNotesAlertMessageDialogState
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "Are you sure you want to delete all notes?",
                        color = Color.White
                    )

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { updateDeleteAllNotesAlertMessageDialogStateMethod(false) },
                            modifier = Modifier.weight(0.5f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onError)
                        ) {
                            Text(
                                text = "Cancel",
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Button(
                            onClick = {
                                updateDeleteAllNotesAlertMessageDialogStateMethod(false)
                                deleteAllNotesMethod()
                            },
                            modifier = Modifier.weight(0.5f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onError)
                        ) {
                            Text(
                                text = "Delete",
                                color = Color.White
                            )
                        }
                    }
                }
            }

            BottomUiSheetActionDialog(
                state = noteActionsDialogState,
                onDismissRequestFunction = { updateNoteActionsDialogStateMethod(false) },
                isExpanded = false
            ) {
                val currentNote = notesList.find { note -> selectedNoteUuidState == note.uuid }

                currentNote?.let { note ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                        ) {
                            Text(text = "Actions with note:")
                            Text(
                                text = note.name,
                                modifier = Modifier
                                    .weight(1f)
                                    .basicMarquee(Int.MAX_VALUE),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            Button(
                                onClick = {
                                    updateNoteActionsDialogStateMethod(false)

                                    updateSelectedNoteUuidStateMethod(note.uuid)
                                    updateNoteNameStateMethod(note.name)
                                    updateNoteContentStateMethod(note.content)

                                    navigator.navigateTo(NavigationRoutes.NoteEditScreen.route)
                                },
                                modifier = Modifier.weight(0.5f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = "Edit note button."
                                )
                            }

                            Button(
                                onClick = {
                                    deleteNoteMethod(note.uuid)
                                    updateNoteActionsDialogStateMethod(false)
                                },
                                modifier = Modifier.weight(0.5f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = MaterialTheme.colorScheme.onError
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = "Delete note button."
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}