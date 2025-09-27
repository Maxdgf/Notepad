package com.example.notepad.ui.screens

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.horizontalScroll
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
import androidx.navigation.NavController

import com.example.notepad.core.data_models.SelectedNote
import com.example.notepad.ui.components.screen_components.TopUiBar

@Composable
fun NoteUiViewScreen(
    navigationController: NavController,
    selectedNoteState: SelectedNote,
    //clearSelectedNoteStateMethod: () -> Unit
) {
    val noteViewVerticalScrollState = rememberScrollState()
    val noteViewHorizontalScrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopUiBar(
                titleContent = {
                    Column {
                        Text(
                            text = selectedNoteState.name,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.basicMarquee(Int.MAX_VALUE),
                            fontSize = 16.sp
                        )

                        Row {
                            Text(
                                text = selectedNoteState.creationDate,
                                fontWeight = FontWeight.Light,
                                modifier = Modifier.basicMarquee(Int.MAX_VALUE),
                                fontSize = 10.sp
                            )

                            Text(
                                text = "${selectedNoteState.content.length} symbols",
                                fontStyle = FontStyle.Italic,
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .basicMarquee(Int.MAX_VALUE),
                                fontSize = 10.sp
                            )
                        }
                    }
                },
                barIcon = {
                    IconButton(onClick = {
                        navigationController.navigate(NavigationRoutes.MainScreen.route)
                        //clearSelectedNoteStateMethod()
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
            Text(
                text = selectedNoteState.content,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(noteViewVerticalScrollState)
                    .horizontalScroll(noteViewHorizontalScrollState),
            )
        }
    )
}