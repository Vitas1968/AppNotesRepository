package ru.geekbrains.gb_kotlin.data.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import ru.geekbrains.gb_kotlin.data.entity.Note
import ru.geekbrains.gb_kotlin.data.model.NoteResult

class FireStoreProvider : RemoteDataProvider{

    companion object {
        private const val NOTES_COLLECTION = "notes"
    }

    private val store: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val noteReference = store.collection(NOTES_COLLECTION)

    override fun subsrcibeToAllNotes(): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        noteReference.addSnapshotListener { snapshot, e ->
            e?.let {
                result.value = NoteResult.Error(it)
            } ?: let {
                snapshot?.let { it ->
                    val notes = mutableListOf<Note>()
                    for (doc: QueryDocumentSnapshot in it) {
                        notes.add(doc.toObject(Note::class.java))
                    }
                   // it.forEach { notes.add(it.toObject(Note::class.java)) }
                    result.value = NoteResult.Success(notes)
                }
            }
        }
        return result
    }

    override fun getNoteById(id: String): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        noteReference.document(id).get()
            .addOnSuccessListener {it
                result.value = NoteResult.Success(it.toObject(Note::class.java))
            }.addOnFailureListener {
                result.value = NoteResult.Error(it)
            }

        return result
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

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        noteReference.document(note.id).set(note)
            .addOnSuccessListener {
                result.value = NoteResult.Success(note)
            }.addOnFailureListener {
                result.value = NoteResult.Error(it)
            }

        return result
    }
}