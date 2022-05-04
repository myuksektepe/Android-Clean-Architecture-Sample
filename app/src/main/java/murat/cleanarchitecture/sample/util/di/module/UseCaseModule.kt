package murat.cleanarchitecture.sample.util.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import murat.cleanarchitecture.sample.feature_note.domain.repository.NotesRepository
import murat.cleanarchitecture.sample.feature_note.domain.use_case.*

@InstallIn(ActivityRetainedComponent::class)
@Module
object UseCaseModule {

    @Provides
    fun provideNoteUseCases(
        notesRepository: NotesRepository
    ): NoteUseCases {
        return NoteUseCases(
            getNotesUseCase = GetNotesUseCase(notesRepository),
            getNoteUseCase = GetNoteUseCase(notesRepository),
            insertNoteUseCase = InsertNoteUseCase(notesRepository),
            deleteNoteUseCases = DeleteUseCase(notesRepository)
        )
    }

}