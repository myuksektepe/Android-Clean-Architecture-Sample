package murat.cleanarchitecture.sample.util.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import murat.cleanarchitecture.sample.feature_note.domain.repository.NotesRepository
import murat.cleanarchitecture.sample.feature_note.domain.use_case.GetNoteByIdUseCase
import murat.cleanarchitecture.sample.feature_note.domain.use_case.GetNotesUseCase
import murat.cleanarchitecture.sample.feature_note.domain.use_case.InsertNoteUseCase
import murat.cleanarchitecture.sample.feature_note.domain.use_case.NoteUseCases
import javax.inject.Singleton

@InstallIn(ActivityRetainedComponent::class)
@Module
object UseCaseModule {

    @Provides
    @Singleton
    fun provideNoteUseCases(
        notesRepository: NotesRepository
    ): NoteUseCases {
        return NoteUseCases(
            getNotesUseCase = GetNotesUseCase(notesRepository),
            getNoteByIdUseCase = GetNoteByIdUseCase(notesRepository),
            insertNoteUseCase = InsertNoteUseCase(notesRepository),
        )
    }

}