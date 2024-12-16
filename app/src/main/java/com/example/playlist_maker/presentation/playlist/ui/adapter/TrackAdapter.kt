package com.example.playlist_maker.presentation.playlist.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker.R
import com.example.playlist_maker.databinding.ItemTrackBinding
import com.example.playlist_maker.presentation.search.dto.TrackItem
import com.example.playlist_maker.utils.dpToPx

class TrackAdapter(
    private val onItemClickListener: (item: TrackItem) -> Unit,
    private val onItemLongClickListener: (item: TrackItem) -> Unit,
) : RecyclerView.Adapter<TrackAdapter.ViewHolder>() {
    private var items: List<TrackItem> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<TrackItem>) {
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
        fun bind(item: TrackItem) {
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
                root.setOnLongClickListener {
                    onItemLongClickListener.invoke(item)
                    true
                }
            }
        }
    }
}