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

import com.example.notepad.R
import com.example.notepad.ui.components.TopUiBar
import com.example.notepad.ui.components.CheckBoxWithUiText
import com.example.notepad.ui.navigation.NavigationRoutes
import com.example.notepad.ui.navigation.Navigator

/**Creates a settings app screen.*/
@Composable
fun SettingsUiScreen(
    navigator: Navigator,
    isGridEnabledState: Boolean,
    updateIsGridEnabledStateMethod: (Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            TopUiBar(
                titleContent = { Text(text = "Settings") },
                barIcon = {
                    IconButton(onClick = { navigator.navigateTo(NavigationRoutes.MainScreen.route) }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_arrow_back_24),
                            contentDescription = null
                        )
                    }
                },
                barActionElements = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_settings_24),
                        contentDescription = null
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CheckBoxWithUiText(
                checked = isGridEnabledState,
                text = "notes list grid view",
                onCheckedChange = { state -> updateIsGridEnabledStateMethod(state) }
            )
        }
    }
}