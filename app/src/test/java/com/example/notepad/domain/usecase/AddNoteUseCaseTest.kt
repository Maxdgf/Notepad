package com.example.notepad.domain.usecase

import com.example.notepad.domain.crypto.TextCipher
import com.example.notepad.domain.model.Note
import com.example.notepad.domain.repository.NoteRepository
import com.example.notepad.domain.usecase.note.AddNoteUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test

class AddNoteUseCaseTest {
    @Test
    fun `test add note use case with password`() = runBlocking {
        val noteRepositoryMock = mockk<NoteRepository>()
        val textCipherMock = mockk<TextCipher>()

        val outputNote = Note(
            name = "name",
            content = "encrypted content",
            passwordSalt = "salt"
        )

        every { textCipherMock.encryptTextWithPassword("password", "content") } returns Pair("encrypted content", "salt")
        coEvery { noteRepositoryMock.addNote(outputNote) } just Runs

        val useCase = AddNoteUseCase(noteRepositoryMock, textCipherMock)

        val inputNote = Note(
            name = "name",
            content = "content"
        )

        useCase("password", inputNote)

        verify(exactly = 1) { textCipherMock.encryptTextWithPassword("password", "content") }
        coVerify(exactly = 1) { noteRepositoryMock.addNote(outputNote) }
    }

    @Test
    fun `test add note use case without password`() = runBlocking {
        val noteRepositoryMock = mockk<NoteRepository>()

        val useCase = AddNoteUseCase(noteRepositoryMock, TextCipher())

        val note = Note(
            name = "name",
            content = "content"
        )

        coEvery { noteRepositoryMock.addNote(note) } just Runs

        useCase(null, note)

        coVerify(exactly = 1) { noteRepositoryMock.addNote(note) }
    }
}