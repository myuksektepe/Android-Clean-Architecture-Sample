package murat.cleanarchitecture.sample.feature_note.domain.use_case

import kotlinx.coroutines.flow.Flow
import murat.cleanarchitecture.sample.feature_note.domain.model.Note
import murat.cleanarchitecture.sample.feature_note.domain.repository.NotesRepository
import murat.cleanarchitecture.sample.util.model.ResultState
import javax.inject.Inject

class DeleteUseCase @Inject constructor(private val repository: NotesRepository) {
    fun invoke(note: Note): Flow<ResultState<Unit>> = repository.deleteNote(note)
}