package com.example.playlist_maker.presentation.playlist_create.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker.R
import com.example.playlist_maker.databinding.FragmentCreatePlaylistBinding
import com.example.playlist_maker.presentation.common.BaseFragment
import com.example.playlist_maker.presentation.playlist_create.view_model.CreatePlaylistViewModel
import com.example.playlist_maker.utils.GalleryHandler
import com.example.playlist_maker.utils.dpToPx
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFragment : BaseFragment<FragmentCreatePlaylistBinding>() {
    private val viewModel by viewModel<CreatePlaylistViewModel>()
    private lateinit var galleryHandler: GalleryHandler
    private val alertDialog by lazy {
        MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlert)
            .setTitle(resources.getText(R.string.alert_dialog_title))
            .setMessage(resources.getText(R.string.alert_dialog_body))
            .setNegativeButton(resources.getText(R.string.alert_dialog_negative_button)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getText(R.string.alert_dialog_positive_button)) { _, _ ->
                this@CreatePlaylistFragment.findNavController().popBackStack()
            }
    }
    private val nameFieldTextWatcher: TextWatcher by lazy {
        object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrBlank()) {
                    viewBinding.newPlaylistButton.isEnabled = s.isNotEmpty()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        galleryHandler = GalleryHandler(this) {
            viewModel.saveImageUri(it)
        }
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleWarningDialog()
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun createViewBinding(): FragmentCreatePlaylistBinding {
        return FragmentCreatePlaylistBinding.inflate(layoutInflater)
    }

    override fun initUi() {
        with(viewBinding) {
            (activity as AppCompatActivity).setSupportActionBar(newPlaylistToolbar)
            (activity as AppCompatActivity).supportActionBar?.title =
                resources.getText(R.string.new_playlist_title)
            newPlaylistToolbar.setNavigationOnClickListener {
                handleWarningDialog()
            }

            newPlaylistImage.setOnClickListener { galleryHandler.selectImage() }
            newPlaylistNameEditText.addTextChangedListener(nameFieldTextWatcher)
            newPlaylistButton.setOnClickListener {
                viewModel.savePlaylist(
                    name = newPlaylistNameEditText.text.toString(),
                    description = newPlaylistDescriptionEditText.text?.toString() ?: ""
                )
            }
        }
    }

    override fun observeData() {
        viewModel.currentPlaylistCover.observe(viewLifecycleOwner) {
            Glide.with(viewBinding.newPlaylistImage)
                .load(it)
                .fitCenter()
                .transform(RoundedCorners(dpToPx(8f, viewBinding.newPlaylistImage.context)))
                .into(viewBinding.newPlaylistImage)
        }

        viewModel.creationEvent.observe(viewLifecycleOwner) {
            val snackBar = Snackbar.make(
                requireContext(),
                viewBinding.root,
                resources.getString(
                    R.string.create_playlist_success_toast,
                    viewBinding.newPlaylistNameEditText.text
                ),
                Snackbar.LENGTH_SHORT
            )
            snackBar.setBackgroundTint(
                requireContext().resources.getColor(
                    R.color.snackbar_bg,
                    null
                )
            )
            snackBar.setTextColor(requireContext().resources.getColor(R.color.snackbar_text, null))
            val textView = snackBar
                .view
                .findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            snackBar.show()
            this@CreatePlaylistFragment.findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        super.onDestroyView()
    }

    private fun handleWarningDialog() {
        with(viewBinding) {
            if (
                !newPlaylistNameEditText.text.isNullOrBlank() ||
                viewModel.currentPlaylistCover.value != null ||
                !newPlaylistDescriptionEditText.text.isNullOrBlank()
            ) {
                alertDialog.show()
            } else {
                this@CreatePlaylistFragment.findNavController().popBackStack()
            }
        }
    }
}
