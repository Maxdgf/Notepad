package com.example.notepad.core.data_management.databases.notes_local_storage

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import junit.framework.TestCase

import com.example.notepad.core.data_management.databases.notes_local_storage.entities.NoteEntity

@RunWith(AndroidJUnit4::class)
class NoteDatabaseTest : TestCase() {
    private lateinit var noteDao: NoteDao
    private lateinit var db: NoteDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, NoteDatabase::class.java).build()
        noteDao = db.getNoteDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() =
        db.close()

    @Test
    @Throws(Exception::class)
    fun addNoteToDatabase() = runBlocking {
        val note = NoteEntity(
            name = "My test note",
            content = "Text of test note."
        )

        noteDao.addNote(note)
        val result = noteDao.getAllNotes().first()

        assertTrue(result.any { it.name == note.name && it.content == note.content })
    }

    @Test
    @Throws(Exception::class)
    fun deleteNoteFromDatabase() = runBlocking {
        val note1 = NoteEntity(
            name = "My test note 1",
            content = "Text of test note 1. 👌"
        )

        val note2 = NoteEntity(
            name = "My test note 2",
            content = "Text of test note 2. ✌️"
        )

        noteDao.apply {
            addNote(note1)
            addNote(note2)
            deleteNote(1)
        }

        val result = noteDao.getAllNotes().first()

        assertTrue(!result.contains(note1))
    }

    @Test
    @Throws(Exception::class)
    fun updateNoteInDatabase() = runBlocking {
        val note = NoteEntity(
            name = "My note for update",
            content = "This is a temporary text."
        )

        noteDao.apply {
            addNote(note)
            updateNote(
                name = "My updated test note",
                content = "Note has been updated! ✨",
                lastEditDateTime = System.currentTimeMillis(),
                id = 1
            )
        }

        val updatedNote = noteDao.getNoteById(1).first()

        assertTrue(updatedNote != null)
        assertEquals(updatedNote?.name, "My updated test note")
        assertEquals(updatedNote?.content, "Note has been updated! ✨")
        assertTrue(updatedNote?.lastEditDateTime != null)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllNotesInDatabase() = runBlocking {
        noteDao.deleteAllNotes()
        val result = noteDao.getAllNotes().first()

        assertTrue(result.isEmpty())
    }
}