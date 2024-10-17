package com.example.playlist_maker.presentation.main.ui

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlist_maker.databinding.ActivityMainBinding
import com.example.playlist_maker.presentation.main.view_model.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private val viewModel by viewModel <MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        if(Build.VERSION.SDK_INT >= 29) {
            viewBinding.root.isForceDarkAllowed = false
        }
    }
}