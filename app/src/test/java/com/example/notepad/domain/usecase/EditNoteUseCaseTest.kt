package com.example.notepad.domain.usecase

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test

import com.example.notepad.domain.crypto.TextCipher
import com.example.notepad.domain.repository.NoteRepository
import com.example.notepad.domain.usecase.note.EditNoteUseCase

class EditNoteUseCaseTest {
    @Test
    fun `test edit note use case with password`() = runBlocking {
        val noteRepositoryMock = mockk<NoteRepository>()
        val textCipherMock = mockk<TextCipher>()

        coEvery { noteRepositoryMock.editNote("new name", "encrypted content", "salt", 1) } just Runs
        every { textCipherMock.encryptTextWithPassword("password", "new content") } returns Pair("encrypted content", "salt")

        val useCase = EditNoteUseCase(noteRepositoryMock, textCipherMock)
        useCase("password", 1, "new name", "new content")

        verify(exactly = 1) { textCipherMock.encryptTextWithPassword("password", "new content") }
        coVerify(exactly = 1) { noteRepositoryMock.editNote("new name", "encrypted content", "salt", 1) }
    }

    @Test
    fun `test edit note use case without password`() = runBlocking {
        val noteRepositoryMock = mockk<NoteRepository>()
        coEvery { noteRepositoryMock.editNote("new name", "new content", null, 1) } just Runs

        val useCase = EditNoteUseCase(noteRepositoryMock, TextCipher())
        useCase(null, 1, "new name", "new content")

        coVerify { noteRepositoryMock.editNote("new name", "new content", null, 1) }
    }
}