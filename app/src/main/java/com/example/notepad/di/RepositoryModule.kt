package com.example.notepad.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import com.example.notepad.data.repository_impl.DataStoreRepositoryImpl
import com.example.notepad.domain.repository.DataStoreRepository
import com.example.notepad.domain.repository.NoteRepository
import com.example.notepad.data.repository_impl.NoteRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindNoteRepository(impl: NoteRepositoryImpl): NoteRepository

    @Binds
    abstract fun bindDataStoreRepository(impl: DataStoreRepositoryImpl): DataStoreRepository
}