package com.example.playlist_maker.presentation.player.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker.R
import com.example.playlist_maker.databinding.FragmentPlayerBinding
import com.example.playlist_maker.domain.prefs.dto.Track
import com.example.playlist_maker.presentation.common.BaseFragment
import com.example.playlist_maker.presentation.player.adapters.PlayerBottomSheetAdapter
import com.example.playlist_maker.presentation.player.dto.PlaylistPlayerItem
import com.example.playlist_maker.presentation.player.view_model.PlayerViewModel
import com.example.playlist_maker.utils.debounce
import com.example.playlist_maker.utils.dpToPx
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : BaseFragment<FragmentPlayerBinding>() {
    private val viewModel by viewModel<PlayerViewModel>()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private val adapter: PlayerBottomSheetAdapter by lazy {
        PlayerBottomSheetAdapter(
            onItemClickListener = {
                itemClickThrottler.invoke(it to false)
            }
        )
    }
    private lateinit var itemClickThrottler: (Pair<PlaylistPlayerItem, Boolean>) -> Unit

    override fun createViewBinding(): FragmentPlayerBinding {
        return FragmentPlayerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val item: Track? = if (Build.VERSION.SDK_INT < 33) {
            arguments?.getSerializable(TRACK) as Track
        } else {
            arguments?.getSerializable(TRACK, Track::class.java)
        }

        if (item != null) {
            viewModel.initialize(item)
        }
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
        bottomSheetBehavior =
            BottomSheetBehavior.from(viewBinding.bottomSheet.standardBottomSheetContainer)

        with(viewBinding.playerContent) {
            (activity as AppCompatActivity).setSupportActionBar(playerToolbar)
            (activity as AppCompatActivity).supportActionBar?.title = EMPTY_STRING
            playerToolbar.setNavigationOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            playerPausePlayButton.setOnClickListener {
                viewModel.applyPlayerScreenState()
            }

            playerAddToFavouriteButton.setOnClickListener {
                viewModel.toggleFavourite()
            }

            playerAddToMediaButton.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                dimBottomSheetBackground(true)
                viewModel.showPlaylists()
            }

            trackInfoTrackTimeItem.infoHeaderTextView.text =
                requireContext().getText(R.string.player_track_info_track_time)
            trackInfoAlbumItem.infoHeaderTextView.text =
                requireContext().getText(R.string.player_track_info_album)
            trackInfoReleaseDateItem.infoHeaderTextView.text =
                requireContext().getText(R.string.player_track_info_release_date)
            trackInfoGenreItem.infoHeaderTextView.text =
                requireContext().getText(R.string.player_track_info_genre)
            trackInfoCountryItem.infoHeaderTextView.text =
                requireContext().getText(R.string.player_track_info_country)
            initBottomSheet()
        }
    }

    override fun observeData() {
        viewModel.currentState.observe(this) {
            applyState(it)
        }
        viewModel.playlistEvent.observe(this) {
            applyEvent(it.first, it.second)
        }
    }

    override fun onStop() {
        viewModel.stopPlayerScreen()
        super.onStop()
    }

    override fun onDestroyView() {
        viewModel.resetPlayerScreen(requireActivity().isFinishing)
        super.onDestroyView()
    }

    private fun initBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        dimBottomSheetBackground(false)
                    }

                    else -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        with(viewBinding.bottomSheet) {
            newPlaylistButton.setOnClickListener {
                this@PlayerFragment.findNavController()
                    .navigate(R.id.action_playerFragment_to_createPlaylistFragment)
            }
            playlistsRecyclerView.adapter = adapter
            playlistsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun applyState(state: PlayerViewModel.State) {
        with(viewBinding.playerContent) {
            val item = state.track

            val album = item?.artworkUrl100?.replaceAfterLast(DELIMITER, ALBUM_SIZE)
            Glide.with(playerAlbumImageView)
                .load(album)
                .placeholder(R.drawable.placeholder_album)
                .fitCenter()
                .transform(RoundedCorners(dpToPx(8f, playerAlbumImageView.context)))
                .into(playerAlbumImageView)

            playerTrackTimeTextView.text = state.timeLeft
            playerTrackNameTextView.text = item?.trackName
            playerArtistNameTextView.text = item?.artistName
            trackInfoTrackTimeItem.infoBodyTextView.text = item?.trackTime
            trackInfoGenreItem.infoBodyTextView.text = item?.primaryGenreName
            trackInfoCountryItem.infoBodyTextView.text = item?.country

            if (item?.collectionName.isNullOrEmpty()) {
                trackInfoAlbumItem.infoHeaderTextView.isGone = true
                trackInfoAlbumItem.infoBodyTextView.isGone = true
            } else {
                trackInfoAlbumItem.infoHeaderTextView.isVisible = true
                trackInfoAlbumItem.infoBodyTextView.isVisible = true
                trackInfoAlbumItem.infoBodyTextView.text = item?.collectionName
            }

            if (item?.releaseDate == null) {
                trackInfoReleaseDateItem.infoBodyTextView.text =
                    requireContext().getText(R.string.player_no_data)
            } else {
                trackInfoReleaseDateItem.infoBodyTextView.text = item.releaseDate
            }

            adapter.setItems(state.playlists)
        }

        setButtonDrawable(
            Button.PLAYER,
            state.isPlay,
            R.drawable.ic_pause,
            R.drawable.ic_play,
        )

        setButtonDrawable(
            Button.MEDIA,
            state.isInMedia,
            R.drawable.ic_already_added_to_media,
            R.drawable.ic_add_to_media,
        )


        setButtonDrawable(
            Button.FAVOURITE,
            state.isFavourite,
            R.drawable.ic_favourite_active,
            R.drawable.ic_favourite_inactive,
        )
    }

    private fun applyEvent(event: Boolean, playlistName: String) {
        val text: String
        if (event) {
            text = resources.getString(
                R.string.playlist_add_track_to_playlist_success_toast,
                playlistName
            )
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            viewModel.toggleMedia(true)
        } else {
            text = resources.getString(
                R.string.playlist_add_track_to_playlist_failure_toast,
                playlistName
            )
            viewModel.toggleMedia(false)
        }

        val snackBar = Snackbar.make(
            requireContext(),
            viewBinding.root,
            text,
            Snackbar.LENGTH_SHORT
        )

        snackBar.setBackgroundTint(
            requireContext().resources.getColor(
                R.color.snackbar_bg,
                null
            )
        )
        snackBar.setTextColor(requireContext().resources.getColor(R.color.snackbar_text, null))
        val textView = snackBar
            .view
            .findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        snackBar.show()
    }

    private fun setButtonDrawable(
        button: Button,
        buttonState: Boolean,
        @DrawableRes positive: Int,
        @DrawableRes negative: Int
    ) {
        val view = when (button) {
            Button.MEDIA -> {
                viewBinding.playerContent.playerAddToMediaButton
            }

            Button.FAVOURITE -> {
                viewBinding.playerContent.playerAddToFavouriteButton
            }

            Button.PLAYER -> {
                viewBinding.playerContent.playerPausePlayButton
            }
        }

        view.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                if (buttonState) positive else negative,
                null
            )
        )
    }

    private fun dimBottomSheetBackground(fillBlackout: Boolean) {
        with(viewBinding) {
            if (fillBlackout) {
                val anim = ObjectAnimator.ofFloat(blackout, "alpha", 0.5f)
                anim.setDuration(300)
                anim.start()
                blackout.visibility = View.VISIBLE
                blackout.isClickable = true
                blackout.isFocusable = true
                blackout.setEnabled(true)
            } else {
                val anim: ObjectAnimator = ObjectAnimator.ofFloat(blackout, "alpha", 0f)
                anim.setDuration(300)
                anim.start()
                anim.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        blackout.visibility = View.GONE
                        blackout.isClickable = false
                        blackout.isFocusable = false
                        blackout.setEnabled(false)
                    }
                })
            }
        }
    }

    enum class Button {
        MEDIA,
        FAVOURITE,
        PLAYER
    }

    companion object {
        private const val DELIMITER = '/'
        private const val ALBUM_SIZE = "512x512bb.jpg"
        private const val EMPTY_STRING = ""
        private const val TRACK = "track"
        private const val CLICK_DELAY = 500L
    }
}