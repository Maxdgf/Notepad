package com.example.notepad.presentation.create_note

enum class PasswordValidation {
    IsEmpty,
    IsBlank,
    WhiteSpacesAtStartAndEndFound,
    ToShort,
    ToLong,
    EngLettersDigitsSpecCharsExcepted,
    FewSpecialChars,
    FewLetters,
    FewDigits,
    Valid
}