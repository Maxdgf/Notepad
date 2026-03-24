package com.example.notepad.ui.screens

import android.content.Intent
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

import com.example.notepad.R
import com.example.notepad.ui.components.SimpleFloatingUiIconButton
import com.example.notepad.ui.components.TopUiBar
import com.example.notepad.ui.components.AlertUiMessageDialog
import com.example.notepad.ui.components.NoDataUiDescriptionBlock
import com.example.notepad.ui.components.NoteUiCard
import com.example.notepad.ui.components.ScrollableUiItemsList
import com.example.notepad.ui.navigation.NavigationRoutes
import com.example.notepad.ui.navigation.Navigator
import com.example.notepad.ui.view_models.NoteViewModel
import com.example.notepad.utils.AppManager

/**
 * Creates a main app screen.
 * @param navigator navigation utility.
 * @param isGridEnabledState notes grid enabled state.
 */
@Composable
fun MainUiScreen(
    navigator: Navigator,
    isGridEnabledState: Boolean,
    noteViewModel: NoteViewModel = hiltViewModel()
) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    val activity = LocalActivity.current

    val appManager = remember { AppManager(activity) }

    var deleteAllNotesAlertMessageDialogState by rememberSaveable { mutableStateOf(false) }
    var deleteNoteAlertMessageDialogState by rememberSaveable { mutableStateOf(false) }
    var dropdownMenuState by rememberSaveable { mutableStateOf(false) }
    var selectedNoteIdToEdit: Long? by rememberSaveable { mutableStateOf(null) }

    val notesList by noteViewModel.noteList.collectAsState()
    val isNotesLoadingState by noteViewModel.isNotesLoadingState.collectAsState()

    Scaffold(
        topBar = {
            TopUiBar(
                titleContent = { Text(text = "Notepad") },
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
                                    navigator.navigateTo(NavigationRoutes.NoteSettingsScreen.route)
                                },
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.baseline_settings_24),
                                            contentDescription = null
                                        )
                                        Text(text = "settings")
                                    }
                                }
                            )

                            DropdownMenuItem(
                                onClick = {
                                    dropdownMenuState = false // hide menu
                                    deleteAllNotesAlertMessageDialogState = true
                                },
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.baseline_delete_24),
                                            contentDescription = null
                                        )
                                        Text(text = "delete all")
                                    }
                                }
                            )

                            HorizontalDivider()

                            DropdownMenuItem(
                                onClick = {
                                    dropdownMenuState = false // hide menu
                                    appManager.breakApp() // exit app
                                },
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.baseline_exit_to_app_24),
                                            contentDescription = null
                                        )
                                        Text(text = "exit")
                                    }
                                }
                            )
                        }
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            SimpleFloatingUiIconButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    navigator.navigateTo(NavigationRoutes.NoteCreationScreen.route)
                },
                icon = painterResource(R.drawable.outline_add_24),
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
                    // check notes list is not empty
                    if (notesList.isNotEmpty())
                        ScrollableUiItemsList(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 5.dp),
                            isGridEnabled = isGridEnabledState,
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
                                    key = { note -> note.id }
                                ) { note ->
                                    NoteUiCard(
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress) // haptic
                                            navigator.navigateTo("${NavigationRoutes.NoteViewScreen.route}/${note.id}")
                                        },
                                        noteName = note.name,
                                        noteDatetimeCreation = note.dateTime,
                                        noteLastEditDatetime = note.lastEditDateTime,
                                        onEdit = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress) // haptic
                                            navigator.navigateTo("${NavigationRoutes.NoteEditScreen.route}/${note.id}")
                                        },
                                        onDelete = {
                                            selectedNoteIdToEdit = note.id
                                            deleteNoteAlertMessageDialogState = true
                                        },
                                        onShare = {
                                            // configure send intent
                                            val sendIntent = Intent().apply {
                                                action = Intent.ACTION_SEND
                                                putExtra(
                                                    Intent.EXTRA_TEXT,
                                                    note.name + "\n" +
                                                            note.dateTime + "\n\n" +
                                                            note.content
                                                )
                                                type = "text/plain"
                                            }

                                            // create chooser
                                            val shareIntent = Intent.createChooser(sendIntent, null)
                                            context.startActivity(shareIntent)
                                        }
                                    )
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
                                    key = { note -> note.id }
                                ) { note ->
                                    NoteUiCard(
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            navigator.navigateTo("${NavigationRoutes.NoteViewScreen.route}/${note.id}")
                                        },
                                        noteName = note.name,
                                        noteDatetimeCreation = note.dateTime,
                                        noteLastEditDatetime = note.lastEditDateTime,
                                        onEdit = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress) // haptic
                                            navigator.navigateTo("${NavigationRoutes.NoteEditScreen.route}/${note.id}")
                                        },
                                        onDelete = {
                                            selectedNoteIdToEdit = note.id
                                            deleteNoteAlertMessageDialogState = true
                                        },
                                        onShare = {
                                            // configure send intent
                                            val sendIntent = Intent().apply {
                                                action = Intent.ACTION_SEND
                                                putExtra(
                                                    Intent.EXTRA_TEXT,
                                                    note.name + "\n" +
                                                            note.dateTime + "\n\n" +
                                                            note.content
                                                )
                                                type = "text/plain"
                                            }

                                            // create chooser
                                            val shareIntent = Intent.createChooser(sendIntent, null)
                                            context.startActivity(shareIntent)
                                        }
                                    )
                                }
                            }
                        )
                    else
                        // show no-data description
                        NoDataUiDescriptionBlock(
                            description = "No notes :(",
                            modifier = Modifier.fillMaxSize()
                        )
                }
            }

            AlertUiMessageDialog(
                onDismissRequestFunction = { deleteAllNotesAlertMessageDialogState = false },
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = Color.White,
                state = deleteAllNotesAlertMessageDialogState,
                titleIcon = painterResource(R.drawable.outline_warning_amber_24),
                titleText = "Warning"
            ) {
                Text(text = "Are you sure you want to delete all notes?")

                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { deleteAllNotesAlertMessageDialogState = false },
                        modifier = Modifier.weight(0.5f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onError)
                    ) { Text(text = "Cancel") }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = {
                            deleteAllNotesAlertMessageDialogState = false
                            noteViewModel.deleteAllNotes()
                        },
                        modifier = Modifier.weight(0.5f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onError)
                    ) { Text(text = "Delete") }
                }
            }

            AlertUiMessageDialog(
                onDismissRequestFunction = { deleteNoteAlertMessageDialogState = false },
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = Color.White,
                state = deleteNoteAlertMessageDialogState,
                titleIcon = painterResource(R.drawable.outline_warning_amber_24),
                titleText = "Warning"
            ) {
                Text(text = "Are you sure you want to delete this note?")

                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { deleteNoteAlertMessageDialogState = false },
                        modifier = Modifier.weight(0.5f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onError)
                    ) { Text(text = "Cancel") }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = {
                            deleteNoteAlertMessageDialogState = false

                            selectedNoteIdToEdit?.let { id ->
                                noteViewModel.deleteNote(id)
                            }
                        },
                        modifier = Modifier.weight(0.5f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onError)
                    ) { Text(text = "Delete") }
                }
            }
        }
    )
}