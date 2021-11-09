package murat.cleanarchitecture.sample.presentation.ui.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
    private lateinit var frmAddNote: ViewGroup
    private lateinit var fabAdd: FloatingActionButton
    //private lateinit var noteTitle: TextInputLayout
    //private lateinit var noteContent: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUI()
        //getMockNotes()

    }

    override fun observeViewModel() {
        viewModel.fetchNotes()

        viewModel.mutableListNotes.observe(viewLifecycleOwner, {
            when (it) {
                is ResultData.Success -> {
                    if (it.data?.size!! > 0) {
                        notesAdapter.update(it.data)
                        binding.txtNoteListEmpty.visibility = View.GONE
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

    private fun setUI() {
        // Set presenter to binding
        binding.presenter = this

        //NotesAdapter
        notesAdapter = NotesAdapter(mutableListOf())

        // RecyclerView
        binding.rcyMain.apply {
            //layoutManager = GridLayoutManager(context, 2)
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = notesAdapter
        }

        frmAddNote = binding.frmAddNote
        fabAdd = binding.fabAdd


        fabAdd.setOnClickListener { _ ->
            //setMockNoteToDB()
            showNewNotePage()
        }

        binding.btnSaveNote.setOnClickListener {

            val title = binding.noteTitle.editText?.text.toString()
            val content = binding.noteContent.editText?.text.toString()

            if (title.isEmpty()) {
                binding.noteTitle.error = getString(R.string.title_is_not_empty)
            } else if (content.isEmpty()) {
                binding.noteTitle.error = null
                binding.noteContent.error = getString(R.string.content_is_not_empty)
            } else {
                binding.noteContent.error = null

                val newNote = Note(
                    id = 0,
                    title = title,
                    content = content,
                    color = null,
                    update_at = null
                )

                viewModel.insertNote(newNote).apply {
                    observeViewModel()
                    hideNewNotePage(null)
                }
            }
        }

        binding.noteTitle.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    binding.noteTitle.error = null
                } else {
                    binding.noteTitle.error = getString(R.string.title_is_not_empty)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.noteContent.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    binding.noteContent.error = null
                } else {
                    binding.noteContent.error = getString(R.string.content_is_not_empty)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun showNewNotePage() {
        frmAddNote.startAnimation(viewModel.bottomUp)
        frmAddNote.visibility = View.VISIBLE

        fabAdd.startAnimation(viewModel.bottomDown)
        fabAdd.visibility = View.GONE
    }

    fun hideNewNotePage(view: View?) {
        frmAddNote.startAnimation(viewModel.bottomDown)
        frmAddNote.visibility = View.GONE

        fabAdd.startAnimation(viewModel.bottomUp)
        fabAdd.visibility = View.VISIBLE

        binding.noteTitle.editText?.text?.clear()
        binding.noteContent.editText?.text?.clear()
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
            hideNewNotePage(null)
        }
    }

    fun insertNoteFromUI(title: String, content: String) {
        val note = Note(
            id = 0,
            title = title,
            content = content,
            color = null,
            update_at = null
        )
        viewModel.insertNote(note).apply {
            observeViewModel()
            hideNewNotePage(null)
        }
    }

}