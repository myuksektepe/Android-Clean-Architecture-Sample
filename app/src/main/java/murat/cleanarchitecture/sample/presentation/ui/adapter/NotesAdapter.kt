package murat.cleanarchitecture.sample.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import murat.cleanarchitecture.sample.domain.model.Note
import sample.R

/**
* Adapter
*
* RecyclerView, ListView gibi kendi içinde Adapter yapısı barındıran sınıfların türetilir.

* Kendine gelen liste şeklindeki veriyi döngüye sokar, döngüye giren her bir item (eleman) için çeşitli kontroller ve tanımlamalar yapılabilir.
* Belirtilen layout (tasarım dosyası) manipüle edilerek UI (kullanıcı arayüzü) güncellenir.
*
 **/

class NotesAdapter(private val notes: MutableList<Note>) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {
    override fun getItemCount(): Int = notes.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_list_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        notes[position].let {
            holder.bind(it)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(newNotes: MutableList<Note>) {
        this.notes.clear()
        this.notes.addAll(newNotes)
        notifyDataSetChanged()
    }

    class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val noteTitle: TextView = view.findViewById(R.id.note_item_title)
        val noteSummary: TextView = view.findViewById(R.id.note_item_summary)

        @SuppressLint("SetTextI18n")
        fun bind(note: Note) {

            // Title
            val title = note.title
            val t_len = title.length
            val t_maxCharLen = (23..67).random()
            val t_summary: String = title.substring(0, Math.min(t_len, t_maxCharLen))
            noteTitle.text = if (t_len >= t_maxCharLen) {
                "$t_summary..."
            } else {
                t_summary
            }


            // Content
            val content = note.content
            val c_len = content.length
            val c_maxCharLen = (57..237).random()
            val c_summary: String = content.substring(0, Math.min(c_len, c_maxCharLen))
            noteSummary.text = if (c_len >= c_maxCharLen) {
                "$c_summary..."
            } else {
                c_summary
            }


        }
    }

}