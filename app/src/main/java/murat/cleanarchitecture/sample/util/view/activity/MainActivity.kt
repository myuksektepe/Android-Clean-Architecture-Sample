package murat.cleanarchitecture.sample.util.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import dagger.hilt.android.AndroidEntryPoint
import murat.cleanarchitecture.sample.feature_note.presentation.view.fragment.NotesFragment
import murat.cleanarchitecture.sample.util.base.BaseActivity
import murat.cleanarchitecture.sample.util.view_model.MainAcitivityViewModel
import sample.R
import sample.databinding.ActivityMainBinding


/**
 *
 * Activity ve Fragment
 *
 * Kendisi için üretilmiş ViewModel'lere bağlıdır. Bu ViewModeller içerisindeki
 * fonksiyonları tetikleterek kendisine geri döndürülen veriler ile UI elementlerini manipüle ederler.
 *
 * Bu işlemler sırasında listeleme yapılacak ise (Örn: RecyclerView içerisinde notların listelenmesi gibi) Adapter kullanabilirler.
 *
 * Kendisine ayrılmış Layout'lara (yani XML ekran tasarım dosyalarına) ulaşırken ViewBinding veya DataBindig kullanabilirler.


 * ViewBinding ve DataBindig Nedir? Farkları Nelerdir?
 *
 * ViewBinding veya DataBindig; UI'daki bir elemana ulaşırken findViewById gibi belleği yoran, ilgili elemanı bulmak için UI'a sürekli istekte
 * bulunan yapı yerine ilgili UI'daki tüm elemanların bir cache bellekte tutulup hızlıca ulaşılmasını sağlayan bir yapıdır.
 *
 * * ViewBinding ile sadece logic'ten ui elementlerini yönetebilirken,
 * * DataBinding ise hem logic'ten ui'i  hem de ui'dan logic'i yönetebilmek üzere iki yönlü çalışabilir.
 *
 */

@AndroidEntryPoint
class MainActivity : BaseActivity<MainAcitivityViewModel, ActivityMainBinding>() {

    override val layoutRes: Int = R.layout.activity_main
    override val viewModel: MainAcitivityViewModel by viewModels()
    override var viewLifecycleOwner: LifecycleOwner = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUI()
    }

    override fun initBinding() {
        super.initBinding()
        binding.presenter = this
        binding.viewModel = viewModel
    }

    override fun observeViewModel() {
    }

    private fun setUI() {
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, NotesFragment()).commit()
    }
}