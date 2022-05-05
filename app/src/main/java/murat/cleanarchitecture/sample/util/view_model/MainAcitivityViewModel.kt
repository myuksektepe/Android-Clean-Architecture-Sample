package murat.cleanarchitecture.sample.util.view_model

import android.app.Application
import dagger.hilt.android.lifecycle.HiltViewModel
import murat.cleanarchitecture.sample.feature_note.domain.use_case.NoteUseCases
import murat.cleanarchitecture.sample.util.base.BaseViewModel
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
) : BaseViewModel()