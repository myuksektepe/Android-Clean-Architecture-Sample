package murat.cleanarchitecture.sample.domain.repository

import kotlinx.coroutines.flow.Flow
import murat.cleanarchitecture.sample.domain.model.Note
import murat.cleanarchitecture.sample.domain.model.ResultData

/**
 *
 * Repository
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
    fun insertNote(note: Note): Flow<ResultData<Unit>>
    fun getNotes(): Flow<ResultData<MutableList<Note>>>
    fun updateNote(note: Note, oldNote: Note): Flow<ResultData<Unit>>
}