package ru.geekbrains.gb_kotlin.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.geekbrains.gb_kotlin.R
import ru.geekbrains.gb_kotlin.data.entity.Note
import ru.geekbrains.gb_kotlin.ui.base.BaseActivity
import ru.geekbrains.gb_kotlin.ui.note.NoteActivity

class MainActivity : BaseActivity<List<Note>?, MainViewState>() {

    companion object {
        fun start(context: Context) = Intent(context, MainActivity::class.java).apply {

            context.startActivity(this)
        }
    }

    override val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override val layoutRes = R.layout.activity_main
    lateinit var adapter: NotesRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        rv_notes.layoutManager = GridLayoutManager(this, 2)
        adapter = NotesRVAdapter { note ->
            NoteActivity.start(this, note.id)
        }

        rv_notes.adapter = adapter
        fab.setOnClickListener {
            NoteActivity.start(this)
        }

        listOf<String>().forEach {
            if(it.isEmpty()){
                return@forEach
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?) =
        MenuInflater(this).inflate(R.menu.main, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.logout -> showLogoutDialog()?.let { true }
        else -> false
    }

    fun showLogoutDialog() {
        supportFragmentManager.findFragmentByTag(LogoutDialog.TAG) ?:
        LogoutDialog.createInstance().show(supportFragmentManager, LogoutDialog.TAG)
    }

    override fun renderData(data: List<Note>?) {
        data?.let {
            adapter.notes = it
        }
    }
}
