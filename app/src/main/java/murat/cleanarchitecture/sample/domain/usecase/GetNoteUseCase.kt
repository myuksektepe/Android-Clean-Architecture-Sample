package murat.cleanarchitecture.sample.domain.usecase

import kotlinx.coroutines.flow.Flow
import murat.cleanarchitecture.sample.domain.model.Note
import murat.cleanarchitecture.sample.domain.model.ResultData
import murat.cleanarchitecture.sample.domain.repository.NotesRepository
import javax.inject.Inject

class GetNoteUseCase @Inject constructor(private val repository: NotesRepository) {
    fun invoke(id: Int): Flow<ResultData<Note>> = repository.getNote(id)
}