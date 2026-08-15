package com.example.notepad.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

import com.example.notepad.domain.repository.DataStoreRepository
import com.example.notepad.proto.NoteDisplaySettings

@HiltViewModel
class NoteDisplaySettingsViewModel @Inject constructor(dataStoreRepository: DataStoreRepository) : ViewModel() {
    val notesDisplaySettings = dataStoreRepository.getNotesDisplaySettings().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        NoteDisplaySettings.getDefaultInstance()
    )
}