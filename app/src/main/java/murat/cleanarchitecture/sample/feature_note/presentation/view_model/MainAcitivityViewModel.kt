package murat.cleanarchitecture.sample.feature_note.presentation.view_model

import android.app.Application
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import murat.cleanarchitecture.sample.feature_note.domain.model.Note
import murat.cleanarchitecture.sample.feature_note.domain.use_case.NoteUseCases
import murat.cleanarchitecture.sample.util.ResultState
import murat.cleanarchitecture.sample.util.base.BaseViewModel
import sample.R
import javax.inject.Inject

/**

ViewModel

Kendisini çağıracak olan Activity veya Fragment için gerekli olan değişken ve fonksiyonları barındırır.

Clean Architecture prensibine göre ViewModel'ler içerisinde Bussines Logic işlemleri barındırmazlar bu sebeple
bu değişken ve fonksiyonların içerisini doldurmak için Bussines Logic işlerimizi yaptığımız
Domain katmanındaki UseCase sınıflarına bağımlılık oluştururlar.

Bütün amaçları kendisine gelen verileri doğru şekilde ilgili UI'a aktarmaktır.

Hilt için @HiltViewModel ile annotate edilirler.

 */

@HiltViewModel
class MainAcitivityViewModel @Inject constructor(
    private val application: Application,
    private val noteUseCases: NoteUseCases,
) : BaseViewModel() {

    private var _mutableListNotes = MutableLiveData<ResultState<MutableList<Note>>>()
    val mutableListNotes: LiveData<ResultState<MutableList<Note>>>
        get() = _mutableListNotes


    private var _insertNote = MutableLiveData<ResultState<Unit>>()
    val insertNote: LiveData<ResultState<Unit>>
        get() = _insertNote


    private var _getNote = MutableLiveData<ResultState<Note>>()
    val getNote: LiveData<ResultState<Note>>
        get() = _getNote


    val bottomUp: Animation = AnimationUtils.loadAnimation(application.applicationContext, R.anim.bottom_up)
    val bottomDown: Animation = AnimationUtils.loadAnimation(application.applicationContext, R.anim.bottom_down)

    fun getAllNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            noteUseCases.getNotesUseCase.invoke().collect {
                handleTask(it) {
                    _mutableListNotes.postValue(it)
                }
            }
        }
    }

    fun insertNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteUseCases.insertNoteUseCase.invoke(note).collect {
                handleTask(it) {
                    _insertNote.postValue(it)
                }
                getAllNotes()
            }
        }
    }

    fun getNoteByID(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            noteUseCases.getNoteByIdUseCase.invoke(id).collect {
                handleTask(it) {
                    _getNote.postValue(it)
                }
            }
        }
    }

}