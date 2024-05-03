package com.example.playlist_maker

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.playlist_maker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
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

            supportButton.setOnClickListener {
                Toast.makeText(
                    this@SettingsActivity,
                    R.string.settings_support_toast,
                    Toast.LENGTH_SHORT
                ).show()
            }

            shareButton.setOnClickListener {
                Toast.makeText(
                    this@SettingsActivity,
                    R.string.settings_share_toast,
                    Toast.LENGTH_SHORT
                ).show()
            }

            agreementButton.setOnClickListener {
                Toast.makeText(
                    this@SettingsActivity,
                    R.string.settings_agreement_toast,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}