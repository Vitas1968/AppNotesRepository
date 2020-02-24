package ru.geekbrains.gb_kotlin.data.provider

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import io.mockk.*
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
        val mockSnapshot = mockk<QuerySnapshot>()
        val slot = slot<EventListener<QuerySnapshot>>()

        every { mockSnapshot.documents } returns listOf(mockkDocument1, mockkDocument2, mockkDocument3)
        every { mockkResultCollection.addSnapshotListener(capture(slot)) } returns mockk()
        provider.subsrcibeToAllNotes().observeForever{
            result = (it as? NoteResult.Success<List<Note>>)?.data
        }
        slot.captured.onEvent(mockSnapshot, null)
        assertEquals(testNotes, result)
        }

    @Test
    fun `deleteNote calls document delete`() {
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockkResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        provider.deleteNote(testNotes[0].id)
        verify(exactly = 1) { mockDocumentReference.delete() }
    }
    //TODO: Тест для getNoteById

    @Test
    fun `showld return Note by id`(){
        var result: Note? = null
        val mockSnapshot = mockk<DocumentSnapshot>()
        val mockTask = mockk<Task<DocumentSnapshot>>()
        val mockkDocumentReference = mockk<DocumentReference>()

        val slot = slot<OnSuccessListener<DocumentSnapshot>>()


        every { mockkResultCollection.document(any()) } returns mockkDocumentReference
        every { mockkResultCollection.document(any()).get() } returns mockTask
        every { mockkResultCollection.addOnSuccessListener(capture(slot)) } returns mockk()

    }



}