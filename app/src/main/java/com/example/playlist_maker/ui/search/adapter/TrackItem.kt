package com.example.playlist_maker.ui.search.adapter

import android.os.Parcel
import android.os.Parcelable

data class TrackItem(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val artworkUrl100: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(trackId)
        parcel.writeString(trackName)
        parcel.writeString(artistName)
        parcel.writeString(trackTime)
        parcel.writeString(collectionName)
        parcel.writeString(releaseDate)
        parcel.writeString(primaryGenreName)
        parcel.writeString(country)
        parcel.writeString(artworkUrl100)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TrackItem> {
        override fun createFromParcel(parcel: Parcel): TrackItem {
            return TrackItem(parcel)
        }

        override fun newArray(size: Int): Array<TrackItem?> {
            return arrayOfNulls(size)
        }
    }

}