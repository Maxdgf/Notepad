package com.example.notepad.presentation.create_note

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.trimmedLength
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlin.time.Duration.Companion.milliseconds

class NoteCreationScreenViewModel : ViewModel() {
    var noteName by mutableStateOf("")
    var noteContent by mutableStateOf("")

    var password by mutableStateOf("")
    var passwordHint by mutableStateOf("")

    private val _passwordString = MutableStateFlow("")
    val passwordString = _passwordString.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val passwordValidationState = _passwordString
        .debounce(250.milliseconds)
        .distinctUntilChanged()
        .transformLatest { password ->
            emit(
                when {
                    password.isEmpty() -> PasswordValidation.IsEmpty // check is password string empty
                    password.isBlank() -> PasswordValidation.IsBlank // check is password string blank
                    password.length != password.trimmedLength() -> PasswordValidation.WhiteSpacesAtStartAndEndFound

                    // check password string length
                    password.length < MIN_PASSWORD_LENGTH -> PasswordValidation.ToShort
                    password.length > MAX_PASSWORD_LENGTH -> PasswordValidation.ToLong

                    password.any { it.code !in 33..126 } -> PasswordValidation.EngLettersDigitsSpecCharsExcepted

                    // check chars, digits and spec chars min count
                    password.count { it.isLetter() } < MIN_LETTERS_COUNT -> PasswordValidation.FewLetters
                    password.count { it.isDigit() } < MIN_DIGITS_COUNT -> PasswordValidation.FewDigits
                    password.count { !it.isLetterOrDigit() } < MIN_SPEC_CHARS_COUNT -> PasswordValidation.FewSpecialChars

                    else -> PasswordValidation.Valid
                }
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            PasswordValidation.IsEmpty
        )


    /** Checks is note name and content empty
     * @return boolean flag. */
    fun isNoteNameOrContentEmpty(): Boolean =
        noteName.isEmpty() || noteContent.isEmpty()

    fun updatePasswordString(password: String) {
        _passwordString.value = password
    }
}