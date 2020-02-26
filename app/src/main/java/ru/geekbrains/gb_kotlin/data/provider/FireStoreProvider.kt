package ru.geekbrains.gb_kotlin.data.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import ru.geekbrains.gb_kotlin.data.entity.Note
import ru.geekbrains.gb_kotlin.data.entity.User
import ru.geekbrains.gb_kotlin.data.errors.NoAuthException
import ru.geekbrains.gb_kotlin.data.model.NoteResult
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FireStoreProvider(private val firebaseAuth: FirebaseAuth, private val store: FirebaseFirestore) : RemoteDataProvider {

    companion object {
        private const val NOTES_COLLECTION = "notes"
        private const val USER_COLLECTION = "users"
    }


    private val currentUser
        get() = firebaseAuth.currentUser

    private val userNotesCollection: CollectionReference
        get() = currentUser?.let {
            store.collection(USER_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
        } ?: throw NoAuthException()


    override suspend fun getCurrentUser() : User?= suspendCoroutine {
        it.resume( currentUser?.let { firebaseUser ->
            User(firebaseUser.displayName ?: "", firebaseUser.email ?: "")
        })
    }

    override fun subsrcibeToAllNotes() : ReceiveChannel<NoteResult> = Channel<NoteResult>(
        Channel.CONFLATED).apply {
        var registration: ListenerRegistration? = null
        try{
            registration=userNotesCollection.addSnapshotListener { snapshot, e ->
            val value=e?.let {
                NoteResult.Error(it)
            } ?: snapshot?.let { snapshot ->
                NoteResult.Success(snapshot.documents.map { it.toObject(Note::class.java) })
            }
                value?.let { offer(it) }
        }
        } catch (e: Throwable){
            offer(NoteResult.Error(e))
        }
        invokeOnClose {
            registration?.remove()
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

    override fun deleteNote(noteId: String): LiveData<NoteResult> =MutableLiveData<NoteResult>().apply {
        try {
            userNotesCollection.document(noteId).delete()
                .addOnSuccessListener {
                    value = NoteResult.Success(null)
                }.addOnFailureListener {
                    value = NoteResult.Error(it)
                }
        } catch (e: Throwable){
            value = NoteResult.Error(e)
        }
    }
}