package murat.cleanarchitecture.sample.presentation.ui.viewmodel

import android.app.Application
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import murat.cleanarchitecture.sample.TAG
import murat.cleanarchitecture.sample.domain.model.Note
import murat.cleanarchitecture.sample.domain.model.ResultData
import murat.cleanarchitecture.sample.domain.usecase.GetNotesUseCase
import murat.cleanarchitecture.sample.domain.usecase.InsertNoteUseCase
import murat.cleanarchitecture.sample.presentation.common.base.BaseViewModel
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
    private val getNotesUseCase: GetNotesUseCase,
    private val insertNoteUseCase: InsertNoteUseCase
) : BaseViewModel() {

    private var _mutableListNotes = MutableLiveData<ResultData<MutableList<Note>>>()
    val mutableListNotes: LiveData<ResultData<MutableList<Note>>>
        get() = _mutableListNotes


    private var _insertNote = MutableLiveData<ResultData<Unit>>()
    val insertNote: LiveData<ResultData<Unit>>
        get() = _insertNote


    val bottomUp: Animation = AnimationUtils.loadAnimation(application.applicationContext, R.anim.bottom_up)
    val bottomDown: Animation = AnimationUtils.loadAnimation(application.applicationContext, R.anim.bottom_down)


    fun fetchNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            getNotesUseCase.invoke().collect {
                handleTask(it) {
                    _mutableListNotes.postValue(it)
                }
            }
        }
    }

    fun insertNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            insertNoteUseCase.invoke(note).collect {
                handleTask(it) {
                    _insertNote.postValue(it)
                }
                fetchNotes()
            }
        }
    }

}