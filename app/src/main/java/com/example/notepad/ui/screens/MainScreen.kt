package com.example.notepad.ui.screens

import android.content.Context
import android.content.Intent
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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

import com.example.notepad.R
import com.example.notepad.core.data_management.databases.notes_local_storage.entities.NoteEntity
import com.example.notepad.ui.components.SimpleFloatingUiIconButton
import com.example.notepad.ui.components.TopUiBar
import com.example.notepad.ui.components.AlertUiMessageDialog
import com.example.notepad.ui.components.LoadingUiBlock
import com.example.notepad.ui.components.NoDataUiDescriptionBlock
import com.example.notepad.ui.components.NoteUiCard
import com.example.notepad.ui.screens.navigation.NavigationRoutes
import com.example.notepad.ui.states.NotesListResult
import com.example.notepad.utils.AppManager
import com.example.notepad.utils.DateTimeFormatter

// lazy vertical grid cells count
private const val CELLS_COUNT = 2

/**
 * Creates a scrollable note items list.
 *
 * @param modifier component modifier.
 * @param context local context.
 * @param isGridViewEnabled notes grid display mode flag.
 * @param isDisplayOrderNumEnabled note order num display mode flag.
 * @param isAlternatingNoteColorsEnabled display alternating note colors flag.
 * @param allNotes notes list.
 * @param onPerformHaptic haptic feedback call function.
 * @param onNavigate call navigation to a specific screen function.
 * @param onDeleteNoteById delete note by id function.
 */
@Composable
private fun ScrollableNoteItemsList(
    modifier: Modifier = Modifier,
    context: Context,
    isGridViewEnabled: Boolean,
    isDisplayOrderNumEnabled: Boolean,
    isAlternatingNoteColorsEnabled: Boolean,
    allNotes: List<NoteEntity>,
    onPerformHaptic: (HapticFeedbackType) -> Unit,
    onNavigate: (String) -> Unit,
    onDeleteNoteById: (Long) -> Unit
) {
    val dateTimeFormatter = remember { DateTimeFormatter() }

    // check notes list(is empty or not)
    if (allNotes.isNotEmpty()) {
        var deleteNoteAlertMessageDialogState by rememberSaveable { mutableStateOf(false) }
        var selectedNoteIdToEdit: Long? by rememberSaveable { mutableStateOf(null) }

        /**
         * Configures intent for send note text.
         * @return intent object.
         */
        val sendNoteIntent: (String) -> Intent = remember {
            { textToSend ->
                Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        textToSend
                    )
                    type = "text/plain"
                }
            }
        }

        if (isGridViewEnabled) {
            // grid list
            LazyVerticalGrid(
                columns = GridCells.Fixed(CELLS_COUNT), // 2 cells
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                itemsIndexed(
                    items = allNotes,
                    key = { index, note -> note.id }
                ) { index, note ->
                    val column = index % CELLS_COUNT // find column num by formula: k % C
                    val row = index / CELLS_COUNT // find row num by formula: k / C
                    val isNoteCardDark = (column + row) % 2 == 0 // is sum even state

                    NoteUiCard(
                        onClick = {
                            onPerformHaptic(HapticFeedbackType.LongPress) // haptic
                            onNavigate("${NavigationRoutes.NoteViewScreen.route}/${note.id}")
                        },
                        noteName = note.name,
                        noteOrderNum = if (isDisplayOrderNumEnabled) index + 1 else null,
                        noteDatetimeCreation = dateTimeFormatter.formatDatetimeNow(note.dateTime),
                        noteLastEditDatetime = note.lastEditDateTime?.let {
                            dateTimeFormatter.formatDatetimeNow(it)
                        },
                        onEdit = {
                            onPerformHaptic(HapticFeedbackType.LongPress) // haptic
                            onNavigate("${NavigationRoutes.NoteEditScreen.route}/${note.id}")
                        },
                        onDelete = {
                            selectedNoteIdToEdit = note.id
                            deleteNoteAlertMessageDialogState = true
                        },
                        onShare = {
                            // configure send intent
                            val sendIntent = sendNoteIntent(note.name + "\n\n" + note.content)
                            // create chooser
                            val shareIntent = Intent.createChooser(sendIntent, null)
                            context.startActivity(shareIntent)
                        },
                        useBrightBg =
                            if (isAlternatingNoteColorsEnabled)
                                if (isNoteCardDark) false // dark bg
                                else true // light bg
                            else false
                    )
                }
            }
        } else {
            // normal list
            LazyColumn(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                itemsIndexed(
                    items = allNotes,
                    key = { index, note -> note.id }
                ) { index, note ->
                    NoteUiCard(
                        onClick = {
                            onPerformHaptic(HapticFeedbackType.LongPress) // haptic
                            onNavigate("${NavigationRoutes.NoteViewScreen.route}/${note.id}")
                        },
                        noteName = note.name,
                        noteOrderNum = if (isDisplayOrderNumEnabled) index + 1 else null,
                        noteDatetimeCreation = dateTimeFormatter.formatDatetimeNow(note.dateTime),
                        noteLastEditDatetime = note.lastEditDateTime?.let {
                            dateTimeFormatter.formatDatetimeNow(it)
                        },
                        onEdit = {
                            onPerformHaptic(HapticFeedbackType.LongPress) // haptic
                            onNavigate("${NavigationRoutes.NoteEditScreen.route}/${note.id}")
                        },
                        onDelete = {
                            selectedNoteIdToEdit = note.id
                            deleteNoteAlertMessageDialogState = true
                        },
                        onShare = {
                            // configure send intent
                            val sendIntent = sendNoteIntent(note.name + "\n\n" + note.content)
                            // create chooser
                            val shareIntent = Intent.createChooser(sendIntent, null)
                            context.startActivity(shareIntent)
                        },
                        useBrightBg =
                            if (isAlternatingNoteColorsEnabled)
                                // check is index even
                                if ((index + 1) % 2 == 0) true // light bg
                                else false // dark bg
                            else false
                    )
                }
            }
        }

        // delete note warn dialog
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
                            onDeleteNoteById(id)
                        }
                    },
                    modifier = Modifier.weight(0.5f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onError)
                ) { Text(text = "Delete") }
            }
        }
    } else {
        NoDataUiDescriptionBlock(
            description = "No notes :(",
            modifier = Modifier.fillMaxSize()
        )
    }
}

