package murat.cleanarchitecture.sample.data.repository.notes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import murat.cleanarchitecture.sample.domain.model.Note
import murat.cleanarchitecture.sample.domain.model.ResultData
import murat.cleanarchitecture.sample.domain.repository.NotesRepository
import javax.inject.Inject

/*
RepositoryImplementatiton

Repository'ler interface tipinde oldukları Hilt'e inject edilemezler bu yüzden bir Repository'den kalıtılmış olan
RepositoryImplementation sınıflarını oluştururuz. Bu sınıflar local ya da network üzerinden gelen verileri işleyecekleri için
Data katmanı içerisinde yer alırlar ve ilgili DataSource'lere bağımlıdırlar.

İlgili Repository'de yer alan fonksiyonlar override edilir ve veri kaynağı olan DataSource'ten getirilen veriler üzerinde
kontroller yapıldıktan sonra flow ile dönüşleri sağlanır.

TODO Flow nedir?

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

    override fun updateNote(note: Note, oldNote: Note): Flow<ResultData<Unit>> = flow {
        emit(ResultData.Loading())
        try {
            emit(ResultData.Success(localDataSource.updateNote(note, oldNote)))
        } catch (e: Exception) {
            emit(ResultData.Failed(errorMessage = e.message))
        }
    }
}