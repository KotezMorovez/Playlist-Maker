package com.example.playlist_maker.ui.search.common

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlist_maker.common.JSONConverter
import com.example.playlist_maker.ui.search.adapter.TrackItem

object HistoryManager {
    private const val PREFERENCES = "playlist_maker_shared_preferences"
    private const val HISTORY = "history"
    private var sharedPrefs: SharedPreferences? = null

    fun initHistoryStorage(context: Context) {
        sharedPrefs = context.getSharedPreferences(PREFERENCES, MODE_PRIVATE)
    }

    fun getHistory(): List<TrackItem> {
        val json = sharedPrefs?.getString(HISTORY, "") ?: ""
        return JSONConverter().unpackListFromJSON(json)
    }

    fun updateList(track: TrackItem): List<TrackItem> {
        val json = sharedPrefs?.getString(HISTORY, "")
        var list = listOf<TrackItem>()

        if (json != null) {
            list = JSONConverter().unpackListFromJSON<TrackItem>(json)

            list = listOf(track) + list.filter { track.trackId != it.trackId }.take(9)
        }

        sharedPrefs?.edit()!!
            .putString(HISTORY, JSONConverter().packListToJSON(list))
            .apply()

        return list
    }

    fun clearHistory() {
        sharedPrefs?.edit()!!.clear().apply()
    }

    fun isHistoryEmpty(): Boolean {
        val list = sharedPrefs?.getString(HISTORY, "")
        return list.isNullOrEmpty()
    }
}