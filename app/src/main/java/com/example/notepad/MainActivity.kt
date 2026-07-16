package com.example.notepad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.notepad.presentation.AppNavGraphRoot
import com.example.notepad.presentation.common.theme.NotepadTheme
import com.example.notepad.presentation.create_note.NoteManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// =======================================================
// |                      Notepad                        |
// =======================================================
// | Simple and minimalistic android app for notes based |
// | on Jetpack Compose, with clean MVVM architecture.   |
// |-----------------------------------------------------|
// | by Maxdgf github: https://github.com/Maxdgf/Notepad |
// =======================================================
// |--> created at: 27.09.2025
// |--> last update: 16.07.2026

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var noteManager: NoteManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotepadTheme {
                // draw app content
                AppNavGraphRoot(noteManager = noteManager)
            }
        }
    }
}