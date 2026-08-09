package com.example.notepad.presentation.common.state

sealed class PasswordState {
    object Empty : PasswordState()
    object Incorrect : PasswordState()
    object None : PasswordState()
}