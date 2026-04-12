package com.example.notepad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.notepad.ui.screens.MainUiNotePad
import com.example.notepad.ui.theme.NotepadTheme
import dagger.hilt.android.AndroidEntryPoint

// =======================================================
// |                      Notepad                        |
// =======================================================
// | Simple and minimalistic android app for notes based |
// | on Jetpack Compose, with clean MVVM architecture.   |
// |-----------------------------------------------------|
// | by Maxdgf github: https://github.com/Maxdgf/Notepad |
// =======================================================
// |--> created at: 27.09.2025
// |--> last update: 11.04.2026

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotepadTheme {
                MainUiNotePad() // draw app content
            }
        }
    }
}