package com.example.notepad.presentation.common.state

sealed class PasswordState {
    object Empty : PasswordState()     // is empty
    object Incorrect : PasswordState() // is incorrect
    object None : PasswordState()      // none
}