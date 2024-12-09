package com.example.playlist_maker.presentation.library.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker.R
import com.example.playlist_maker.databinding.ItemLibraryPlaylistBinding
import com.example.playlist_maker.presentation.library.dto.PlaylistLibraryItem
import com.example.playlist_maker.utils.dpToPx

class LibraryPlaylistAdapter(
    private val onItemClickListener: (item: PlaylistLibraryItem) -> Unit
) : RecyclerView.Adapter<LibraryPlaylistAdapter.ViewHolder>() {

    private var items: List<PlaylistLibraryItem> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<PlaylistLibraryItem>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemLibraryPlaylistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(private val binding: ItemLibraryPlaylistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PlaylistLibraryItem) {
            with(binding) {
                val totalTracks = item.trackCount.toString() + " ${
                    playlistTotalTracks.resources.getQuantityString(
                        R.plurals.tracks,
                        item.trackCount
                    )
                }"

                Glide.with(playlistCover)
                    .load(item.imageUri)
                    .placeholder(R.drawable.placeholder_album)
                    .fitCenter()
                    .transform(RoundedCorners(dpToPx(2f, playlistCover.context)))
                    .into(playlistCover)

                playlistName.text = item.name
                playlistTotalTracks.text = totalTracks
                playlistName.invalidate()
                playlistName.requestLayout()

                playlistTotalTracks.invalidate()
                playlistTotalTracks.requestLayout()

                root.setOnClickListener {
                    onItemClickListener.invoke(item)
                }
            }
        }
    }
}