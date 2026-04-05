package com.example.notepad.ui.view_models

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.example.notepad.app_data_store.repository.AppDataStoreRepository

@HiltViewModel
class AppDataStoreViewModel @Inject constructor(private val appDataStoreRepository: AppDataStoreRepository) : ViewModel() {
    val notesGridEnabledMode = appDataStoreRepository.getGridEnabledState().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        false
    )

    val noteTextSize = appDataStoreRepository.getNoteTextSize().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        10
    )

    val textWrapMode = appDataStoreRepository.getTextWrapState().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        false
    )

    val orderNumEnabledState = appDataStoreRepository.getOrderNumState().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        false
    )

    val alternatingNoteColorsEnabledState = appDataStoreRepository.getAlternatingNoteColorsState().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        false
    )

    fun saveNotesGridEnabledState(state: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            appDataStoreRepository.saveGridEnabledState(state)
        }

    fun saveNoteTextSize(size: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            appDataStoreRepository.saveNoteTextSize(size)
        }

    fun saveTextWrapState(state: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            appDataStoreRepository.saveTextWrapState(state)
        }

    fun saveOrderNumEnabledState(state: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            appDataStoreRepository.saveOrderNumState(state)
        }

    fun saveAlternatingNoteColorsState(state: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            appDataStoreRepository.saveAlternatingNoteColorsState(state)
        }
}