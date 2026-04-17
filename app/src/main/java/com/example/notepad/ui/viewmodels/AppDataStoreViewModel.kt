package com.example.notepad.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.example.notepad.app_data_store.repository.AppDataStoreRepository
import com.example.notepad.proto.NoteDisplaySettings
import com.example.notepad.proto.NoteViewSettings

@HiltViewModel
class AppDataStoreViewModel @Inject constructor(
    private val appDataStoreRepository: AppDataStoreRepository
) : ViewModel() {
    val noteViewSettings = appDataStoreRepository.getNoteViewSettings().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        NoteViewSettings.getDefaultInstance()
    )

    val notesDisplaySettings = appDataStoreRepository.getNotesDisplaySettings().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        NoteDisplaySettings.getDefaultInstance()
    )

    fun saveNoteViewSettings(noteSettings: NoteViewSettings) =
        viewModelScope.launch(Dispatchers.IO) {
            appDataStoreRepository.saveNoteViewSettings(noteSettings)
        }

    fun saveNotesDisplaySettings(noteSettings: NoteDisplaySettings) =
        viewModelScope.launch(Dispatchers.IO) {
            appDataStoreRepository.saveNotesDisplaySettings(noteSettings)
        }
}