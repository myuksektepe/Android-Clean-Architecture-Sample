package murat.cleanarchitecture.sample.data.repository.notes

import android.util.Log
import murat.cleanarchitecture.sample.TAG
import murat.cleanarchitecture.sample.data.db.room.note.NoteDao
import murat.cleanarchitecture.sample.data.mapper.toNote
import murat.cleanarchitecture.sample.data.mapper.toNoteEntity
import murat.cleanarchitecture.sample.domain.model.Note
import javax.inject.Inject

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
            noteDao.insertNote(this)
        }
    }

    override suspend fun updateNote(note: Note, oldNote: Note) {
        Log.i(TAG, "updateNote note:${note} - oldNote: ${oldNote}")
        note.toNoteEntity().apply {
            noteDao.updateNote(this)
        }
    }
}