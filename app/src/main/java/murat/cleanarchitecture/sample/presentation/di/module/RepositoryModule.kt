package murat.cleanarchitecture.sample.presentation.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import murat.cleanarchitecture.sample.data.repository.notes.NotesLocalDataSource
import murat.cleanarchitecture.sample.data.repository.notes.NotesRepositoryImpl
import murat.cleanarchitecture.sample.domain.repository.NotesRepository

@InstallIn(ActivityRetainedComponent::class)
@Module
object RepositoryModule {

    @Provides
    fun provideNoteRepository(localDataSource: NotesLocalDataSource): NotesRepository {
        return NotesRepositoryImpl(localDataSource)
    }

}