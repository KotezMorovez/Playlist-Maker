package com.example.playlist_maker.ui

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker.R
import com.example.playlist_maker.common.GlobalConstants.EMPTY_STRING
import com.example.playlist_maker.common.GlobalConstants.TRACK
import com.example.playlist_maker.common.dpToPx
import com.example.playlist_maker.databinding.ActivityPlayerBinding
import com.example.playlist_maker.ui.search.adapter.TrackItem

class PlayerActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityPlayerBinding
    private var item: TrackItem? = null
    private var

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
            playerTrackTimeTextView.text = item?.trackTime

            infoItem1.infoHeaderTextView.text = this@PlayerActivity.getText(R.string.player_info1)
            infoItem1.infoBodyTextView.text = timer

            if (item?.collectionName.isNullOrEmpty()) {
                infoItem2.infoHeaderTextView.isGone = true
                infoItem2.infoBodyTextView.isGone = true

            } else {
                infoItem2.infoHeaderTextView.isVisible = true
                infoItem2.infoBodyTextView.isVisible = true
                infoItem2.infoHeaderTextView.text =
                    this@PlayerActivity.getText(R.string.player_info2)
                infoItem2.infoBodyTextView.text = item?.collectionName
            }

            infoItem3.infoHeaderTextView.text = this@PlayerActivity.getText(R.string.player_info3)
            infoItem3.infoBodyTextView.text = item?.releaseDate

            infoItem4.infoHeaderTextView.text = this@PlayerActivity.getText(R.string.player_info4)
            infoItem4.infoBodyTextView.text = item?.primaryGenreName

            infoItem5.infoHeaderTextView.text = this@PlayerActivity.getText(R.string.player_info5)
            infoItem5.infoBodyTextView.text = item?.country
        }
    }

    companion object {
        private const val DELIMITER = '/'
        private const val ALBUM_SIZE = "512x512bb.jpg"
        private val timer = "00:00"
    }
}