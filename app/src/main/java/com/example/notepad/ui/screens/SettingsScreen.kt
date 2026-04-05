package com.example.notepad.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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

/**Creates a settings app screen.*/
@Composable
fun SettingsUiScreen(
    onNavigateTo: (String) -> Unit,
    isGridEnabledState: Boolean,
    updateIsGridEnabledStateMethod: (Boolean) -> Unit,
    isDisplayOrderNumEnabledState: Boolean,
    updateIsDisplayOrderNumEnabledStateMethod: (Boolean) -> Unit,
    isAlternatingNoteColorsEnabledState: Boolean,
    updateIsAlternatingNoteColorsEnabledState: (Boolean) -> Unit
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
                checked = isGridEnabledState,
                text = "notes list grid view",
                onCheckedChange = { state -> updateIsGridEnabledStateMethod(state) }
            )

            CheckBoxWithUiText(
                checked = isDisplayOrderNumEnabledState,
                text = "display order num",
                onCheckedChange = { state -> updateIsDisplayOrderNumEnabledStateMethod(state) }
            )

            CheckBoxWithUiText(
                checked = isAlternatingNoteColorsEnabledState,
                text = "alternating note colors",
                onCheckedChange = { state -> updateIsAlternatingNoteColorsEnabledState(state) }
            )
        }
    }
}