package com.example.playlist_maker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {
    private var darkTheme = false
    private var sharedPrefs: SharedPreferences? = null

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPrefs?.getBoolean(IS_DARK_THEME, false)!!
        switchTheme(darkTheme)
    }

    fun switchTheme(isDarkTheme: Boolean) {
        darkTheme = isDarkTheme
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun saveTheme() {
        sharedPrefs?.edit()!!
            .putBoolean(IS_DARK_THEME, darkTheme)
            .apply()
    }

    companion object {
        private const val THEME_PREFERENCES = "theme_preferences"
        private const val IS_DARK_THEME = "is_dark_theme"
    }
}