package ru.geekbrains.gb_kotlin.ui.main

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.item_note.view.*
import ru.geekbrains.gb_kotlin.R
import ru.geekbrains.gb_kotlin.common.getColorInt
import ru.geekbrains.gb_kotlin.data.entity.Note

class NotesRVAdapter(val onItemViewClick : ((note: Note) -> Unit)? = null) : RecyclerView.Adapter<NotesRVAdapter.ViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        )

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(vh: ViewHolder, pos: Int) = vh.bind(notes[pos])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(note: Note) = with(itemView) {
            tv_title.text = note.title
            tv_text.text = note.text
            (this as CardView).setCardBackgroundColor(note.color.getColorInt(context))
            itemView.setOnClickListener {
                onItemViewClick?.invoke(note)
            }

        }
    }
}