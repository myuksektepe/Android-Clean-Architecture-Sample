package murat.cleanarchitecture.sample.data.repository.notes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import murat.cleanarchitecture.sample.domain.model.Note
import murat.cleanarchitecture.sample.domain.model.ResultData
import murat.cleanarchitecture.sample.domain.repository.NotesRepository
import javax.inject.Inject

/**
 * Repository Implementatiton
 *
 * Repository'ler interface tipinde oldukları için Hilt'e inject edilemezler bu yüzden bir Repository'den kalıtılmış olan
 * RepositoryImplementation sınıflarını oluştururuz. Bu sınıflar local ya da network üzerinden gelen verileri işleyecekleri için
 * Data katmanı içerisinde yer alırlar ve ilgili DataSource'lere bağımlıdırlar.
 *
 * İlgili Repository'de yer alan fonksiyonlar override edilir ve veri kaynağı olan DataSource'ten getirilen veriler üzerinde
 * kontroller yapıldıktan sonra flow ile dönüşleri sağlanır.
 *
 * .
 * - Flow Nedir?
 *
 * Farklı işler yapan iki farklı iş parçası olduğunu düşünelim. Örneğin; Bir iş parçası veritabanından bir bilgi getiriyor,
 * diğer iş parçası ise bu bilgiyi alıp üzerinde bazı işlemler yapıyor. Eğer ikinci iş parçası veriyi teslim almaya
 * gittiği zaman ilk iş parçası henüz veriyi getirmemiş olursa burada concurrency (eş zamanlılık, tutarlılık) sorunu
 * oluşur.
 *
 * Bu sorunu ortadan kaldırmak için geliştirilen bir yöntem; channel (kanal) yöntemidir. Bu yöntem ile iki farklı iş
 * parçası birbiri ile iletişim halindedir ve örneğimizdeki ilk iş parçası görevini tamamlandığını ikinci iş parcasına
 * haber verebilir, böylece concurrency (eş zamanlılık, tutarlılık) sorunu ortadan kalkar.
 *
 * Fakat bu channel (kanal) çözümü default (varsayılan) olarak otomatik başlayan yani 'hot' biçiminde planlanmıştır.
 *
 * Kotlin'in sağladığı Flow ise bu channel (kanal) çözümünün collect edilene (derlenene, tetikletilene) kadar işleme
 * başlamayan 'cold' halidir.
 *
 */

class NotesRepositoryImpl @Inject constructor(private val localDataSource: NotesLocalDataSource) : NotesRepository {

    override fun insertNote(note: Note): Flow<ResultData<Unit>> = flow {
        emit(ResultData.Loading())
        try {
            emit(ResultData.Success(localDataSource.insertNote(note)))
        } catch (e: Exception) {
            emit(ResultData.Failed(errorMessage = e.message))
        }
    }

    override fun getNotes(): Flow<ResultData<MutableList<Note>>> = flow {
        emit(ResultData.Loading())
        try {
            emit(ResultData.Success(localDataSource.getNotes()))
        } catch (e: Exception) {
            emit(ResultData.Failed(errorMessage = e.message))
        }
    }

    override fun getNote(id: Int): Flow<ResultData<Note>> = flow {
        emit(ResultData.Loading())
        try {
            emit(ResultData.Success(localDataSource.getNote(id)))
        } catch (e: Exception) {
            emit(ResultData.Failed(errorMessage = e.message))
        }
    }

    override fun updateNote(note: Note, oldNote: Note): Flow<ResultData<Unit>> = flow {
        emit(ResultData.Loading())
        try {
            emit(ResultData.Success(localDataSource.updateNote(note, oldNote)))
        } catch (e: Exception) {
            emit(ResultData.Failed(errorMessage = e.message))
        }
    }
}