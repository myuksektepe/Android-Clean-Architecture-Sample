package murat.cleanarchitecture.sample.util.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import murat.cleanarchitecture.sample.feature_note.data.repository.NotesLocalDataSource
import murat.cleanarchitecture.sample.feature_note.data.repository.NotesRepositoryImpl
import murat.cleanarchitecture.sample.feature_note.domain.repository.NotesRepository

@InstallIn(ActivityRetainedComponent::class)
@Module
object RepositoryModule {

    @Provides
    fun provideNoteRepository(localDataSource: NotesLocalDataSource): NotesRepository {
        return NotesRepositoryImpl(localDataSource)
    }

}