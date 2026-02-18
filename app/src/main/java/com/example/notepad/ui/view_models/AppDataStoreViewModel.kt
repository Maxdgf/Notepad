package com.example.notepad.ui.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notepad.app_data_store.repository.AppDataStoreImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppDataStoreViewModel @Inject constructor(private val appDataStoreImpl: AppDataStoreImpl) : ViewModel() {
    val state = appDataStoreImpl.getGridEnabledState().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        false
    )

    val noteTextSize = appDataStoreImpl.getNoteTextSize().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        10
    )

    fun saveState(state: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            appDataStoreImpl.saveGridEnabledState(state)
        }
    }

    fun saveNoteTextSize(size: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            appDataStoreImpl.saveNoteTextSize(size)
        }
    }
}