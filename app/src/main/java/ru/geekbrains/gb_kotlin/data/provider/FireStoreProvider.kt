package ru.geekbrains.gb_kotlin.data.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import ru.geekbrains.gb_kotlin.data.entity.Note
import ru.geekbrains.gb_kotlin.data.model.NoteResult

class FireStoreProvider : RemoteDataProvider {

    companion object {
        private const val NOTES_COLLECTION = "notes"
    }

    private val store: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val noteReference = store.collection(NOTES_COLLECTION)

    override fun subsrcibeToAllNotes() = MutableLiveData<NoteResult>().apply {
        noteReference.addSnapshotListener { snapshot, e ->
            e?.let {
                value = NoteResult.Error(it)
            } ?: let {
                snapshot?.let { it ->
                    value = NoteResult.Success(snapshot.map { it.toObject(Note::class.java) })
                }
            }
        }
    }

    override fun getNoteById(id: String) = MutableLiveData<NoteResult>().apply {
        noteReference.document(id).get()
            .addOnSuccessListener { it
                value = NoteResult.Success(it.toObject(Note::class.java))
            }.addOnFailureListener {
                value = NoteResult.Error(it)
            }
    }


    fun deleteNote(note: Note): LiveData<NoteResult>{
        val result = MutableLiveData<NoteResult>()
        noteReference.document(note.id).delete()
            .addOnSuccessListener {
                result.value = NoteResult.Success(note)
            }.addOnFailureListener {
                result.value = NoteResult.Error(it)
            }

        return result
    }

    override fun saveNote(note: Note)= MutableLiveData<NoteResult>().apply {
        noteReference.document(note.id).set(note)
            .addOnSuccessListener {
                value = NoteResult.Success(note)
            }.addOnFailureListener {
                value = NoteResult.Error(it)
            }
    }
}