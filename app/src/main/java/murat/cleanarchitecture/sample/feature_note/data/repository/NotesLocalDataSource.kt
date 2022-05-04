package murat.cleanarchitecture.sample.feature_note.data.repository

import murat.cleanarchitecture.sample.feature_note.domain.model.Note

/**
 * Feature -> LocalDataSource
 *
 * Her bir feature (özellik) için local (yerel) data kaynağı üzerinde yapılacak işler için oluşturulan
 * fonksiyonları barındıran interface'lerdir.
 *
 * Interface'ler Hilt'e inject edilemedikleri için kendilerinden kalıtılmış bir Implementation sınıfına ihtiyaçları vardır.
 *
 */

interface NotesLocalDataSource {
    suspend fun getNotes(): MutableList<Note>
    suspend fun getNote(id: Int): Note
    suspend fun insertNote(note: Note): Unit
    suspend fun updateNote(note: Note, oldNote: Note): Unit
    suspend fun deleteNote(note: Note)
}