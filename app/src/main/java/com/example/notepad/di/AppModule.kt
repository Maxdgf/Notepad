package com.example.notepad.di

import android.content.Context
import androidx.room.Room
import com.example.notepad.app_data_store.repository.AppDataStoreImpl
import com.example.notepad.app_data_store.repository.AppDataStoreRepository
import com.example.notepad.core.data_management.databases.notes_local_storage.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

import com.example.notepad.core.data_management.databases.notes_local_storage.NoteDatabase
import com.example.notepad.core.data_management.databases.notes_local_storage.repository.NoteRepository
import com.example.notepad.core.data_management.databases.notes_local_storage.repository.NoteRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideNoteAppDatabase(@ApplicationContext context: Context): NoteDatabase =
        Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            "notes_database"
        ).fallbackToDestructiveMigration(false).build()

    @Provides
    fun provideNoteDao(noteDatabase: NoteDatabase): NoteDao =
        noteDatabase.getNoteDao()

    @Singleton
    @Provides
    fun provideNoteRepository(noteDao: NoteDao): NoteRepository =
        NoteRepositoryImpl(noteDao)

    @Singleton
    @Provides
    fun provideAppDataStoreManager(
        @ApplicationContext context: Context
    ): AppDataStoreRepository = AppDataStoreImpl(context)
}