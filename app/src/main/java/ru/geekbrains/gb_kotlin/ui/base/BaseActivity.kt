package ru.geekbrains.gb_kotlin.ui.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

abstract class BaseActivity<T, S : BaseViewState<T>> : AppCompatActivity() {
    abstract val viewModel: BaseViewModel<T, S>
    abstract val layoutRes: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        viewModel.getViewState().observe(this, object : Observer<S> {
            override fun onChanged(t: S?) {
                t ?: return
                t.error?.let {
                    renderError(it)
                    return
                }
                renderData(t.data)
            }
        })
    }

    abstract fun renderData(data: T)

    private fun renderError(error: Throwable) {
        error.message?.let { showError(it) }
    }

    protected fun showError(error: String){
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

}