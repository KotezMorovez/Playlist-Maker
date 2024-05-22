package com.example.playlist_maker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlist_maker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    companion object {
        private const val SEND_INTENT_TYPE = "text/plain"
        private const val SEND_INTENT_DATA = "mailto:"
    }

    private lateinit var viewBinding: ActivitySettingsBinding

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

            share.setOnClickListener {
                val text = resources.getText(R.string.share_message).toString()
                val sendIntent = Intent(Intent.ACTION_SEND)
                sendIntent.putExtra(Intent.EXTRA_TEXT, text)
                sendIntent.type = SEND_INTENT_TYPE

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }

            support.setOnClickListener {
                val message = resources.getText(R.string.email_message)
                val subject = resources.getText(R.string.email_subject)
                val supportIntent = Intent(Intent.ACTION_SENDTO)

                supportIntent.data = Uri.parse(SEND_INTENT_DATA)
                supportIntent.putExtra(
                    Intent.EXTRA_EMAIL,
                    arrayOf(resources.getText(R.string.email_to))
                )
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
}