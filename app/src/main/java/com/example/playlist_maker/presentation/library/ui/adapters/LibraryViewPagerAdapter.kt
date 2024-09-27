package com.example.playlist_maker.presentation.library.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlist_maker.presentation.library.ui.LibraryFavTracksPageFragment
import com.example.playlist_maker.presentation.library.ui.LibraryPlaylistsPageFragment

class LibraryViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> LibraryFavTracksPageFragment.newInstance()
            else -> LibraryPlaylistsPageFragment.newInstance()
        }
    }
}