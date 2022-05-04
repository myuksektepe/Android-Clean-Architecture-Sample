package murat.cleanarchitecture.sample.feature_note.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import murat.cleanarchitecture.sample.feature_note.domain.model.Note
import sample.databinding.NoteListItemBinding

/**
 * Adapter
 *
 * RecyclerView, ListView gibi kendi içinde Adapter yapısı barındıran sınıfların türetilir.

 * Kendine gelen liste şeklindeki veriyi döngüye sokar, döngüye giren her bir item (eleman) için çeşitli kontroller ve tanımlamalar yapılabilir.
 * Belirtilen layout (tasarım dosyası) manipüle edilerek UI (kullanıcı arayüzü) güncellenir.
 *
 **/

class NotesAdapter(private val notes: MutableList<Note>) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    internal var onItemSelected: (position: Int, item: Note) -> Unit = { _, _ -> }

    override fun getItemCount(): Int = notes.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        notes[position].let {
            holder.bind(
                it,
                position,
                onItemSelected
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(newNotes: MutableList<Note>) {
        this.notes.clear()
        this.notes.addAll(newNotes)
        notifyDataSetChanged()
    }

    class NoteViewHolder(private val binding: NoteListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(
            note: Note,
            position: Int,
            onItemSelected: (Int, Note) -> Unit
        ) {

            // Title
            val title = note.title
            val t_len = title.length
            val t_maxCharLen = (23..67).random()
            val t_summary: String = title.substring(0, Math.min(t_len, t_maxCharLen))
            binding.txtNoteItemTitle.text = if (t_len >= t_maxCharLen) {
                "$t_summary..."
            } else {
                t_summary
            }


            // Content
            val content = note.content
            val c_len = content.length
            val c_maxCharLen = (57..237).random()
            val c_summary: String = content.substring(0, Math.min(c_len, c_maxCharLen))
            binding.txtNoteItemContent.text = if (c_len >= c_maxCharLen) {
                "${c_summary.trim()}..."
            } else {
                c_summary
            }


            // Color
            if (note.color.isNullOrBlank()) {
                binding.vwNoteItemColor.visibility = View.GONE
            }


            // Item Selected
            binding.cardNoteItem.setOnClickListener {
                onItemSelected.invoke(position, note)
            }

        }
    }

}