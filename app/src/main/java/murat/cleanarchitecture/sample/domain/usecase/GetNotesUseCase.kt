package murat.cleanarchitecture.sample.domain.usecase

import kotlinx.coroutines.flow.Flow
import murat.cleanarchitecture.sample.domain.model.Note
import murat.cleanarchitecture.sample.domain.model.ResultData
import murat.cleanarchitecture.sample.domain.repository.NotesRepository
import javax.inject.Inject

/**
 * UseCase
 *
 * Uygulamanın içerdiği her bir Feature (özellik)'de kullanıcı için yapılan ya da kullacının bizzat yaptığı
 * her bir işlem için UseCase oluşturulur.
 *
 * Örneğin: Bütün notların listelenmesi, seçilen notun detaylarının getirilmesi ya da yeni not eklenmesi vb. gibi.
 *
 * UseCase'ler ilgili Feature (özellik) için üretilmiş olan Repository'leri çağırarak bu Repository içindeki kendiyle
 * alakalı olan fonksiyondan veri alan, -genellikle invoke (çağırmak) isminde- bir fonksiyon barındırır.
 *
 **/

class GetNotesUseCase @Inject constructor(private val respository: NotesRepository) {
    fun invoke(): Flow<ResultData<MutableList<Note>>> = respository.getNotes()
}