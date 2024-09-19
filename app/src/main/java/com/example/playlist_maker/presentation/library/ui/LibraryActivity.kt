package com.example.playlist_maker.presentation.library.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlist_maker.databinding.ActivityLibraryBinding
import com.example.playlist_maker.presentation.library.view_model.LibraryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityLibraryBinding
    private val viewModel by viewModel <LibraryViewModel>()

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