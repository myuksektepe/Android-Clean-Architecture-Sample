package murat.cleanarchitecture.sample.feature_note.domain.repository

import kotlinx.coroutines.flow.Flow
import murat.cleanarchitecture.sample.feature_note.domain.model.Note
import murat.cleanarchitecture.sample.util.model.ResultState

/**
 * Feature -> Repository
 *
 * İçerisinde bir Feature (özellik) için kullanılacak olan tüm fonksiyonları barındıran interface'lerdir.
 * Örneğin: Not işlemleri için kaydet, listele, güncelle vb. gibi.
 *
 * Repository'ler interface tipinde oldukları için Hilt'e inject edilemezler.
 * Bu yüzden Repository'lerden türetilmiş olan Implementation'lara ihtiyaç duyarız.
 *
 * (Bknz: https://developer.android.com/training/dependency-injection/hilt-android#hilt-modules)
 *
 * Local (yerel) veya Remote (uzak) veri kaynakları üzerinde kayıt ve güncelleme gibi bir veya
 * birden fazla satırın ekleneceği işlem yapacak olan fonksiyonlar [Unit] (birim)
 * tipinden baz alınmış [Flow] (akış)'lar geri döndürürken, hiç bir satırın etkilenmeyeceği,
 * kayıt çekme gibi işlemler hangi türde veri döndürelecek ise onun için oluşturulan Model'den
 * baz alınan [Flow] (akış) döndürürler.
 *
 */

interface NotesRepository {
    fun insertNote(note: Note): Flow<ResultState<Unit>>
    fun getNotes(): Flow<ResultState<MutableList<Note>>>
    fun getNote(id: Int): Flow<ResultState<Note>>
    fun updateNote(note: Note, oldNote: Note): Flow<ResultState<Unit>>
    fun deleteNote(note: Note): Flow<ResultState<Unit>>
}