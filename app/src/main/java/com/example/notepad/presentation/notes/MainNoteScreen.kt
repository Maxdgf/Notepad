package com.example.notepad.presentation.notes

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
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

import com.example.notepad.R
import com.example.notepad.domain.model.Note
import com.example.notepad.presentation.common.components.AlertMessageDialog
import com.example.notepad.presentation.common.components.DropdownMenuIconItem
import com.example.notepad.presentation.common.components.LoadingView
import com.example.notepad.presentation.common.components.NoDataDescriptionBlock
import com.example.notepad.presentation.common.components.NoteCard
import com.example.notepad.presentation.common.components.SearchPanelView
import com.example.notepad.presentation.common.components.SimpleFloatingIconButton
import com.example.notepad.presentation.common.components.TopAppBar
import com.example.notepad.presentation.common.utils.AppManager
import com.example.notepad.presentation.common.utils.DateTimeFormatter
import com.example.notepad.presentation.common.utils.Toaster
import com.example.notepad.presentation.navigation.NavigationRoutes

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
 * @param onPerformHaptic haptic feedback call function.
 * @param onNavigate call navigation to a specific screen function.
 */
@Suppress("ASSIGNED_VALUE_IS_NEVER_READ")
@Composable
private fun ScrollableNoteItemsList(
    modifier: Modifier = Modifier,
    context: Context,
    isGridViewEnabled: Boolean,
    isDisplayOrderNumEnabled: Boolean,
    isAlternatingNoteColorsEnabled: Boolean,
    notes: List<Note>,
    onPerformHaptic: (HapticFeedbackType) -> Unit,
    onNavigate: (String) -> Unit,
    onPerformEvent: (MainNoteEvent) -> Unit
) {
    val context = LocalContext.current
    val packageManager = context.packageManager

    val dateTimeFormatter = remember { DateTimeFormatter() }
    val toaster = remember { Toaster(context) }

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
                putExtra(Intent.EXTRA_TEXT, textToSend)
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
                items = notes,
                key = { _, note -> note.id }
            ) { index, note ->
                val column = index % CELLS_COUNT             // find column num by formula: k % C
                val row = index / CELLS_COUNT                // find row num by formula: k / C
                val isNoteCardDark = (column + row) % 2 == 0 // is sum even state

                NoteCard(
                    onClick = {
                        onPerformHaptic(HapticFeedbackType.LongPress) // haptic
                        onNavigate("${NavigationRoutes.NoteViewScreen.route}/${note.id}")
                    },
                    noteName = note.name,
                    noteOrderNum = if (isDisplayOrderNumEnabled) index + 1 else null,
                    noteDatetimeCreation = dateTimeFormatter.formatDatetimeNow(note.creationTime),
                    noteLastEditDatetime = note.lastEditTime?.let {
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
                        val shareIntent = Intent.createChooser(sendIntent, null) // create chooser

                        if (sendIntent.resolveActivity(packageManager) != null)
                            context.startActivity(shareIntent)
                        else
                            toaster.showToast("Unable to share note!")
                    },
                    useBrightBg =
                        if (isAlternatingNoteColorsEnabled) {
                            if (isNoteCardDark) false // dark bg
                            else true                 // light bg
                        } else false
                )
            }
        }
    } else {
        // normal list
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            itemsIndexed(
                items = notes,
                key = { _, note -> note.id }
            ) { index, note ->
                NoteCard(
                    onClick = {
                        onPerformHaptic(HapticFeedbackType.LongPress) // haptic
                        onNavigate("${NavigationRoutes.NoteViewScreen.route}/${note.id}")
                    },
                    noteName = note.name,
                    noteOrderNum = if (isDisplayOrderNumEnabled) index + 1 else null,
                    noteDatetimeCreation = dateTimeFormatter.formatDatetimeNow(note.creationTime),
                    noteLastEditDatetime = note.lastEditTime?.let {
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
                        val shareIntent = Intent.createChooser(sendIntent, null) // create chooser

                        if (sendIntent.resolveActivity(packageManager) != null)
                            context.startActivity(shareIntent)
                        else
                            toaster.showToast("Unable to share note!")
                    },
                    useBrightBg =
                        if (isAlternatingNoteColorsEnabled) {
                            // check is index even
                            if ((index + 1) % 2 == 0) true // light bg
                            else false                     // dark bg
                        } else false
                )
            }
        }
    }

    // delete note warn dialog
    AlertMessageDialog(
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
                        onPerformEvent(MainNoteEvent.DeleteMainNoteById(id))
                    }
                },
                modifier = Modifier.weight(0.5f),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onError)
            ) { Text(text = "Delete") }
        }
    }
}

// developer public email address
private const val DEVELOPER_EMAIL_ADDRESS = "maxma4090@gmail.com"

/** Creates a main app screen.
 * @param onNavigateTo function for navigate to specific screen */
