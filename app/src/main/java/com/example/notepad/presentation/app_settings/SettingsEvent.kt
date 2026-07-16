package com.example.notepad.presentation.app_settings

import com.example.notepad.domain.model.SortNotesModeName

sealed interface SettingsEvent {
    data class UpdateGridState(val state: Boolean) : SettingsEvent
    data class UpdateOrderNumState(val state: Boolean) : SettingsEvent
    data class UpdateZebraColorsState(val state: Boolean) : SettingsEvent
    data class UpdateSortModeState(val mode: SortNotesModeName) : SettingsEvent
    data class UpdateAscSortState(val state: Boolean) : SettingsEvent
}