package ru.geekbrains.gb_kotlin.ui.note

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.mockk
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import ru.geekbrains.gb_kotlin.data.NotesRepository
import ru.geekbrains.gb_kotlin.data.entity.Note
import ru.geekbrains.gb_kotlin.data.model.NoteResult

class NoteViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<NotesRepository>()
    private val noteLiveData = MutableLiveData<NoteResult>()

    private val testNote = Note("1", "title", "text")

    private lateinit var viewModel: NoteViewModel

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }
}