/**
 * Creates a main app screen.
 *
 * @param isGridViewEnabled notes grid display mode flag.
 * @param isDisplayOrderNumEnabled note order num display mode flag.
 * @param isAlternatingNoteColorsEnabled display alternating note colors flag.
 * @param notesList notes list.
 * @param onDeleteAllNotes delete all notes function.
 * @param onDeleteNoteById delete note by id function.
 */
@Composable
fun MainUiScreen(
    onNavigateTo: (String) -> Unit,
    isGridViewEnabled: Boolean,
    isDisplayOrderNumEnabled: Boolean,
    isAlternatingNoteColorsEnabled: Boolean,
    notesList: NotesListResult,
    onDeleteAllNotes: () -> Unit,
    onDeleteNoteById: (Long) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    val activity = LocalActivity.current

    val appManager = remember { AppManager(activity) }
    var deleteAllNotesAlertMessageDialogState by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopUiBar(
                titleContent = { Text(text = "Notepad") },
                barActionElements = {
                    var dropdownMenuState by rememberSaveable { mutableStateOf(false) }

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
                                    onNavigateTo(NavigationRoutes.NoteSettingsScreen.route)
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
                    onNavigateTo(NavigationRoutes.NoteCreationScreen.route)
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
                // match notes list state
                when (val notesListState = notesList) {
                    is NotesListResult.SuccessfullyLoaded ->
                        ScrollableNoteItemsList(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 5.dp),
                            context = context,
                            isGridViewEnabled = isGridViewEnabled,
                            allNotes = notesListState.noteList,
                            onPerformHaptic = haptic::performHapticFeedback,
                            onNavigate = onNavigateTo,
                            isDisplayOrderNumEnabled = isDisplayOrderNumEnabled,
                            isAlternatingNoteColorsEnabled = isAlternatingNoteColorsEnabled,
                            onDeleteNoteById = onDeleteNoteById
                        )
                    is NotesListResult.LoadedWithException ->
                        NoDataUiDescriptionBlock(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            description = notesListState.message
                        )
                    NotesListResult.Loading -> LoadingUiBlock() // show loading block
                }
            }

            // delete all notes warning dialog
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
                            onDeleteAllNotes() // delete all notes
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