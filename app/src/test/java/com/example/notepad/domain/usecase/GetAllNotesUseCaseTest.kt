package com.example.notepad.domain.usecase

import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test

import com.example.notepad.data.room_database.NoteEntity
import com.example.notepad.domain.repository.DataStoreRepository
import com.example.notepad.domain.repository.NoteRepository
import com.example.notepad.domain.usecase.note.GetAllNotesUseCase
import com.example.notepad.presentation.notes.NotesListResult
import com.example.notepad.proto.NoteDisplaySettings
import com.example.notepad.proto.SortNotesMode

class GetAllNotesUseCaseTest {
    @Test
    fun `test get all notes use case`() = runBlocking {
        val noteRepository = mockk<NoteRepository>()
        val dataStoreRepository = mockk<DataStoreRepository>()

        val useCase = GetAllNotesUseCase(noteRepository, dataStoreRepository)

        val testNotesList = listOf(
            NoteEntity(
                id = 1,
                name = "test1",
                content = "1 note"
            ),
            NoteEntity(
                id = 2,
                name = "test2",
                content = "2 note"
            ),
            NoteEntity(
                id = 3,
                name = "test3",
                content = "3 note"
            )
        )

        val testSettings = NoteDisplaySettings.newBuilder()
            .setIsGridEnabled(false)
            .setIsZebraNoteColorsEnabled(false)
            .setIsOrderNumEnabled(false)
            .setSortMode(SortNotesMode.Default)
            .setIsSortAsc(false)
            .build()

        coEvery { noteRepository.getAllNotes() } returns flowOf(testNotesList)
        coEvery { dataStoreRepository.getNotesDisplaySettings() } returns flowOf(testSettings)

        val result = useCase().first()

        assertTrue(result !is NotesListResult.Exception && result !is NotesListResult.EmptyList)
    }
}