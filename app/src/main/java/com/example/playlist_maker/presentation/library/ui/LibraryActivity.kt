package com.example.playlist_maker.presentation.library.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.playlist_maker.databinding.ActivityLibraryBinding
import com.example.playlist_maker.di.Injector

class LibraryActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityLibraryBinding
    private val viewModel: ViewModel by viewModels { Injector.getViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        initUi()
        observeData()
    }

    private fun initUi() {}

    private fun observeData() {}
}