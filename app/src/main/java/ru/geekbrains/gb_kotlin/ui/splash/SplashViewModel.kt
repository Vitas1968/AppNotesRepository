package ru.geekbrains.gb_kotlin.ui.splash

import ru.geekbrains.gb_kotlin.data.NotesRepository
import ru.geekbrains.gb_kotlin.data.errors.NoAuthException
import ru.geekbrains.gb_kotlin.ui.base.BaseViewModel

class SplashViewModel : BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser() {
        NotesRepository.getCurrentUser().observeForever {                                                                                                                                                                                                                           //Я копипастил код с урока и не заметил эту надпись
            viewStateLiveData.value = it?.let {
                SplashViewState(authenticated = true)
            } ?: let {
                SplashViewState(error = NoAuthException())
            }
        }
    }
}