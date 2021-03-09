package ru.geekbrains.gb_kotlin.ui.note

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.geekbrains.gb_kotlin.data.NotesRepository
import ru.geekbrains.gb_kotlin.data.entity.Note
import ru.geekbrains.gb_kotlin.ui.base.BaseViewModel

class NoteViewModel(private val notesRepository: NotesRepository) : BaseViewModel<NoteData>() {


    @ExperimentalCoroutinesApi
    private val pendingNote: Note?
        get() = getViewState().poll()?.note

    @ExperimentalCoroutinesApi
    fun save(note: Note) {
        setData(NoteData(note = note))
    }

    @ExperimentalCoroutinesApi
    fun loadNote(noteId: String) {
        launch {
            try {
                notesRepository.getNoteById(noteId).let {
                    setData(NoteData(note = it))
                }
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun deleteNote() {
        pendingNote?.let { note ->
            launch {
                try {
                    notesRepository.deleteNote(note.id)
                    setData(NoteData(isDeleted = true))
                } catch (e: Throwable) {
                    setError(e)
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    @VisibleForTesting
    public override fun onCleared() {
        launch {
            pendingNote?.let {
                try {
                    notesRepository.saveNote(it)
                } catch (e: Throwable) {
                    setError(e)
                }
            }
            super.onCleared()
        }
    }
}