package ru.geekbrains.gb_kotlin.data.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import ru.geekbrains.gb_kotlin.data.entity.Note
import ru.geekbrains.gb_kotlin.data.entity.User
import ru.geekbrains.gb_kotlin.data.errors.NoAuthException
import ru.geekbrains.gb_kotlin.data.model.NoteResult

class FireStoreProvider : RemoteDataProvider {

    companion object {
        private const val NOTES_COLLECTION = "notes"
        private const val USER_COLLECTION = "users"
    }

    private val store: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser
    private val userNotesCollection: CollectionReference
        get() = currentUser?.let {                                                                                                                                                                                                                                                                                                                                      //Я копипастил код с урока и не заметил эту надпись
            store.collection(USER_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
        } ?: throw NoAuthException()


    override fun getCurrentUser() = MutableLiveData<User?>().apply {
        value = currentUser?.let { firebaseUser ->
            User(firebaseUser.displayName ?: "", firebaseUser.email ?: "")
        }
    }

    override fun subsrcibeToAllNotes() = MutableLiveData<NoteResult>().apply {
        try{
        userNotesCollection.addSnapshotListener { snapshot, e ->
            e?.let {
                throw it
            } ?: let {
                snapshot?.let { snapshot ->
                    value = NoteResult.Success(snapshot.map { it.toObject(Note::class.java) })
                }
            }
        }
        } catch (e: Throwable){
            value = NoteResult.Error(e)
        }
    }

    override fun getNoteById(id: String) = MutableLiveData<NoteResult>().apply {
        try {
            userNotesCollection.document(id).get()
                .addOnSuccessListener { snapshot ->
                    value = NoteResult.Success(snapshot.toObject(Note::class.java))
                }.addOnFailureListener {
                    value = NoteResult.Error(it)
                }
        } catch (e: Throwable){
            value = NoteResult.Error(e)
        }
    }


    fun deleteNote(note: Note): LiveData<NoteResult>{
        val result = MutableLiveData<NoteResult>()
        userNotesCollection.document(note.id).delete()
            .addOnSuccessListener {
                result.value = NoteResult.Success(note)
            }.addOnFailureListener {
                result.value = NoteResult.Error(it)
            }

        return result
    }

    override fun saveNote(note: Note)= MutableLiveData<NoteResult>().apply {
        try {
            userNotesCollection.document(note.id).set(note)
                .addOnSuccessListener {
                    value = NoteResult.Success(note)
                }.addOnFailureListener {
                    value = NoteResult.Error(it)
                }
        } catch (e: Throwable){
            value = NoteResult.Error(e)
        }
    }
}