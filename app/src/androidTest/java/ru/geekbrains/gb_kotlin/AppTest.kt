package ru.geekbrains.gb_kotlin

import android.app.Application

import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.koin.android.ext.android.startKoin

class AppTest : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, emptyList())
    }
}