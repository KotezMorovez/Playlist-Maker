package com.example.playlist_maker.presentation.feature.player

import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker.R
import com.example.playlist_maker.utils.Timer
import com.example.playlist_maker.utils.dpToPx
import com.example.playlist_maker.databinding.ActivityPlayerBinding
import com.example.playlist_maker.utils.AudioPlayer
import com.example.playlist_maker.presentation.models.TrackItem

class PlayerActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityPlayerBinding
    private var item: TrackItem? = null
    private var isPlay: Boolean = false
    private var isFavourite: Boolean = false
    private var isInMedia: Boolean = false
    private val player = AudioPlayer()
    private lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        item = if (Build.VERSION.SDK_INT < 33) {
            intent.extras?.getParcelable(TRACK)
        } else {
            intent.extras?.getParcelable(TRACK, TrackItem::class.java)
        }

        timer = Timer(TIMER_DELAY, PREVIEW_TIME) { timeLeft ->
            val timeLeftSeconds = Math.round(timeLeft / 1000f).toLong()

            viewBinding.playerTrackTimeTextView.text =
                String.format("%02d:%02d", timeLeftSeconds / 60, timeLeftSeconds)

            if (timer.state == Timer.State.IDLE) {
                setButtonDrawable(
                    Button.PLAYER,
                    R.drawable.ic_pause,
                    R.drawable.ic_play,
                )
            }
        }

        player.preparePlayer(item?.previewUrl ?: "")
        initUi()
    }

    private fun setButtonDrawable(
        button: Button,
        @DrawableRes positive: Int,
        @DrawableRes negative: Int
    ) {
        val view: ImageView
        val state: Boolean

        when (button) {
            Button.MEDIA -> {
                view = viewBinding.playerAddToMediaButton
                state = isInMedia
                isInMedia = !isInMedia
            }

            Button.FAVOURITE -> {
                view = viewBinding.playerAddToFavouriteButton
                state = isFavourite
                isFavourite = !isFavourite
            }

            Button.PLAYER -> {
                view = viewBinding.playerPausePlayButton
                state = isPlay
                isPlay = !isPlay
            }
        }

        view.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                if (state) negative else positive,
                null
            )
        )
    }

    private fun initUi() {
        with(viewBinding) {
            this@PlayerActivity.setSupportActionBar(playerToolbar)
            this@PlayerActivity.supportActionBar?.title = EMPTY_STRING
            playerToolbar.setNavigationOnClickListener {
                this@PlayerActivity.onBackPressedDispatcher.onBackPressed()
            }

            val album = item?.artworkUrl100?.replaceAfterLast(DELIMITER, ALBUM_SIZE)
            Glide.with(playerAlbumImageView)
                .load(album)
                .placeholder(R.drawable.placeholder_album)
                .fitCenter()
                .transform(RoundedCorners(dpToPx(8f, playerAlbumImageView.context)))
                .into(playerAlbumImageView)

            playerPausePlayButton.setOnClickListener {
                togglePlayButton()
            }

            playerAddToFavouriteButton.setOnClickListener {
                setButtonDrawable(
                    Button.FAVOURITE,
                    R.drawable.ic_favourite_active,
                    R.drawable.ic_favourite_inactive,
                )
            }

            playerAddToMediaButton.setOnClickListener {
                setButtonDrawable(
                    Button.MEDIA,
                    R.drawable.ic_already_added_to_media,
                    R.drawable.ic_add_to_media,
                )
            }

            playerTrackNameTextView.text = item?.trackName
            playerArtistNameTextView.text = item?.artistName

            trackInfoTrackTimeItem.infoHeaderTextView.text =
                this@PlayerActivity.getText(R.string.player_track_info_track_time)
            trackInfoTrackTimeItem.infoBodyTextView.text = item?.trackTime

            if (item?.collectionName.isNullOrEmpty()) {
                trackInfoAlbumItem.infoHeaderTextView.isGone = true
                trackInfoAlbumItem.infoBodyTextView.isGone = true

            } else {
                trackInfoAlbumItem.infoHeaderTextView.isVisible = true
                trackInfoAlbumItem.infoBodyTextView.isVisible = true
                trackInfoAlbumItem.infoHeaderTextView.text =
                    this@PlayerActivity.getText(R.string.player_track_info_album)
                trackInfoAlbumItem.infoBodyTextView.text = item?.collectionName
            }

            trackInfoReleaseDateItem.infoHeaderTextView.text =
                this@PlayerActivity.getText(R.string.player_track_info_release_date)
            trackInfoReleaseDateItem.infoBodyTextView.text = item?.releaseDate

            trackInfoGenreItem.infoHeaderTextView.text =
                this@PlayerActivity.getText(R.string.player_track_info_genre)
            trackInfoGenreItem.infoBodyTextView.text = item?.primaryGenreName

            trackInfoCountryItem.infoHeaderTextView.text =
                this@PlayerActivity.getText(R.string.player_track_info_country)
            trackInfoCountryItem.infoBodyTextView.text = item?.country
        }
    }

    private fun togglePlayButton() {
        setButtonDrawable(
            Button.PLAYER,
            R.drawable.ic_pause,
            R.drawable.ic_play,
        )

        if (isPlay) {
            player.applyState(AudioPlayer.State.STARTED)
            timer.run()
        } else {
            player.applyState(AudioPlayer.State.PAUSED)
            timer.pause()
        }
    }

    override fun onStop() {
        togglePlayButton()
        super.onStop()
    }

    override fun onDestroy() {
        player.applyState(AudioPlayer.State.RELEASED)
        timer.reset()
        super.onDestroy()
    }

    companion object {
        private const val DELIMITER = '/'
        private const val ALBUM_SIZE = "512x512bb.jpg"
        private const val PREVIEW_TIME = 30000L
        private const val TIMER_DELAY = 1000L
        private const val EMPTY_STRING = ""
        private const val TRACK = "track"
    }

    enum class Button {
        MEDIA,
        PLAYER,
        FAVOURITE;
    }
}