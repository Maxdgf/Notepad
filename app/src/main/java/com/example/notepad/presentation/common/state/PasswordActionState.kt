package com.example.notepad.presentation.common.state

sealed class PasswordActionState {
    object Input : PasswordActionState()  // password is being input now
    object Submit : PasswordActionState()
}