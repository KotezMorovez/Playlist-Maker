package com.example.playlist_maker.presentation.library.ui

import androidx.appcompat.app.AppCompatActivity
import com.example.playlist_maker.R
import com.example.playlist_maker.databinding.FragmentLibraryBinding
import com.example.playlist_maker.presentation.common.BaseFragment
import com.example.playlist_maker.presentation.library.ui.adapters.LibraryViewPagerAdapter
import com.example.playlist_maker.presentation.library.view_model.LibraryViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryFragment : BaseFragment<FragmentLibraryBinding>() {
    private var tabMediator: TabLayoutMediator? = null
    private val viewModel by viewModel<LibraryViewModel>()

    override fun createViewBinding(): FragmentLibraryBinding {
        return FragmentLibraryBinding.inflate(layoutInflater)
    }

    override fun initUi() {
        with(viewBinding) {
            (activity as AppCompatActivity).setSupportActionBar(libraryToolbar)
            (activity as AppCompatActivity).supportActionBar?.title =
                resources.getText(R.string.library_title)

            libraryViewPager.adapter =
                LibraryViewPagerAdapter(childFragmentManager, lifecycle)

            tabMediator = TabLayoutMediator(libraryTabLayout, libraryViewPager) { tab, position ->
                when (position) {
                    0 -> tab.text =
                        requireContext().resources.getText(R.string.library_tab_fav_tracks)

                    1 -> tab.text =
                        requireContext().resources.getText(R.string.library_tab_playlists)
                }
            }
            tabMediator?.attach()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator?.detach()
    }

    override fun observeData() {}
}