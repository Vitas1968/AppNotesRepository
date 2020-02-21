package ru.geekbrains.gb_kotlin.data.provider

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import ru.geekbrains.gb_kotlin.data.entity.Note
import ru.geekbrains.gb_kotlin.data.errors.NoAuthException
import ru.geekbrains.gb_kotlin.data.model.NoteResult


class FireStoreProviderTest {
    @get:Rule
    val taskExecutorRule=InstantTaskExecutorRule()

    private val mockkDb=mockk<FirebaseFirestore>()
    private val mockkAuth=mockk<FirebaseAuth>()
    private val mockkResultCollection=mockk<CollectionReference>()
    private val mockkUser=mockk<FirebaseUser>()

    private val mockkDocument1=mockk<DocumentSnapshot>()
    private val mockkDocument2=mockk<DocumentSnapshot>()
    private val mockkDocument3=mockk<DocumentSnapshot>()

    private val testNotes= listOf(Note("1"),Note("2"),Note("3"))
    private val provider = FireStoreProvider(mockkAuth,mockkDb)

    @Before
    fun setUp() {
        clearAllMocks()
        every { mockkAuth.currentUser } returns mockkUser
        every { mockkUser.uid} returns ""
        every { mockkDb.collection(any()).document(any()).collection(any()) } returns mockkResultCollection
        every { mockkDocument1.toObject(Note::class.java) } returns testNotes[0]
        every { mockkDocument2.toObject(Note::class.java) } returns testNotes[1]
        every { mockkDocument3.toObject(Note::class.java) } returns testNotes[2]

    }

    @After
    fun tearDown() {
    }

    @Test
    fun `showld throw NoAuthExeption if no auth`(){

        var result:Any?=null
        every { mockkAuth.currentUser } returns  null
        provider.subsrcibeToAllNotes().observeForever {
            result = (it as? NoteResult.Error)?.error}
        //assertTrue(result is NoAuthException)
        assert(result is NoAuthException)
    }

    @Test
    fun `saveNotes calls set`(){
        val mockkDocumentReference : DocumentReference = mockk<DocumentReference>()
        every { mockkResultCollection.document(testNotes[0].id) } returns mockkDocumentReference
        provider.saveNote(testNotes[0])
        verify (exactly = 1){mockkDocumentReference.set(testNotes[0])}

    }

    @Test
    fun `subscribe to all notes returns notes`() {

        var result: List<Note>? = null
        every { mockkAuth.currentUser } returns null
        provider.subsrcibeToAllNotes().observeForever {
            //      result = (it as? NoteResult.Error)?.error}
            //assertTrue(result is NoAuthException)
            //  assert(result is NoAuthException)
        }
    }
}