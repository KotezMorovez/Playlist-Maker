package com.example.playlist_maker.presentation.playlist.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker.R
import com.example.playlist_maker.databinding.FragmentPlaylistBinding
import com.example.playlist_maker.presentation.common.BaseFragment
import com.example.playlist_maker.presentation.playlist.ui.adapter.TrackAdapter
import com.example.playlist_maker.presentation.playlist.view_model.PlaylistViewModel
import com.example.playlist_maker.presentation.search.dto.TrackItem
import com.example.playlist_maker.utils.debounce
import com.example.playlist_maker.utils.dpToPx
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : BaseFragment<FragmentPlaylistBinding>() {
    private val viewModel by viewModel<PlaylistViewModel>()
    private lateinit var trackListBottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var menuBottomSheetBehavior: BottomSheetBehavior<View>
    private var trackId = ""
    private val alertDialog by lazy {
        MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlert)
            .setTitle(resources.getText(R.string.playlist_track_alert_dialog_title))
            .setMessage(resources.getText(R.string.playlist_track_alert_dialog_body))
            .setNegativeButton(resources.getText(R.string.playlist_track_alert_dialog_negative_button)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getText(R.string.playlist_track_alert_dialog_positive_button)) { _, _ ->
                viewModel.deleteTrack(trackId)
            }
    }
    private val adapter: TrackAdapter by lazy {
        TrackAdapter(
            onItemClickListener = {
                itemClickThrottler.invoke(it)
            },
            onItemLongClickListener = {
                trackId = it.trackId
                alertDialog.show()
            }
        )
    }
    private lateinit var itemClickThrottler: (TrackItem) -> Unit

    override fun createViewBinding(): FragmentPlaylistBinding {
        return FragmentPlaylistBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id: String? = if (Build.VERSION.SDK_INT < 33) {
            arguments?.getSerializable(PLAYLIST_ID) as String
        } else {
            arguments?.getSerializable(PLAYLIST_ID, String::class.java)
        }

        if (id != null) {
            viewModel.initialize(id)
        }
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

        viewBinding.playlistContent.shareButton.post {
            val location = IntArray(2)
            viewBinding.playlistContent.shareButton.getLocationOnScreen(location)
            val bottomSheetHeight =
                resources.displayMetrics.heightPixels - location[1] - viewBinding.playlistContent.shareButton.height - dpToPx(
                    MARGIN_DP,
                    requireContext()
                )

            trackListBottomSheetBehavior.peekHeight = bottomSheetHeight
            trackListBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun initUi() {
        trackListBottomSheetBehavior =
            BottomSheetBehavior.from(viewBinding.bottomSheet.standardBottomSheetContainer)
        menuBottomSheetBehavior =
            BottomSheetBehavior.from(viewBinding.menuBottomSheet.menuBottomSheetContainer)

        with(viewBinding.playlistContent) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.title = EMPTY_STRING
            toolbar.setNavigationOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            shareButton.setOnClickListener {
                viewModel.handleShareButtonClick()
            }

            menuButton.setOnClickListener {
                dimBottomSheetBackground(true)
                menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }

        with(viewBinding.bottomSheet) {
            playlistsRecyclerView.adapter = adapter
            playlistsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

        initMenuBottomSheet()
    }

    override fun observeData() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            applyState(state)
        }
        viewModel.navigationTrackEvent.observe(viewLifecycleOwner) { track ->
            val bundle = bundleOf()
            bundle.putSerializable(TRACK, track)

            findNavController()
                .navigate(R.id.action_playlistFragment_to_playerFragment, bundle)
        }
        viewModel.navigationEditEvent.observe(viewLifecycleOwner) { playlistId ->
            val bundle = bundleOf()
            bundle.putString(PLAYLIST_ID, playlistId)

            this@PlaylistFragment.findNavController().navigate(
                R.id.action_playlistFragment_to_editPlaylistFragment, bundle
            )
        }
        viewModel.shareEvent.observe(viewLifecycleOwner) { event ->
            if (event) {
                applyShareEvent()
            } else {
                Snackbar.make(
                    requireContext(),
                    viewBinding.root,
                    resources.getText(R.string.playlist_no_tracks_toast),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.deleteEvent.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    private fun applyState(state: PlaylistViewModel.State) {
        viewBinding.bottomSheet.playlistsRecyclerView.isGone = true
        viewBinding.bottomSheet.noTracksTextView.isGone = true

        with(viewBinding.playlistContent) {
            Glide.with(coverImageView)
                .load(state.playlist.imageUri)
                .placeholder(R.drawable.placeholder_album)
                .centerCrop()
                .into(coverImageView)

            nameTextView.text = state.playlist.name

            if (state.playlist.description.isBlank()) {
                descriptionTextView.isGone = true
            } else {
                descriptionTextView.isVisible = true
                descriptionTextView.text = state.playlist.description
            }

            totalTimeTextView.text = getDuration(state.playlist.totalDuration)
            trackCountTextView.text = getTracksCount(state.playlist.tracksCount)
        }

        with(viewBinding.bottomSheet) {
            if (state.tracks.isNotEmpty()) {
                playlistsRecyclerView.isVisible = true
                adapter.setItems(state.tracks)
            } else {
                noTracksTextView.isVisible = true
            }
        }

        with(viewBinding.menuBottomSheet) {
            Glide.with(coverImageView)
                .load(state.playlist.imageUri)
                .placeholder(R.drawable.placeholder_album)
                .fitCenter()
                .transform(RoundedCorners(dpToPx(8f, coverImageView.context)))
                .into(coverImageView)

            nameTextView.text = state.playlist.name
            trackCountTextView.text = getTracksCount(state.playlist.tracksCount)
        }
    }

    private fun applyShareEvent() {
        val playlistText =
            "${viewBinding.playlistContent.nameTextView.text}\n" +
                    "${viewBinding.playlistContent.descriptionTextView.text}\n" +
                    "${viewBinding.playlistContent.trackCountTextView.text}\n"

        val tracksText = viewModel.getTracksText().trim()

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_TEXT, playlistText + tracksText)
        shareIntent.type = SEND_INTENT_TYPE

        val chooserIntent = Intent.createChooser(shareIntent, null)
        startActivity(chooserIntent)
    }

    private fun getDuration(duration: Int): String {
        return "$duration " + resources.getQuantityString(
            R.plurals.minutes,
            duration
        )
    }

    private fun getTracksCount(count: Int): String {
        return "$count " + resources.getQuantityString(
            R.plurals.tracks,
            count
        )
    }

    private fun initMenuBottomSheet() {
        with(viewBinding.menuBottomSheet) {
            menuBottomSheetBehavior.addBottomSheetCallback(object :
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

            editTextView.setOnClickListener {
                viewModel.handleEditButtonClick()
            }

            deleteTextView.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlert)
                    .setTitle(resources.getText(R.string.playlist_alert_dialog_title))
                    .setMessage(
                        resources.getString(
                            R.string.playlist_alert_dialog_body,
                            viewBinding.menuBottomSheet.nameTextView.text
                        )
                    )
                    .setNegativeButton(resources.getText(R.string.playlist_alert_dialog_negative_button)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton(resources.getText(R.string.playlist_alert_dialog_positive_button)) { _, _ ->
                        viewModel.deletePlaylist()
                    }.show()
            }

            shareTextView.setOnClickListener {
                menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                viewModel.handleShareButtonClick()
            }
        }
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

    companion object {
        private const val EMPTY_STRING = ""
        private const val CLICK_DELAY = 500L
        private const val PLAYLIST_ID = "playlist_id"
        private const val MARGIN_DP = 24F
        private const val TRACK = "track"
        private const val SEND_INTENT_TYPE = "text/plain"
    }
}