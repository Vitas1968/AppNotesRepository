package ru.geekbrains.gb_kotlin.ui.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_note.*
import org.jetbrains.anko.alert
import org.koin.android.viewmodel.ext.android.viewModel
import ru.geekbrains.gb_kotlin.R
import ru.geekbrains.gb_kotlin.data.entity.Note
import ru.geekbrains.gb_kotlin.ui.base.BaseActivity
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : BaseActivity<NoteViewState.Data, NoteViewState>() {
    companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"
        private const val DATE_TIME_FORMAT = "dd.MM.yy HH:mm"

        fun start(context: Context, noteId: String? = null) {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_NOTE, noteId)
            context.startActivity(intent)
        }
    }

    override val layoutRes = R.layout.activity_note
    override val model: NoteViewModel by viewModel()
    private var note: Note? = null

    val textChahgeListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            saveNote()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val noteId = intent.getStringExtra(EXTRA_NOTE)

        noteId?.let {
            model.loadNote(it)
        } ?: let {
            supportActionBar?.title = getString(R.string.new_note_title)
        }
    }

    override fun renderData(data: NoteViewState.Data) {
        if (data.isDeleted) finish()
        this.note = data.note
        initView()
    }

    fun initView() {
        note?.let { note ->
            et_title.setText(note.title)
            et_body.setText(note.text)
            val color = when (note.color) {
                Note.Color.WHITE -> R.color.white
                Note.Color.YELLOW -> R.color.yellow
                Note.Color.GREEN -> R.color.green
                Note.Color.BLUE -> R.color.blue
                Note.Color.RED -> R.color.red
                Note.Color.VIOLET -> R.color.violet
                Note.Color.PINK -> R.color.pink
            }

            toolbar.setBackgroundColor(ContextCompat.getColor(this, color))
        }

        et_title.addTextChangedListener(textChahgeListener)
        et_body.addTextChangedListener(textChahgeListener)
    }

    fun saveNote() {
        if (et_title.text == null || et_title.text!!.length < 3) return

        note = note?.copy(
            title = et_title.text.toString(),
            text = et_body.text.toString(),
            lastChanged = Date()
        ) ?: Note(
            UUID.randomUUID().toString(),
            et_title.text.toString(),
            et_body.text.toString(),
            Note.Color.values()[Random().nextInt(7)]
        )

        note?.let { model.save(it) }
    }

    private fun deleteNote() {
        alert {
            messageResource = R.string.note_delete_message
            negativeButton(R.string.note_delete_cancel) { dialog -> dialog.dismiss() }
            positiveButton(R.string.note_delete_ok) { model.deleteNote() }
        }.show()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed();
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
