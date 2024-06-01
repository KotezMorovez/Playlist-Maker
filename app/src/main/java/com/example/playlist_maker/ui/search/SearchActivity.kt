package com.example.playlist_maker.ui.search

import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlist_maker.R
import com.example.playlist_maker.data.TrackService
import com.example.playlist_maker.databinding.ActivitySearchBinding
import com.example.playlist_maker.ui.search.adapter.SearchAdapter


class SearchActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivitySearchBinding
    private var searchRequest = EMPTY_STRING
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s.isNullOrEmpty()) {
                viewBinding.searchFieldClearButton.isVisible = false

            } else {
                viewBinding.searchFieldClearButton.isVisible = true
            }
        }

        override fun afterTextChanged(s: Editable?) {
            val outState = Bundle()
            outState.putString(REQUEST, s.toString())
            onSaveInstanceState(outState)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        initUi()
    }

    private fun initUi() {
        with(viewBinding) {
            this@SearchActivity.setSupportActionBar(searchToolbar)
            this@SearchActivity.supportActionBar?.title = resources.getText(R.string.search_title)
            searchToolbar.setNavigationOnClickListener {
                this@SearchActivity.onBackPressedDispatcher.onBackPressed()
            }

            searchEditText.addTextChangedListener(textWatcher)
            searchEditText.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val imm =
                        searchEditText.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
                    return@setOnEditorActionListener true
                } else {
                    return@setOnEditorActionListener false
                }
            }

            searchFieldClearButton.setOnClickListener {
                searchEditText.setText(EMPTY_STRING)
                searchRequest = EMPTY_STRING
                searchEditText.clearFocus()
                val imm =
                    searchEditText.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
            }

            val adapter = SearchAdapter()
            adapter.setItems(TrackService.getTracks())
            searchRecyclerView.adapter = adapter
            searchRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(REQUEST, searchRequest)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?, persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)

        if (savedInstanceState != null) {
            searchRequest = savedInstanceState.getString(REQUEST, searchRequest)
            viewBinding.searchEditText.setText(searchRequest)
        }
    }

    companion object {
        private const val EMPTY_STRING = ""
        private const val REQUEST = "search"
    }

}