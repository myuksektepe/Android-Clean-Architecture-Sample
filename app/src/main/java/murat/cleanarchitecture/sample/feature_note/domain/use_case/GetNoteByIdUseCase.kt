package murat.cleanarchitecture.sample.feature_note.domain.use_case

import kotlinx.coroutines.flow.Flow
import murat.cleanarchitecture.sample.feature_note.domain.model.Note
import murat.cleanarchitecture.sample.feature_note.domain.repository.NotesRepository
import murat.cleanarchitecture.sample.util.ResultState
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(private val repository: NotesRepository) {
    fun invoke(id: Int): Flow<ResultState<Note>> = repository.getNote(id)
}