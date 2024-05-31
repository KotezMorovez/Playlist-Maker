package com.example.playlist_maker.ui.search.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker.R
import com.example.playlist_maker.common.dpToPx
import com.example.playlist_maker.databinding.ItemSearchBinding

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    private var items: List<TrackItem> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<TrackItem>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(private val binding: ItemSearchBinding) :
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
            }
        }
    }
}