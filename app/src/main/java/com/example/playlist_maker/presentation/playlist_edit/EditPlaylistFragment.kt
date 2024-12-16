package com.example.playlist_maker.presentation.playlist_edit

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker.R
import com.example.playlist_maker.databinding.FragmentCreatePlaylistBinding
import com.example.playlist_maker.presentation.common.BaseFragment
import com.example.playlist_maker.utils.GalleryHandler
import com.example.playlist_maker.utils.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : BaseFragment<FragmentCreatePlaylistBinding>() {
    private val viewModel by viewModel<EditPlaylistViewModel>()
    private lateinit var galleryHandler: GalleryHandler
    private val nameFieldTextWatcher: TextWatcher by lazy {
        object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewBinding.newPlaylistButton.isEnabled = !s.isNullOrBlank()
            }

            override fun afterTextChanged(s: Editable?) {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        galleryHandler = GalleryHandler(this) {
            viewModel.saveImageUri(it)
        }
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val id: String? = arguments?.getString(PLAYLIST_ID)

        if (id != null) {
            viewModel.initialize(id)
        }
    }

    override fun createViewBinding(): FragmentCreatePlaylistBinding {
        return FragmentCreatePlaylistBinding.inflate(layoutInflater)
    }

    override fun initUi() {
        with(viewBinding) {
            (activity as AppCompatActivity).setSupportActionBar(newPlaylistToolbar)
            (activity as AppCompatActivity).supportActionBar?.title =
                resources.getText(R.string.edit_playlist_title)

            newPlaylistToolbar.setNavigationOnClickListener {
                this@EditPlaylistFragment.findNavController().popBackStack()
            }

            newPlaylistImage.setOnClickListener { galleryHandler.selectImage() }
            newPlaylistNameEditText.addTextChangedListener(nameFieldTextWatcher)
            newPlaylistButton.setText(R.string.edit_playlist_button)
            newPlaylistButton.setOnClickListener {
                viewModel.savePlaylist(
                    newPlaylistNameEditText.text.toString(),
                    newPlaylistDescriptionEditText.text.toString()
                )
            }
            newPlaylistButton.isEnabled = true
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

        viewModel.playlistInfoEvent.observe(viewLifecycleOwner) { info ->
            with(viewBinding) {
                newPlaylistNameEditText.removeTextChangedListener(nameFieldTextWatcher)
                newPlaylistNameEditText.setText(info.name)
                newPlaylistNameEditText.addTextChangedListener(nameFieldTextWatcher)

                newPlaylistDescriptionEditText.setText(info.description)
            }
        }

        viewModel.creationEvent.observe(viewLifecycleOwner) {
            this@EditPlaylistFragment.findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        super.onDestroyView()
    }

    companion object {
        private const val PLAYLIST_ID = "playlist_id"
    }
}