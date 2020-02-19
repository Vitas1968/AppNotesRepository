package ru.geekbrains.gb_kotlin.data.provider

import androidx.lifecycle.LiveData
import ru.geekbrains.gb_kotlin.data.entity.Note
import ru.geekbrains.gb_kotlin.data.entity.User
import ru.geekbrains.gb_kotlin.data.model.NoteResult

interface RemoteDataProvider {
    fun subsrcibeToAllNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note): LiveData<NoteResult>
    fun getCurrentUser(): LiveData<User?>
    fun deleteNote(noteId: String): LiveData<NoteResult>
}