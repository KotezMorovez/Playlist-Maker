package com.example.playlist_maker.ui

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker.R
import com.example.playlist_maker.common.dpToPx
import com.example.playlist_maker.databinding.ActivityPlayerBinding
import com.example.playlist_maker.ui.search.adapter.TrackItem

class PlayerActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityPlayerBinding
    private var item: TrackItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        item = if (Build.VERSION.SDK_INT < 33) {
            intent.extras?.getParcelable(TRACK)
        } else {
            intent.extras?.getParcelable(TRACK, TrackItem::class.java)
        }
        initUi()
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


            playerTrackNameTextView.text = item?.trackName
            playerArtistNameTextView.text = item?.artistName
            playerTrackTimeTextView.text = timer


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

    companion object {
        private const val DELIMITER = '/'
        private const val ALBUM_SIZE = "512x512bb.jpg"
        private val timer = "00:00"
        private const val EMPTY_STRING = ""
        private const val TRACK = "track"
    }
}