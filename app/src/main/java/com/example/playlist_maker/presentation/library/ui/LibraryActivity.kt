package com.example.playlist_maker.presentation.library.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlist_maker.databinding.ActivityLibraryBinding
import com.example.playlist_maker.di.Injector
import com.example.playlist_maker.di.ViewModelFactory
import com.example.playlist_maker.presentation.library.view_model.LibraryViewModel

class LibraryActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityLibraryBinding
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityLibraryBinding.inflate(layoutInflater)
        viewModelFactory = Injector.getViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[LibraryViewModel::class.java]

        setContentView(viewBinding.root)

        initUi()
        observeData()
    }

    private fun initUi() {}

    private fun observeData() {}
}