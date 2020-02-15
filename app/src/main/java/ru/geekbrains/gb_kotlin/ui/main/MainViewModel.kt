package ru.geekbrains.gb_kotlin.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import ru.geekbrains.gb_kotlin.data.NotesRepository
import ru.geekbrains.gb_kotlin.data.entity.Note
import ru.geekbrains.gb_kotlin.data.model.NoteResult
import ru.geekbrains.gb_kotlin.ui.base.BaseViewModel

class MainViewModel : BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = object : Observer<NoteResult>{
        override fun onChanged(t: NoteResult?) {
            t ?: return

            when(t){
                is NoteResult.Success<*> -> {
                    viewStateLiveData.value = MainViewState(notes = t.data as? List<Note>)
                }
                is NoteResult.Error -> {
                    viewStateLiveData.value = MainViewState(error = t.error)
                }
            }
        }
    }

    private val repositoryNotes = NotesRepository.getNotes()

    init {
         viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    override fun getViewState(): LiveData<MainViewState> = viewStateLiveData

    override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
       /* repositoryNotes.removeObserver{noteresult ->
            noteresult?.let {
                when (it) {
                    is NoteResult.Success<*> -> {
                        viewStateLiveData.value =
                            MainViewState(notes = it.data as? List<Note>)
                    }
                    is NoteResult.Error -> {
                        viewStateLiveData.value = MainViewState(error = it.error)
                    }
                }
            }
        }
        */
        super.onCleared()
    }

}