package murat.cleanarchitecture.sample.presentation.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import murat.cleanarchitecture.sample.domain.model.Note
import murat.cleanarchitecture.sample.domain.model.ResultData
import murat.cleanarchitecture.sample.presentation.common.base.BaseActivity
import murat.cleanarchitecture.sample.presentation.ui.adapter.NotesAdapter
import murat.cleanarchitecture.sample.presentation.ui.viewmodel.MainAcitivityViewModel
import sample.R
import sample.databinding.ActivityMainBinding
import java.util.*

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
    private lateinit var notesAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUI()
        //getMockNotes()

        binding.fabAdd.setOnClickListener { view ->
            /*
            Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
             */
            setMockNoteToDB()
        }
    }

    private fun setUI() {
        notesAdapter = NotesAdapter(mutableListOf())

        binding.rcyMain.apply {
            //layoutManager = GridLayoutManager(context, 2)
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = notesAdapter
        }
    }

    override fun observeViewModel() {
        viewModel.fetchNotes()

        viewModel.mutableListNotes.observe(viewLifecycleOwner, {
            when (it) {
                is ResultData.Success -> {
                    if (it.data?.size!! > 0) {
                        notesAdapter.update(it.data)
                    } else {
                        binding.txtNoteListEmpty.visibility = View.VISIBLE
                    }
                }
                is ResultData.Loading -> {

                }
                is ResultData.Failed -> {

                }
            }
        })
    }

    private fun getMockNotes() {
        val mockData = mutableListOf(
            Note(1, "Note 1 için uzuuun bir başlık denemesi", "Even better if this is wrapped in a function named safe_substring or substring_safe, like paxdiablo's answer, so that usage is easier to read / intent more obvious", "#004400", Date().time, null),
            Note(2, "Note 2", "Opinion: while this solution is \"neat\", I think it is actually less readable than a solution that uses if / else in the obvious way. If the reader hasn't seen this trick, he/she has to think harder to understand the code.", null, Date().time, null),
            Note(3, "Note 3", "Zuppa!", "#B4D%44", Date().time, null),
            Note(4, "Note 4", "It doesn't answer the question, but regardless it still provides the solution. If the OP is able to understand, I think this is a better solution", null, Date().time, null),
        )
        notesAdapter.update(mockData)
    }

    private fun setMockNoteToDB() {
        val randomID = (0..1000).random()
        val mockNote = Note(
            randomID,
            "$randomID. Title",
            "Lo ipsum dolor sit amet, consectetur adipiscing elit. Phasellus iaculis eros lorem, nec finibus elit venenatis a. Lo ipsum dolor sit amet, consectetur adipiscing elit.\n\nPhasellus iaculis eros lorem, nec finibus elit venenatis a.",
            randomColorHex(),
            Date().time,
            null
        )
        viewModel.insertNote(mockNote).apply {
            observeViewModel()
        }
    }
}