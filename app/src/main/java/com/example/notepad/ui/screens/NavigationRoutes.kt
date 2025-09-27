package com.example.notepad.ui.screens

sealed class NavigationRoutes(val route: String) {
    object MainScreen: NavigationRoutes("main_screen")
    object NoteCreationScreen: NavigationRoutes("note_creation_screen")
    object NoteViewScreen: NavigationRoutes("note_view_screen")
    object NoteEditScreen: NavigationRoutes("note_edit_screen")
}