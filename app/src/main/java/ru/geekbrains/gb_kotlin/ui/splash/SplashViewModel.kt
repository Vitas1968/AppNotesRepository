package ru.geekbrains.gb_kotlin.ui.splash

import kotlinx.coroutines.launch
import ru.geekbrains.gb_kotlin.data.NotesRepository
import ru.geekbrains.gb_kotlin.data.errors.NoAuthException
import ru.geekbrains.gb_kotlin.ui.base.BaseViewModel

class SplashViewModel(private val notesRepository: NotesRepository) : BaseViewModel<Boolean?>() {

    fun requestUser() {
        launch {
            notesRepository.getCurrentUser()?.let {
                setData(true)
            } ?: setError(NoAuthException())
        }
    }
}