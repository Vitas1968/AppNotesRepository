package ru.geekbrains.gb_kotlin.ui.splash

import ru.geekbrains.gb_kotlin.ui.base.BaseViewState

class SplashViewState(authenticated: Boolean? = null, error: Throwable? = null) : BaseViewState<Boolean?>(authenticated, error)