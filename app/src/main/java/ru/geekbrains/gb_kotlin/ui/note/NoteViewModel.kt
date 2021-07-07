package ru.geekbrains.gb_kotlin.ui.note

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.launch
import ru.geekbrains.gb_kotlin.data.NotesRepository
import ru.geekbrains.gb_kotlin.data.entity.Note
import ru.geekbrains.gb_kotlin.data.model.NoteResult
import ru.geekbrains.gb_kotlin.ui.base.BaseViewModel

class NoteViewModel(private val notesRepository: NotesRepository): BaseViewModel< NoteData>() {


    private val pendingNote: Note?
        get() = getViewState().poll()?.note

    fun save(note: Note) {
        setData(NoteData(note = note))
    }

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

    @VisibleForTesting
    override public fun onCleared() {
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