package ru.geekbrains.gb_kotlin.data.provider

import androidx.lifecycle.LiveData
import kotlinx.coroutines.channels.ReceiveChannel
import ru.geekbrains.gb_kotlin.data.entity.Note
import ru.geekbrains.gb_kotlin.data.entity.User
import ru.geekbrains.gb_kotlin.data.model.NoteResult

interface RemoteDataProvider {
    fun subsrcibeToAllNotes(): ReceiveChannel<NoteResult>
    suspend fun getNoteById(id: String): Note
    suspend fun saveNote(note: Note): Note
    suspend fun getCurrentUser(): User?
    suspend fun deleteNote(noteId: String)
}