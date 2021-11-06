package murat.cleanarchitecture.sample.domain.usecase

import kotlinx.coroutines.flow.Flow
import murat.cleanarchitecture.sample.domain.model.Note
import murat.cleanarchitecture.sample.domain.model.ResultData
import murat.cleanarchitecture.sample.domain.repository.NotesRepository
import javax.inject.Inject

class InsertNoteUseCase @Inject constructor(private val respository: NotesRepository) {
    operator fun invoke(note: Note): Flow<ResultData<Unit>> = respository.insertNote(note)
}