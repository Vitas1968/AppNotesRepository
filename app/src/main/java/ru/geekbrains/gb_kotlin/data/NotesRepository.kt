package ru.geekbrains.gb_kotlin.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.geekbrains.gb_kotlin.data.entity.Note
import ru.geekbrains.gb_kotlin.data.provider.FireStoreProvider
import ru.geekbrains.gb_kotlin.data.provider.RemoteDataProvider
import java.util.*

class NotesRepository(val remoteProvider: RemoteDataProvider) {

    fun getNotes() = remoteProvider.subsrcibeToAllNotes()
    suspend fun  saveNote(note: Note) = remoteProvider.saveNote(note)
    suspend fun  getNoteById(id: String) = remoteProvider.getNoteById(id)
    suspend fun  getCurrentUser() = remoteProvider.getCurrentUser()
    suspend fun deleteNote(id: String) = remoteProvider.deleteNote(id)
}