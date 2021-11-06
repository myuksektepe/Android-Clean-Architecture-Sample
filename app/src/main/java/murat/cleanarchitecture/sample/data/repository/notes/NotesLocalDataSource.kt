package murat.cleanarchitecture.sample.data.repository.notes

import murat.cleanarchitecture.sample.domain.model.Note

interface NotesLocalDataSource {
    suspend fun getNotes(): MutableList<Note>
    suspend fun insertNote(note: Note): Unit
    suspend fun updateNote(note: Note, oldNote: Note): Unit
}