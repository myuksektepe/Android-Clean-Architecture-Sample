package murat.cleanarchitecture.sample.presentation.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import murat.cleanarchitecture.sample.domain.repository.NotesRepository
import murat.cleanarchitecture.sample.domain.usecase.GetNotesUseCase

@InstallIn(ActivityRetainedComponent::class)
@Module
object UseCaseModule {

    @Provides
    fun providesGetNotesUseCase(respository: NotesRepository): GetNotesUseCase {
        return GetNotesUseCase(respository)
    }

}