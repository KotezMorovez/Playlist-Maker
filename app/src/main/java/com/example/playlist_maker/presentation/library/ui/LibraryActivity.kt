package com.example.playlist_maker.presentation.library.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.playlist_maker.R
import com.example.playlist_maker.databinding.ActivityLibraryBinding

class LibraryActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityLibraryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.fragment_container_view, LibraryFragment.newInstance())
            }
        }
    }
}