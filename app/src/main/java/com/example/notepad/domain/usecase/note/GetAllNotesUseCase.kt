package com.example.notepad.domain.usecase.note

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import javax.inject.Inject

import com.example.notepad.proto.SortNotesMode
import com.example.notepad.data.room_database.toDomainModel
import com.example.notepad.domain.repository.DataStoreRepository
import com.example.notepad.domain.repository.NoteRepository
import com.example.notepad.presentation.notes.NotesListResult

class GetAllNotesUseCase @Inject constructor(
    private val noteRepository: NoteRepository,
    private val dataStoreRepository: DataStoreRepository
) {
    /**
     * Sorts notes list by specific selector, by ASC or DESC.
     *
     * @param isAsc sort by ASC
     * @param selector specific selector for sorting
     *
     * @return sorted list of notes
     */
    private fun <Note, R : Comparable<R>> List<Note>.sortByAscOrDesc(
        isAsc: Boolean,
        selector: (Note) -> R?
    ): List<Note> = if (isAsc) {
        this.sortedBy(selector) // sort by ASC
    } else {
        this.sortedByDescending(selector) // sort by DESC
    }

    operator fun invoke(): Flow<NotesListResult> {
        return noteRepository.getAllNotes()
            .combine(dataStoreRepository.getNotesDisplaySettings()) { notes, displaySettings ->
                if (notes.isNotEmpty()) {
                    var notesList = withContext(Dispatchers.Default) {
                        notes.map { it.toDomainModel() }
                    }
                    val isAsc = displaySettings.isSortAsc // is ASC or DESC

                    // sort note list
                    notesList = when (displaySettings.sortMode) {
                        SortNotesMode.Default -> notesList // by default
                        SortNotesMode.ByCreationTime -> withContext(Dispatchers.Default) {
                            notesList.sortByAscOrDesc(isAsc) { it.creationTime } // by creation time millis
                        }
                        SortNotesMode.ByContentSize -> withContext(Dispatchers.Default) {
                            notesList.sortByAscOrDesc(isAsc) { it.content.length } // by note's content size
                        }
                        SortNotesMode.ByNameLength -> withContext(Dispatchers.Default) {
                            notesList.sortByAscOrDesc(isAsc) { it.name.length } // by note's name length
                        }
                        SortNotesMode.ByLastEditTime -> withContext(Dispatchers.Default) {
                            notesList.sortByAscOrDesc(isAsc) { it.lastEditTime } // by last edit time millis
                        }
                        SortNotesMode.UNRECOGNIZED -> notesList
                    }

                    NotesListResult.ContentList(notesList)
                } else {
                    NotesListResult.EmptyList
                }
            }
            .catch { exception ->
                // emit exception state
                emit(
                    NotesListResult.Exception(
                        exception.message ?:
                        "An unexpected error occurred, the notes was not loaded."
                    )
                )
            }
    }
}
