package ru.geekbrains.gb_kotlin.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import ru.geekbrains.gb_kotlin.data.NotesRepository
import ru.geekbrains.gb_kotlin.data.provider.FireStoreProvider
import ru.geekbrains.gb_kotlin.data.provider.RemoteDataProvider
import ru.geekbrains.gb_kotlin.ui.main.MainViewModel
import ru.geekbrains.gb_kotlin.ui.note.NoteViewModel
import ru.geekbrains.gb_kotlin.ui.splash.SplashViewModel


val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single<RemoteDataProvider>{ FireStoreProvider(get(), get()) }
    single { NotesRepository(get()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

val mainModule = module {
    viewModel { MainViewModel(get()) }
}

val noteModule = module {
    viewModel { NoteViewModel(get()) }
}