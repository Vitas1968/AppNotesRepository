package ru.geekbrains.gb_kotlin.ui.note

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import ru.geekbrains.gb_kotlin.data.NotesRepository
import ru.geekbrains.gb_kotlin.data.entity.Note
import ru.geekbrains.gb_kotlin.data.model.NoteResult
import ru.geekbrains.gb_kotlin.ui.base.BaseViewModel

class NoteViewModel(private val notesRepository: NotesRepository): BaseViewModel<Note?, NoteViewState>() {
    init {
        viewStateLiveData.value = NoteViewState()
    }

    private var pendingNote: Note? = null

    fun save(note: Note) {
        pendingNote = note
    }

    fun loadNote(noteId: String) {
        notesRepository.getNoteById(noteId).observeForever(object : Observer<NoteResult> {
            override fun onChanged(t: NoteResult?) {
                t ?: return
                when (t) {
                    is NoteResult.Success<*> -> {
                        viewStateLiveData.value = NoteViewState(note = t.data as Note)
                    }
                    is NoteResult.Error -> {
                        viewStateLiveData.value = NoteViewState(error = t.error)
                    }
                }
            }
        })
    }

    override fun onCleared() {
        pendingNote?.let {
            notesRepository.saveNote(it)
        }
    }

}