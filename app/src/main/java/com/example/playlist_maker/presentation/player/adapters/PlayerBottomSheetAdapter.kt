package com.example.playlist_maker.presentation.player.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker.R
import com.example.playlist_maker.databinding.ItemBottomSheetPlaylistBinding
import com.example.playlist_maker.presentation.player.dto.PlaylistPlayerItem
import com.example.playlist_maker.utils.dpToPx

class PlayerBottomSheetAdapter(
    private val onItemClickListener: (item: PlaylistPlayerItem) -> Unit
) : RecyclerView.Adapter<PlayerBottomSheetAdapter.ViewHolder>() {
    private var items: List<PlaylistPlayerItem> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<PlaylistPlayerItem>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemBottomSheetPlaylistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(private val binding: ItemBottomSheetPlaylistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PlaylistPlayerItem) {
            with(binding) {
                val totalTracks = item.tracksCount.toString() + " ${
                    tracksCountTextView.resources.getQuantityString(
                        R.plurals.tracks,
                        item.tracksCount
                    )
                }"

                Glide.with(playlistCoverImageView)
                    .load(item.coverImageURI)
                    .placeholder(R.drawable.placeholder_album)
                    .fitCenter()
                    .transform(RoundedCorners(dpToPx(2f, playlistCoverImageView.context)))
                    .into(playlistCoverImageView)

                playlistNameTextView.text = item.name
                tracksCountTextView.text = totalTracks

                playlistNameTextView.invalidate()
                playlistNameTextView.requestLayout()

                tracksCountTextView.invalidate()
                tracksCountTextView.requestLayout()

                root.setOnClickListener {
                    onItemClickListener.invoke(item)
                }
            }
        }
    }
}