package com.example.notepad.presentation.common.state

sealed class PasswordActionState {
    object Input : PasswordActionState()
    object Submit : PasswordActionState()
}