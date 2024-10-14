package com.example.playlist_maker.presentation.settings.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.example.playlist_maker.App
import com.example.playlist_maker.R
import com.example.playlist_maker.databinding.FragmentSettingsBinding
import com.example.playlist_maker.presentation.common.BaseFragment
import com.example.playlist_maker.presentation.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    private val viewModel by viewModel <SettingsViewModel>()

    override fun createViewBinding(): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(layoutInflater)
    }

    override fun initUi() {
        with(viewBinding) {
            (activity as AppCompatActivity).setSupportActionBar(settingsToolbar)
            (activity as AppCompatActivity).supportActionBar?.title =
                resources.getText(R.string.settings_title)

            viewModel.isDarkTheme()

            darkThemeSwitch.setOnCheckedChangeListener { _, checked ->
                viewModel.saveCurrentTheme(checked)

                (requireActivity().applicationContext as App).switchTheme(checked)
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

    override fun observeData() {
        viewModel.themeState.observe(this) {
            viewBinding.darkThemeSwitch.isChecked = it
        }
    }

    companion object {
        private const val SEND_INTENT_TYPE = "text/plain"
        private var SEND_INTENT_DATA = "mailto:"
    }
}