package com.example.playlist_maker.presentation.main.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.playlist_maker.R
import com.example.playlist_maker.databinding.ActivityMainBinding
import com.example.playlist_maker.di.Injector
import com.example.playlist_maker.presentation.settings.ui.SettingsActivity
import com.example.playlist_maker.presentation.search.ui.SearchActivity
import com.example.playlist_maker.presentation.library.ui.LibraryActivity
import com.example.playlist_maker.presentation.main.view_model.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {Injector.getViewModelFactory()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        if(Build.VERSION.SDK_INT >= 29) {
            viewBinding.root.isForceDarkAllowed = false
        }

        initUi()
    }

    private fun initUi() {
        with(viewBinding) {
            this@MainActivity.setSupportActionBar(mainToolbar)
            this@MainActivity.supportActionBar?.title = resources.getText(R.string.main_title)

            searchButton.setOnClickListener {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            libraryButton.setOnClickListener {
                val intent = Intent(this@MainActivity, LibraryActivity::class.java)
                startActivity(intent)
            }

            settingsButton.setOnClickListener {
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
    }
}