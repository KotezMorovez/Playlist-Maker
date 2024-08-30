package com.example.playlist_maker.data.service

import android.content.SharedPreferences
import com.example.playlist_maker.data.model.TrackEntity
import com.example.playlist_maker.utils.JSONConverter

interface PrefsStorage {
    fun getHistory(): List<TrackEntity>
    fun setHistory(list: List<TrackEntity>)
    fun clearHistory()
    fun isHistoryEmpty(): Boolean
    fun getFirstLaunchFlag(): Boolean
    fun setTheme(isDarkTheme: Boolean)
    fun disableFirstLaunchFlag()
    fun getTheme(): Boolean
}

class PrefsStorageImpl(
    private val sharedPrefs: SharedPreferences
) : PrefsStorage {
    override fun getHistory(): List<TrackEntity> {
        val json = sharedPrefs.getString(HISTORY, "") ?: ""
        return JSONConverter().unpackListFromJSON(json)
    }

    override fun setHistory(list: List<TrackEntity>) {
        sharedPrefs.edit()
            .putString(HISTORY, JSONConverter().packListToJSON(list))
            .apply()
    }

    override fun clearHistory() {
        sharedPrefs.edit().clear().apply()
    }

    override fun isHistoryEmpty(): Boolean {
        val list = sharedPrefs.getString(HISTORY, "")
        return list.isNullOrEmpty()
    }

    override fun getFirstLaunchFlag(): Boolean {
        return sharedPrefs.getBoolean(IS_FIRST_LAUNCH, true)
    }

    override fun disableFirstLaunchFlag() {
        sharedPrefs.edit().putBoolean(IS_FIRST_LAUNCH, false).apply()
    }

    override fun setTheme(isDarkTheme: Boolean) {
        sharedPrefs.edit()
            .putBoolean(IS_DARK_THEME, isDarkTheme)
            .apply()
    }

    override fun getTheme(): Boolean {
        return sharedPrefs.getBoolean(IS_DARK_THEME, false)
    }

    companion object {
        private const val HISTORY = "history"
        private const val IS_FIRST_LAUNCH = "is_first_launch"
        private const val IS_DARK_THEME = "is_dark_theme"
    }
}