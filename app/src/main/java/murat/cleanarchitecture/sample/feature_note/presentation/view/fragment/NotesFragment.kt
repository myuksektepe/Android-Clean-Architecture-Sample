package murat.cleanarchitecture.sample.feature_note.presentation.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import murat.cleanarchitecture.sample.feature_note.domain.model.Note
import murat.cleanarchitecture.sample.feature_note.presentation.adapter.NotesAdapter
import murat.cleanarchitecture.sample.feature_note.presentation.view_model.NotesViewModel
import murat.cleanarchitecture.sample.util.base.BaseFragment
import murat.cleanarchitecture.sample.util.extensions.hideKeyboard
import murat.cleanarchitecture.sample.util.extensions.randomColorHex
import murat.cleanarchitecture.sample.util.model.ResultState
import sample.R
import sample.databinding.FragmentNotesBinding
import java.util.*

@AndroidEntryPoint
class NotesFragment : BaseFragment<NotesViewModel, FragmentNotesBinding>() {

    override val layoutRes: Int = R.layout.fragment_notes
    override val viewModel: NotesViewModel by viewModels()

    private lateinit var notesAdapter: NotesAdapter
    private lateinit var frmAddNote: ViewGroup
    private lateinit var fabAdd: FloatingActionButton

    override fun initBinding() {
        super.initBinding()
        binding.presenter = this
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
        viewModel.getAllNotes()
    }

    override fun observeViewModel() {
        viewModel.getNotes.observe(viewLifecycleOwner) {
            when (it) {
                is ResultState.SUCCESS -> {
                    if (it.data?.size!! > 0) {
                        notesAdapter.update(it.data)
                        binding.txtNoteListEmpty.visibility = View.GONE
                    } else {
                        binding.txtNoteListEmpty.visibility = View.VISIBLE
                    }
                }
                is ResultState.LOADING -> {

                }
                is ResultState.FAIL -> {

                }
            }
        }
    }

    private fun setUI() {
        notesAdapter = NotesAdapter(mutableListOf())

        binding.rcyMain.apply {
            //layoutManager = GridLayoutManager(context, 2)
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = notesAdapter
            notesAdapter.onItemSelected = { _, item ->
                viewModel.getNoteByID(item.id)
                viewModel.getNote.observe(viewLifecycleOwner) {
                    when (it) {
                        is ResultState.SUCCESS -> {
                            binding.noteTitle.editText?.setText(it.data!!.title)
                            binding.noteContent.editText?.setText(it.data!!.content)
                            showNewNotePage()
                        }
                    }
                }
            }
        }

        frmAddNote = binding.frmAddNote
        fabAdd = binding.fabAdd
        fabAdd.setOnClickListener { _ ->
            //setMockNoteToDB()
            showNewNotePage()
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
        binding.noteTitle.editText?.text?.clear()
        binding.noteContent.editText?.text?.clear()

        frmAddNote.startAnimation(viewModel.bottomUp)
        frmAddNote.visibility = View.VISIBLE

        fabAdd.startAnimation(viewModel.bottomDown)
        fabAdd.visibility = View.GONE
    }

    fun hideNewNotePage() {
        frmAddNote.startAnimation(viewModel.bottomDown)
        frmAddNote.visibility = View.GONE

        fabAdd.startAnimation(viewModel.bottomUp)
        fabAdd.visibility = View.VISIBLE

        binding.noteTitle.error = null
        binding.noteContent.error = null

        binding.noteContent.hideKeyboard()
    }

    fun insertNoteFromUI(title: String, content: String) {
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
                hideNewNotePage()
            }
        }
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
            hideNewNotePage()
        }
    }
}