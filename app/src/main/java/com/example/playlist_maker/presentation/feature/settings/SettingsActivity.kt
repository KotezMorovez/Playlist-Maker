package com.example.playlist_maker.presentation.feature.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.example.playlist_maker.App
import com.example.playlist_maker.R
import com.example.playlist_maker.databinding.ActivitySettingsBinding
import com.example.playlist_maker.di.Injector
import com.example.playlist_maker.domain.interactors.ThemeInteractor

class SettingsActivity: AppCompatActivity() {
    private lateinit var viewBinding: ActivitySettingsBinding
    private val themeInteractor: ThemeInteractor = Injector.getThemeInteractor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        initUi()
    }

    private fun initUi() {
        with(viewBinding) {
            this@SettingsActivity.setSupportActionBar(settingsToolbar)
            this@SettingsActivity.supportActionBar?.title =
                resources.getText(R.string.settings_title)
            settingsToolbar.setNavigationOnClickListener {
                this@SettingsActivity.onBackPressedDispatcher.onBackPressed()
            }

            darkThemeSwitch.isChecked = AppCompatDelegate.getDefaultNightMode() == MODE_NIGHT_YES

            darkThemeSwitch.setOnCheckedChangeListener { _, checked ->
                themeInteractor.saveTheme(checked)
                (applicationContext as App).switchTheme(checked)
            }

            share.setOnClickListener {
                val text = resources.getText(R.string.share_message).toString()
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.putExtra(Intent.EXTRA_TEXT, text)
                shareIntent.type = SEND_INTENT_TYPE

                val chooserIntent = Intent.createChooser(shareIntent, null)
                startActivity(chooserIntent)
            }

            support.setOnClickListener {
                val supportIntent = Intent(Intent.ACTION_SENDTO)
                val email: Array<String> = arrayOf(resources.getText(R.string.email_to).toString())
                val message = resources.getText(R.string.email_message)
                val subject = resources.getText(R.string.email_subject)

                supportIntent.data = Uri.parse(SEND_INTENT_DATA)
                supportIntent.putExtra(Intent.EXTRA_EMAIL, email)
                supportIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
                supportIntent.putExtra(Intent.EXTRA_TEXT, message)

                startActivity(supportIntent)
            }

            agreement.setOnClickListener {
                val url = resources.getText(R.string.agreement_url).toString()
                val agreementIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(agreementIntent)
            }
        }
    }

    companion object {
        private const val SEND_INTENT_TYPE = "text/plain"
        private var SEND_INTENT_DATA = "mailto:"
    }
}