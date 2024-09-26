package com.example.playlist_maker.presentation.player.ui

import android.os.Build
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker.R
import com.example.playlist_maker.utils.dpToPx
import com.example.playlist_maker.databinding.ActivityPlayerBinding
import com.example.playlist_maker.domain.prefs.dto.Track
import com.example.playlist_maker.presentation.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityPlayerBinding
    private val viewModel by viewModel <PlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val item: Track? = if (Build.VERSION.SDK_INT < 33) {
            intent.extras?.getSerializable(TRACK) as Track
        } else {
            intent.extras?.getSerializable(TRACK, Track::class.java)
        }

        if (item != null) {
            viewModel.initialize(item)
        }

        initUi()
        observeData()
    }

    private fun initUi() {
        with(viewBinding) {
            this@PlayerActivity.setSupportActionBar(playerToolbar)
            this@PlayerActivity.supportActionBar?.title = EMPTY_STRING
            playerToolbar.setNavigationOnClickListener {
                this@PlayerActivity.onBackPressedDispatcher.onBackPressed()
            }

            playerPausePlayButton.setOnClickListener {
                viewModel.applyPlayerScreenState()
            }

            playerAddToFavouriteButton.setOnClickListener {
                viewModel.toggleFavourite()
            }

            playerAddToMediaButton.setOnClickListener {
                viewModel.toggleMedia()
            }

            trackInfoTrackTimeItem.infoHeaderTextView.text =
                this@PlayerActivity.getText(R.string.player_track_info_track_time)
            trackInfoAlbumItem.infoHeaderTextView.text =
                this@PlayerActivity.getText(R.string.player_track_info_album)
            trackInfoReleaseDateItem.infoHeaderTextView.text =
                this@PlayerActivity.getText(R.string.player_track_info_release_date)
            trackInfoGenreItem.infoHeaderTextView.text =
                this@PlayerActivity.getText(R.string.player_track_info_genre)
            trackInfoCountryItem.infoHeaderTextView.text =
                this@PlayerActivity.getText(R.string.player_track_info_country)
        }
    }

    private fun applyState(state: PlayerViewModel.State) {
        with(viewBinding) {
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
                    this@PlayerActivity.getText(R.string.player_no_data)
            } else {
                trackInfoReleaseDateItem.infoBodyTextView.text = item.releaseDate
            }

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

    private fun observeData() {
        viewModel.currentState.observe(this) {
            applyState(it)
        }
    }

    private fun setButtonDrawable(
        button: Button,
        buttonState: Boolean,
        @DrawableRes positive: Int,
        @DrawableRes negative: Int
    ) {
        val view = when (button) {
            Button.MEDIA -> {
                viewBinding.playerAddToMediaButton
            }

            Button.FAVOURITE -> {
                viewBinding.playerAddToFavouriteButton
            }

            Button.PLAYER -> {
                viewBinding.playerPausePlayButton
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

    override fun onStop() {
        viewModel.stopPlayerScreen()
        super.onStop()
    }

    override fun onDestroy() {
        viewModel.resetPlayerScreen(isFinishing)
        super.onDestroy()
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
    }
}