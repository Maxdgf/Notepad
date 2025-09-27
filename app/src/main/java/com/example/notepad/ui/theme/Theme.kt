package com.example.notepad.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = granite,
    onPrimary = white,
    secondary = arlekin,
    onSecondary = verdepomise,
    error = error,
    onError = lightError
)

private val LightColorScheme = lightColorScheme(
    primary = pear,
    onPrimary = black,
    secondary = verdepomise,
    onSecondary = arlekin,
    error = error,
    onError = lightError
)

@Composable
fun NotepadTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}