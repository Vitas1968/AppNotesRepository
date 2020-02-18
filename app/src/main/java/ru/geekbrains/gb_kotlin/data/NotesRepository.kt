package ru.geekbrains.gb_kotlin.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.geekbrains.gb_kotlin.data.entity.Note
import ru.geekbrains.gb_kotlin.data.provider.FireStoreProvider
import ru.geekbrains.gb_kotlin.data.provider.RemoteDataProvider
import java.util.*

class NotesRepository(val remoteProvider: RemoteDataProvider) {

    fun getNotes() = remoteProvider.subsrcibeToAllNotes()
    fun saveNote(note: Note) = remoteProvider.saveNote(note)
    fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    fun getCurrentUser() = remoteProvider.getCurrentUser()
}