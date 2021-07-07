package ru.geekbrains.gb_kotlin

import android.app.Application
import org.koin.android.ext.android.startKoin
import ru.geekbrains.gb_kotlin.di.appModule
import ru.geekbrains.gb_kotlin.di.mainModule
import ru.geekbrains.gb_kotlin.di.noteModule
import ru.geekbrains.gb_kotlin.di.splashModule

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule, splashModule, mainModule, noteModule))
    }
}