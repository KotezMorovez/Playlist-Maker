package com.example.playlist_maker

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)

        val firstLaunchFlag = sharedPrefs.getBoolean(IS_FIRST_LAUNCH, true)

        val darkTheme = if (firstLaunchFlag) {
            sharedPrefs.edit().putBoolean(IS_FIRST_LAUNCH, false).apply()

            val darkMode = when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    true
                }

                Configuration.UI_MODE_NIGHT_NO -> {
                    false
                }

                else -> {
                    false
                }
            }
            saveTheme(darkMode)
            darkMode
        } else {
            sharedPrefs.getBoolean(IS_DARK_THEME, false)
        }

        switchTheme(darkTheme)
    }

    fun switchTheme(isDarkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun saveTheme(isDarkTheme: Boolean) {
        sharedPrefs.edit()
            .putBoolean(IS_DARK_THEME, isDarkTheme)
            .apply()
    }

    companion object {
        private const val PREFERENCES = "playlist_maker_shared_preferences"
        private const val IS_FIRST_LAUNCH = "is_first_launch"
        private const val IS_DARK_THEME = "is_dark_theme"
    }
}