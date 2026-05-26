package com.example.notepad.core.data_management.databases.notes_local_storage

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.notepad.core.data_management.databases.notes_local_storage.entities.NoteEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class NoteDatabaseTest {
    private lateinit var userDao: NoteDao
    private lateinit var db: NoteDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, NoteDatabase::class.java).build()
        userDao = db.getNoteDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() = db.close()

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() = runBlocking {
        val note = NoteEntity(
            name = "My test note",
            content = "Text of test note."
        )

        userDao.addNote(note)
        val result = userDao.getAllNotes().first()

        assertTrue(result.any { it.name == note.name && it.content == note.content })
    }
}