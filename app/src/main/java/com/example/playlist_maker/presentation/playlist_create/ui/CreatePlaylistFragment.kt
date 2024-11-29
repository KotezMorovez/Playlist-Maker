package com.example.playlist_maker.presentation.playlist_create.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.playlist_maker.R
import com.example.playlist_maker.databinding.FragmentCreatePlaylistBinding
import com.example.playlist_maker.presentation.common.BaseFragment
import com.example.playlist_maker.presentation.playlist_create.view_model.CreatePlaylistViewModel
import com.example.playlist_maker.utils.GalleryHandler
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFragment : BaseFragment<FragmentCreatePlaylistBinding>() {
    private val viewModel by viewModel<CreatePlaylistViewModel>()
    private lateinit var galleryHandler: GalleryHandler
    private val nameFieldTextWatcher: TextWatcher by lazy {
        object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewBinding.newPlaylistButton.isEnabled = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        galleryHandler = GalleryHandler(this) {
            viewModel.uploadImage(it, requireContext().contentResolver)
        }
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        super.onCreate(savedInstanceState)
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
                requireActivity().onBackPressedDispatcher.onBackPressed()
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
        viewModel.currentPlaylistCover.observe(viewLifecycleOwner){
            viewBinding.newPlaylistImage.setImageBitmap(it)
        }
    }

    override fun onDestroyView() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        super.onDestroyView()
    }
}
