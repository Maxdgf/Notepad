package com.example.notepad.presentation.app_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notepad.domain.model.toProtoEnum
import com.example.notepad.domain.repository.DataStoreRepository
import com.example.notepad.proto.NoteDisplaySettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val dataStoreRepository: DataStoreRepository) : ViewModel() {
    val notesDisplaySettings = dataStoreRepository.getNotesDisplaySettings().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        NoteDisplaySettings.getDefaultInstance()
    )

    fun performEvent(event: SettingsEvent) {
        val noteSettings = notesDisplaySettings.value.toBuilder()

        when (event) {
            is SettingsEvent.UpdateGridState -> noteSettings.setIsGridEnabled(event.state)
            is SettingsEvent.UpdateOrderNumState -> noteSettings.setIsOrderNumEnabled(event.state)
            is SettingsEvent.UpdateZebraColorsState -> noteSettings.setIsZebraNoteColorsEnabled(event.state)
            is SettingsEvent.UpdateSortModeState -> noteSettings.setSortMode(event.mode.toProtoEnum())
            is SettingsEvent.UpdateAscSortState -> noteSettings.setIsSortAsc(event.state)
        }

        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveNotesDisplaySettings(noteSettings.build())
        }
    }
}