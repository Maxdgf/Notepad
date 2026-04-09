package com.example.notepad.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.notepad.R
import com.example.notepad.ui.components.TopUiBar
import com.example.notepad.ui.components.CheckBoxWithUiText
import com.example.notepad.ui.screens.navigation.NavigationRoutes

/*** Creates a settings app screen.*/
@Composable
fun SettingsUiScreen(
    onNavigateTo: (String) -> Unit,
    isGridViewEnabled: Boolean,
    onUpdateIsGridEnabledState: (Boolean) -> Unit,
    isDisplayOrderNumEnabled: Boolean,
    onUpdateIsDisplayOrderNumEnabledState: (Boolean) -> Unit,
    isAlternatingNoteColorsEnabled: Boolean,
    onUpdateIsAlternatingNoteColorsEnabled: (Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            TopUiBar(
                titleContent = { Text(text = "Settings") },
                barIcon = {
                    IconButton(onClick = { onNavigateTo(NavigationRoutes.MainScreen.route) }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_arrow_back_24),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(5.dp)
        ) {
            Text(
                text = "Note display settings",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            CheckBoxWithUiText(
                checked = isGridViewEnabled,
                text = "notes list grid view",
                onCheckedChange = { state -> onUpdateIsGridEnabledState(state) }
            )

            CheckBoxWithUiText(
                checked = isDisplayOrderNumEnabled,
                text = "display order num",
                onCheckedChange = { state -> onUpdateIsDisplayOrderNumEnabledState(state) }
            )

            CheckBoxWithUiText(
                checked = isAlternatingNoteColorsEnabled,
                text = "alternating note colors",
                onCheckedChange = { state -> onUpdateIsAlternatingNoteColorsEnabled(state) }
            )
        }
    }
}