package com.example.playlist_maker.presentation.library.ui

import com.example.playlist_maker.databinding.FragmentLibraryFavTracksPageBinding
import com.example.playlist_maker.presentation.common.BaseFragment
import com.example.playlist_maker.presentation.library.view_model.LibraryFavTracksPageViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryFavTracksPageFragment : BaseFragment<FragmentLibraryFavTracksPageBinding>() {
    private val viewModel by viewModel <LibraryFavTracksPageViewModel>()

    override fun createViewBinding(): FragmentLibraryFavTracksPageBinding {
        return FragmentLibraryFavTracksPageBinding.inflate(layoutInflater)
    }

    override fun initUi() {
        with(viewBinding) {}
    }

    override fun observeData() {}

    companion object {
        fun newInstance() = LibraryFavTracksPageFragment()
    }
}