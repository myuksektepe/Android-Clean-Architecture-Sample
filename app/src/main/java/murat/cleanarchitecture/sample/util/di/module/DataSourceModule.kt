package murat.cleanarchitecture.sample.util.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import murat.cleanarchitecture.sample.feature_note.data.data_source.NoteDao
import murat.cleanarchitecture.sample.feature_note.data.repository.NotesLocalDataSource
import murat.cleanarchitecture.sample.feature_note.data.repository.NotesLocalDataSourceImpl

@InstallIn(ActivityRetainedComponent::class)
@Module
object DataSourceModule {

    @Provides
    fun provideNotesLocalDataSource(noteDao: NoteDao): NotesLocalDataSource {
        return NotesLocalDataSourceImpl(noteDao)
    }
}