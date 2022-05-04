package murat.cleanarchitecture.sample.feature_note.domain.use_case

import kotlinx.coroutines.flow.Flow
import murat.cleanarchitecture.sample.feature_note.domain.model.Note
import murat.cleanarchitecture.sample.feature_note.domain.repository.NotesRepository
import murat.cleanarchitecture.sample.util.ResultState
import javax.inject.Inject

/**
 * Bknz: GetNotesUseCase.kt
 */

class InsertNoteUseCase @Inject constructor(private val repository: NotesRepository) {
    operator fun invoke(note: Note): Flow<ResultState<Unit>> = repository.insertNote(note)
}