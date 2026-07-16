package com.example.notepad.presentation.app_settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

import com.example.notepad.R
import com.example.notepad.domain.model.SortNotesModeName
import com.example.notepad.domain.model.toDomainEnum
import com.example.notepad.presentation.common.components.TopAppBar
import com.example.notepad.presentation.common.components.SwitchWithText
import com.example.notepad.presentation.navigation.NavigationRoutes

@Composable
private fun SettingsParametersPanel(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(5.dp)
    ) {
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        HorizontalDivider()

        content()
    }
}

/*** Creates a settings app screen.*/
@Composable
fun SettingsAppScreen(onNavigateTo: (String) -> Unit) {
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val notesDisplaySettings by settingsViewModel.notesDisplaySettings.collectAsState()

    val haptic = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            TopAppBar(
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
                .padding(10.dp)
        ) {
            SettingsParametersPanel(title = "Note display settings") {
                SwitchWithText(
                    checked = notesDisplaySettings.isGridEnabled,
                    text = "notes list grid view",
                    onCheckedChange = { state ->
                        haptic.performHapticFeedback(HapticFeedbackType.KeyboardTap)
                        settingsViewModel.performEvent(SettingsEvent.UpdateGridState(state))
                    }
                )

                SwitchWithText(
                    checked = notesDisplaySettings.isOrderNumEnabled,
                    text = "display order num",
                    onCheckedChange = { state ->
                        haptic.performHapticFeedback(HapticFeedbackType.KeyboardTap)
                        settingsViewModel.performEvent(SettingsEvent.UpdateOrderNumState(state))
                    }
                )

                SwitchWithText(
                    checked = notesDisplaySettings.isZebraNoteColorsEnabled,
                    text = "zebra note colors",
                    onCheckedChange = { state ->
                        haptic.performHapticFeedback(HapticFeedbackType.KeyboardTap)
                        settingsViewModel.performEvent(SettingsEvent.UpdateZebraColorsState(state))
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            SettingsParametersPanel(title = "Sort notes") {
                SortNotesModeName.entries.forEach { mode ->
                    SwitchWithText(
                        checked = notesDisplaySettings.sortMode.toDomainEnum().name == mode.name,
                        onCheckedChange = { _ ->
                            haptic.performHapticFeedback(HapticFeedbackType.KeyboardTap)

                            if (notesDisplaySettings.sortMode.toDomainEnum().name != mode.name) {
                                settingsViewModel.performEvent(
                                    SettingsEvent.UpdateSortModeState(mode)
                                )
                            }
                        },
                        text = mode.name
                    )
                }

                HorizontalDivider()

                SwitchWithText(
                    enabled = notesDisplaySettings.sortMode.toDomainEnum().name != SortNotesModeName.Default.name,
                    checked = notesDisplaySettings.isSortAsc,
                    text = "is ASC",
                    onCheckedChange = { state ->
                        haptic.performHapticFeedback(HapticFeedbackType.KeyboardTap)
                        settingsViewModel.performEvent(SettingsEvent.UpdateAscSortState(state))
                    }
                )
            }
        }
    }
}