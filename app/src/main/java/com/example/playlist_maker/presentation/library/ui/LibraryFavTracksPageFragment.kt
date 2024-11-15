package com.example.playlist_maker.presentation.library.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlist_maker.R
import com.example.playlist_maker.databinding.FragmentLibraryFavTracksPageBinding
import com.example.playlist_maker.domain.prefs.dto.Track
import com.example.playlist_maker.presentation.common.BaseFragment
import com.example.playlist_maker.presentation.library.dto.TrackLibraryItem
import com.example.playlist_maker.presentation.library.ui.adapters.LibraryFavTracksAdapter
import com.example.playlist_maker.presentation.library.view_model.LibraryFavTracksPageViewModel
import com.example.playlist_maker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryFavTracksPageFragment : BaseFragment<FragmentLibraryFavTracksPageBinding>() {
    private val viewModel by viewModel<LibraryFavTracksPageViewModel>()
    private val libraryAdapter: LibraryFavTracksAdapter by lazy {
        LibraryFavTracksAdapter(onItemClickListener = {
            itemClickThrottler.invoke(it)
        })
    }
    private lateinit var itemClickThrottler: (TrackLibraryItem) -> Unit

    override fun createViewBinding(): FragmentLibraryFavTracksPageBinding {
        return FragmentLibraryFavTracksPageBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemClickThrottler = debounce(
            delay = CLICK_DELAY,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            isDebouncer = false
        ) { item ->
            viewModel.handleItemClick(item)
        }
    }

    override fun initUi() {
        with(viewBinding) {
            libraryFavTracksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            libraryFavTracksRecyclerView.adapter = libraryAdapter
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

    private fun applyState(state: LibraryFavTracksPageViewModel.State) {
        viewBinding.libraryFavTracksRecyclerView.isGone = true
        viewBinding.noDataContainer.isGone = true

        when (state) {
            is LibraryFavTracksPageViewModel.State.NoData -> {
                viewBinding.noDataContainer.isVisible = true
            }

            is LibraryFavTracksPageViewModel.State.Data -> {
                viewBinding.libraryFavTracksRecyclerView.isVisible = true
                libraryAdapter.setItems(state.list)
            }
        }
    }

    private fun applyEvent(track: Track) {
        val bundle = bundleOf()
        bundle.putSerializable(TRACK, track)

        this@LibraryFavTracksPageFragment.findNavController()
            .navigate(R.id.action_libraryFavTracksPageFragment_to_playerFragment, bundle)

    }

    companion object {
        fun newInstance() = LibraryFavTracksPageFragment()
        private const val CLICK_DELAY = 500L
        private const val TRACK = "track"
    }
}