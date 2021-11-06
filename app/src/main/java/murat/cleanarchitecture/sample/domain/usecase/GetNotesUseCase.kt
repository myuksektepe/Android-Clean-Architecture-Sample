package murat.cleanarchitecture.sample.domain.usecase

import kotlinx.coroutines.flow.Flow
import murat.cleanarchitecture.sample.domain.model.Note
import murat.cleanarchitecture.sample.domain.model.ResultData
import murat.cleanarchitecture.sample.domain.repository.NotesRepository
import javax.inject.Inject

/*

UseCase

Kullanıcının yaptığı her bir işlem için UseCase oluşturulur. Örneğin bütün kayıtların listelenmesi ya da sadece seçilen kaydın detaylarının
getirilmesi ya da yeni kayıt eklenmesi vb. gibi.

UseCase'ler ilgili Feature için üretilmiş olan Repository'leri çağırarak bu Repository içindeki kendiyle alakalı olan fonksiyondan veri alan,
-genellikle invoke isminde- bir operator fonksiyon barındırır.

TODO Operator Fonksiyon Nedir?

 */

class GetNotesUseCase @Inject constructor(private val respository: NotesRepository) {
    operator fun invoke(): Flow<ResultData<MutableList<Note>>> = respository.getNotes()
}