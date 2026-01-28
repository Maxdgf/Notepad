package com.example.notepad

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(name = "Notepad_preferences")

val gridEnabledState = booleanPreferencesKey(name = "is_grid_enabled")