@Suppress("ASSIGNED_VALUE_IS_NEVER_READ")
@Composable
fun MainAppScreen(
    onNavigateTo: (String) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    val activity = LocalActivity.current
    val packageManager = context.packageManager

    val appManager = remember { AppManager(activity) }
    val toaster = remember { Toaster(context) }

    val mainNoteViewModel: MainNoteViewModel = hiltViewModel()
    val settingsViewModel: NoteDisplaySettingsViewModel = hiltViewModel()

    /**
     * Configures intent for send feedback about app by email.
     * @return intent object.
     */
    val sendFeedbackViaEmailIntent: (String) -> Intent = remember {
        { subject ->
            // only email apps
            Intent(Intent.ACTION_SENDTO).apply {
                data = "mailto:$DEVELOPER_EMAIL_ADDRESS?subject=$subject".toUri()
            }
        }
    }

    var deleteAllNotesAlertMessageDialogState by rememberSaveable { mutableStateOf(false) }
    var searchViewState by rememberSaveable { mutableStateOf(false) }

    val allNotesList by mainNoteViewModel.a.collectAsState() //noteList
    val foundedNotesBySearchQuery by mainNoteViewModel.noteListBySearchQuery.collectAsState()
    val notesDisplaySettings by settingsViewModel.notesDisplaySettings.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                titleContent = {
                    if (!searchViewState)
                        Text(text = "Notepad")
                },
                barActionElements = {
                    var dropdownMenuState by remember { mutableStateOf(false) }
                    val searchQuery by mainNoteViewModel.searchQuery.collectAsState()

                    // search view
                    SearchPanelView(
                        modifier = Modifier.fillMaxWidth(),
                        state = searchViewState,
                        onDismissRequest = {
                            searchViewState = false
                            mainNoteViewModel.updateSearchQuery("") // clear query
                        },
                        query = searchQuery,
                        onUpdateQuery = { query ->
                            mainNoteViewModel.updateSearchQuery(query)
                        },
                        onClearQuery = {
                            mainNoteViewModel.updateSearchQuery("") // clear query
                        }
                    )

                    // check search note state
                    if (!searchViewState) {
                        IconButton(onClick = { searchViewState = true }) {
                            Icon(
                                painter = painterResource(R.drawable.outline_search_24),
                                contentDescription = null
                            )
                        }

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
                                DropdownMenuIconItem(
                                    onClick = {
                                        dropdownMenuState = false // hide menu
                                        onNavigateTo(NavigationRoutes.NoteSettingsScreen.route)
                                    },
                                    iconPainter = painterResource(R.drawable.baseline_settings_24),
                                    text = "settings",
                                    contentDescription = null,
                                )

                                // delete all notes button
                                when (val notesListState = allNotesList) {
                                    is NotesListResult.ContentList ->
                                        DropdownMenuIconItem(
                                            onClick = {
                                                dropdownMenuState = false // hide menu
                                                if (notesListState.noteList.isNotEmpty())
                                                    deleteAllNotesAlertMessageDialogState = true
                                            },
                                            iconPainter = painterResource(R.drawable.baseline_delete_24),
                                            text = "delete all",
                                            contentDescription = null
                                        )
                                    else -> {} // nothing show
                                }

                                HorizontalDivider()

                                DropdownMenuIconItem(
                                    onClick = {
                                        val sendFeedbackIntent = sendFeedbackViaEmailIntent("Notepad app feedback")

                                        if (sendFeedbackIntent.resolveActivity(packageManager) != null)
                                            context.startActivity(sendFeedbackIntent)
                                        else
                                            toaster.showToast("No email apps!")
                                    },
                                    iconPainter = painterResource(R.drawable.baseline_email_24),
                                    text = "send feedback",
                                    contentDescription = null
                                )

                                DropdownMenuIconItem(
                                    onClick = {
                                        dropdownMenuState = false // hide menu
                                        appManager.breakApp()     // exit app
                                    },
                                    text = "exit",
                                    iconPainter = painterResource(R.drawable.baseline_exit_to_app_24),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            SimpleFloatingIconButton(
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
                when (val notesListState = allNotesList) {
                    is NotesListResult.ContentList -> {
                        if (!searchViewState) {
                            ScrollableNoteItemsList(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 5.dp),
                                context = context,
                                isGridViewEnabled = notesDisplaySettings.isGridEnabled,
                                notes = notesListState.noteList,
                                onPerformHaptic = haptic::performHapticFeedback,
                                onNavigate = onNavigateTo,
                                isDisplayOrderNumEnabled = notesDisplaySettings.isOrderNumEnabled,
                                isAlternatingNoteColorsEnabled = notesDisplaySettings.isZebraNoteColorsEnabled,
                                onPerformEvent = mainNoteViewModel::performEvent
                            )
                        } else {
                            when (val foundNotes = foundedNotesBySearchQuery) {
                                is NoteSearchResult.Found -> {
                                    ScrollableNoteItemsList(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 5.dp),
                                        context = context,
                                        isGridViewEnabled = notesDisplaySettings.isGridEnabled,
                                        notes = foundNotes.notes,
                                        onPerformHaptic = haptic::performHapticFeedback,
                                        onNavigate = onNavigateTo,
                                        isDisplayOrderNumEnabled = notesDisplaySettings.isOrderNumEnabled,
                                        isAlternatingNoteColorsEnabled = notesDisplaySettings.isZebraNoteColorsEnabled,
                                        onPerformEvent = mainNoteViewModel::performEvent
                                    )
                                }
                                is NoteSearchResult.NotFound -> {
                                    NoDataDescriptionBlock(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(innerPadding),
                                        description = "No notes found."
                                    )
                                }
                                is NoteSearchResult.Searching -> {
                                    LoadingView(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(innerPadding),
                                        description = "Searching notes..."
                                    ) // show loading block
                                }
                            }
                        }
                    }
                    is NotesListResult.Exception -> {
                        NoDataDescriptionBlock(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            description = notesListState.message
                        )
                    }
                    NotesListResult.Loading -> {
                        LoadingView(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            description = "Loading notes..."
                        ) // show loading block
                    }
                    NotesListResult.EmptyList -> {
                        NoDataDescriptionBlock(
                            description = "No notes :(",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        )
                    }
                }
            }

            // delete all notes warning dialog
            AlertMessageDialog(
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
                            mainNoteViewModel.performEvent(MainNoteEvent.DeleteAllNotes) // delete all notes
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
