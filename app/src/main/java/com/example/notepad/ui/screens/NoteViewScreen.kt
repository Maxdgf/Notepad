package com.example.notepad.ui.screens

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.notepad.core.data_management.databases.notes_local_storage.NoteEntity
import com.example.notepad.ui.components.screen_components.TopUiBar
import com.example.notepad.ui.navigation.NavigationRoutes
import com.example.notepad.ui.navigation.Navigator

@Composable
fun NoteUiViewScreen(
    navigator: Navigator,
    currentNote: NoteEntity?
) {
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
                    IconButton(onClick = {
                        navigator.navigateTo(NavigationRoutes.MainScreen.route)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Return to main screen icon button."
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            currentNote?.let {
                Text(
                    text = it.content,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(noteViewVerticalScrollState)
                )
            }
        }
    )
}