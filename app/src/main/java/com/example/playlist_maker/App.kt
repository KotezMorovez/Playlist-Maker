package com.example.playlist_maker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlist_maker.di.Injector

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.initializeDependencyWithContext(this)
        val themeInteractor = Injector.getThemeInteractor()

        val firstLaunchFlag = themeInteractor.getFirstLaunchFlag()

        val darkTheme = if (firstLaunchFlag) {
            themeInteractor.disableFirstLaunchFlag()
            val config = this.resources
                ?.configuration
                ?.uiMode
                ?.and(Configuration.UI_MODE_NIGHT_MASK)

            val darkMode =
                when (config) {
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
            themeInteractor.saveTheme(darkMode)
            darkMode
        } else {
            themeInteractor.getTheme()
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
}