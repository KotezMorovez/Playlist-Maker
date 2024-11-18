package com.example.playlist_maker.presentation.library.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker.R
import com.example.playlist_maker.databinding.ItemTrackBinding
import com.example.playlist_maker.presentation.library.dto.TrackLibraryItem
import com.example.playlist_maker.utils.dpToPx

class LibraryFavTracksAdapter(
    private val onItemClickListener: (item: TrackLibraryItem) -> Unit
) : RecyclerView.Adapter<LibraryFavTracksAdapter.ViewHolder>() {
    private var items: List<TrackLibraryItem> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<TrackLibraryItem>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemTrackBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(private val binding: ItemTrackBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TrackLibraryItem) {
            with(binding) {
                trackNameTextView.text = item.trackName
                artistNameTextView.text = item.artistName
                trackTimeTextView.text = item.trackTime
                Glide.with(trackImageView)
                    .load(item.artworkUrl100)
                    .placeholder(R.drawable.placeholder_album)
                    .fitCenter()
                    .transform(RoundedCorners(dpToPx(2f, trackImageView.context)))
                    .into(trackImageView)

                artistNameTextView.invalidate()
                artistNameTextView.requestLayout()

                root.setOnClickListener {
                    onItemClickListener.invoke(item)
                }
            }
        }
    }


}