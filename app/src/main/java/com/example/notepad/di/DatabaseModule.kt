package com.example.notepad.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

import com.example.notepad.data.room_database.NoteDao
import com.example.notepad.data.room_database.NoteDatabase

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideNoteAppDatabase(@ApplicationContext context: Context): NoteDatabase =
        Room.databaseBuilder(context, NoteDatabase::class.java, "notes_database")
            .fallbackToDestructiveMigration(false)
            .build()

    @Provides
    fun provideNoteDao(noteDatabase: NoteDatabase): NoteDao =
        noteDatabase.getNoteDao()
}