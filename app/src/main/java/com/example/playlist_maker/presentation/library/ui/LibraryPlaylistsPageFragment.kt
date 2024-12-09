package com.example.playlist_maker.presentation.library.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlist_maker.R
import com.example.playlist_maker.databinding.FragmentLibraryPlaylistsPageBinding
import com.example.playlist_maker.domain.library.dto.Playlist
import com.example.playlist_maker.presentation.common.BaseFragment
import com.example.playlist_maker.presentation.library.dto.PlaylistLibraryItem
import com.example.playlist_maker.presentation.library.ui.adapters.LibraryPlaylistAdapter
import com.example.playlist_maker.presentation.library.view_model.LibraryPlaylistsPageViewModel
import com.example.playlist_maker.utils.GridSpacingItemDecoration
import com.example.playlist_maker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryPlaylistsPageFragment : BaseFragment<FragmentLibraryPlaylistsPageBinding>() {
    private val viewModel by viewModel<LibraryPlaylistsPageViewModel>()
    private val adapter: LibraryPlaylistAdapter by lazy {
        LibraryPlaylistAdapter(onItemClickListener = {
            itemClickThrottler.invoke(it to false)
        })
    }
    private lateinit var itemClickThrottler: (Pair<PlaylistLibraryItem, Boolean>) -> Unit

    override fun createViewBinding(): FragmentLibraryPlaylistsPageBinding {
        return FragmentLibraryPlaylistsPageBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemClickThrottler = debounce(
            delay = CLICK_DELAY,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            isDebouncer = false
        ) { item ->
            viewModel.handleItemClick(item.first)
        }
    }

    override fun initUi() {
        with(viewBinding) {
            newPlaylistButton.setOnClickListener {
                this@LibraryPlaylistsPageFragment.findNavController()
                    .navigate(R.id.action_libraryPlaylistsFragment_to_createPlaylistFragment)
            }

            val spacingInPixels = resources.getDimensionPixelSize(R.dimen.grid_layout_margin)
            libraryPlaylistsRecyclerView.addItemDecoration(
                GridSpacingItemDecoration(
                    2,
                    spacingInPixels,
                    true,
                    0
                )
            )

            libraryPlaylistsRecyclerView.adapter = adapter
            libraryPlaylistsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    override fun observeData() {
        viewModel.currentState.observe(viewLifecycleOwner) {
            applyState(it)
        }

        viewModel.navigationEvent.observe(viewLifecycleOwner) {
            applyEvent(it)
        }
    }

    private fun applyState(state: LibraryPlaylistsPageViewModel.State) {
        viewBinding.libraryPlaylistsRecyclerView.isGone = true
        viewBinding.noDataContainer.isGone = true

        when (state) {
            is LibraryPlaylistsPageViewModel.State.NoData -> {
                viewBinding.noDataContainer.isVisible = true
            }

            is LibraryPlaylistsPageViewModel.State.Data -> {
                viewBinding.libraryPlaylistsRecyclerView.isVisible = true
                adapter.setItems(state.list)
            }
        }
    }

    private fun applyEvent(playlist: Playlist) {
        val bundle = bundleOf()
        bundle.putSerializable(PLAYLIST_ID, playlist.id)

        this@LibraryPlaylistsPageFragment.findNavController()
            .navigate(R.id.action_libraryFavTracksPageFragment_to_playerFragment, bundle)
    }

    companion object {
        fun newInstance() = LibraryPlaylistsPageFragment()
        private const val CLICK_DELAY = 500L
        private const val PLAYLIST_ID = "playlist_id"
    }
}