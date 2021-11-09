package murat.cleanarchitecture.sample.data.repository.notes

import android.util.Log
import murat.cleanarchitecture.sample.TAG
import murat.cleanarchitecture.sample.data.db.room.note.NoteDao
import murat.cleanarchitecture.sample.data.mapper.toNote
import murat.cleanarchitecture.sample.data.mapper.toNoteEntity
import murat.cleanarchitecture.sample.domain.model.Note
import javax.inject.Inject

/**
 * Feature -> LocalDataSourceImpl
 *
 * LocalDataSource'lerden katılıtan implementation sınıflarıdır.
 *
 * İlgili feature (özellik) için oluşturulan Data Access Object (Veri Erişim Nesnesi)'i inject ederek
 * Dao içerisindeki fonksiyonlar ile LocalDataSource'de yer alan fonksiyonlar arasındaki bağlantıları kurarlar.
 *
 * Bu işlemleri yaparken;
 *
 * - Domain katmanındaki UseCase'lere veri iletirken Entitiy'i Model'e,
 * - UseCase'den aldığı veriyi Dao'e iletirken Model'i Entity'e çevirirler.
 *
 * ve bunun için Data katmanında ilgili feature (özellik) için oluşturulan Mapper'ları kullanırlar.
 */

class NotesLocalDataSourceImpl @Inject constructor(private val noteDao: NoteDao) : NotesLocalDataSource {

    override suspend fun getNotes(): MutableList<Note> {
        val allNotes = mutableListOf<Note>()
        noteDao.getAllNotes().onEach {
            allNotes.add(it.toNote())
        }
        return allNotes
    }

    override suspend fun insertNote(note: Note) {
        note.toNoteEntity().apply {
            val l = noteDao.insertNote(this)
            Log.i(TAG, "NotesLocalDataSourceImpl -> Insert L: $l")
        }
    }

    override suspend fun updateNote(note: Note, oldNote: Note) {
        Log.i(TAG, "updateNote note:${note} - oldNote: ${oldNote}")
        note.toNoteEntity().apply {
            noteDao.updateNote(this)
        }
    }
}