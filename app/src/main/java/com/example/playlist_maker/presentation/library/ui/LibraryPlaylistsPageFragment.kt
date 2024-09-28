package com.example.playlist_maker.presentation.library.ui

import com.example.playlist_maker.databinding.FragmentLibraryPlaylistsPageBinding
import com.example.playlist_maker.presentation.common.BaseFragment
import com.example.playlist_maker.presentation.library.view_model.LibraryPlaylistsPageViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryPlaylistsPageFragment: BaseFragment<FragmentLibraryPlaylistsPageBinding>() {
    private val viewModel by viewModel <LibraryPlaylistsPageViewModel>()

    override fun createViewBinding(): FragmentLibraryPlaylistsPageBinding {
        return FragmentLibraryPlaylistsPageBinding.inflate(layoutInflater)
    }

    override fun initUi() {
        with(viewBinding) {
            newPlaylistButton.setOnClickListener { /* TODO */ }
        }
    }

    override fun observeData() {}

    companion object {
        fun newInstance() = LibraryPlaylistsPageFragment()
    }
}