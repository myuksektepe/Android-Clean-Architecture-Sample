package murat.cleanarchitecture.sample.presentation.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import murat.cleanarchitecture.sample.data.db.room.note.NoteDao
import murat.cleanarchitecture.sample.data.repository.notes.NotesLocalDataSource
import murat.cleanarchitecture.sample.data.repository.notes.NotesLocalDataSourceImpl

@InstallIn(ActivityRetainedComponent::class)
@Module
object DataSourceModule {

    @Provides
    fun provideNotesLocalDataSource(noteDao: NoteDao): NotesLocalDataSource {
        return NotesLocalDataSourceImpl(noteDao)
    }
}