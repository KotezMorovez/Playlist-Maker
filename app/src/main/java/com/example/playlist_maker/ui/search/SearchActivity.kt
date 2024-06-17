package com.example.playlist_maker.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlist_maker.R
import com.example.playlist_maker.data.repository.SearchRepositoryImpl
import com.example.playlist_maker.databinding.ActivitySearchBinding
import com.example.playlist_maker.domain.model.request.SearchRequest
import com.example.playlist_maker.ui.search.adapter.SearchAdapter
import com.example.playlist_maker.ui.search.adapter.TrackItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale


class SearchActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivitySearchBinding
    private var searchRequest = EMPTY_STRING
    private var state = State.DATA
    private val searchRepo = SearchRepositoryImpl.getInstance()
    private val adapter = SearchAdapter()
    private var loadTracksJob: Job? = null
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            viewBinding.searchFieldClearButton.isInvisible = s.isNullOrEmpty()
        }

        override fun afterTextChanged(s: Editable?) {
            searchRequest = s.toString()
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

            val imm =
                searchEditText.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

            searchEditText.addTextChangedListener(textWatcher)
            searchEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
                    loadTracks(searchEditText.text.toString())
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

            searchFieldClearButton.setOnClickListener {
                searchEditText.setText(EMPTY_STRING)
                searchRequest = EMPTY_STRING
                searchEditText.clearFocus()
                imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
                adapter.setItems(listOf())

                state = State.DATA
                applyState(state)
            }

            searchRecyclerView.adapter = adapter
            searchRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
        }
    }

    private fun loadTracks(text: String) {
        loadTracksJob?.cancel()

        loadTracksJob = CoroutineScope(Dispatchers.IO).launch {
            val result = searchRepo.getTracks(
                SearchRequest(
                    text = text
                )
            )

            if (result.isSuccess) {
                val response = result.getOrNull()
                if (response?.resultCount != 0) {
                    val tracks = response?.tracks?.map {
                        TrackItem(
                            trackName = it.trackName,
                            artistName = it.artistName,
                            trackTime = SimpleDateFormat(
                                "mm:ss",
                                Locale.getDefault()
                            ).format(it.trackTimeMillis),
                            artworkUrl100 = it.artworkUrl100
                        )
                    } ?: listOf()

                    withContext(Dispatchers.Main) {
                        state = State.DATA
                        applyState(state)
                        adapter.setItems(tracks)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        state = State.NO_DATA
                        applyState(state)
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    state = State.ERROR
                    applyState(state)

                    viewBinding.refreshButton.setOnClickListener {
                        loadTracks(viewBinding.searchEditText.text.toString())
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(REQUEST, searchRequest)
        outState.putInt(STATE, state.ordinal)

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        with(viewBinding) {
            searchRequest = savedInstanceState.getString(REQUEST, searchRequest)
            state = State.fromIndex(savedInstanceState.getInt(STATE))

            searchEditText.setText(searchRequest)
            applyState(state)
        }

        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun applyState(state: State) {
        with(viewBinding) {
            searchRecyclerView.isGone = true
            noInternetErrorContainer.isGone = true
            searchErrorContainer.isGone = true

            when (state) {
                State.DATA -> {
                    searchRecyclerView.isVisible = true
                }

                State.NO_DATA -> {
                    searchErrorContainer.isVisible = true
                }

                State.ERROR -> {
                    noInternetErrorContainer.isVisible = true
                }
            }
        }
    }

    enum class State {
        DATA,
        NO_DATA,
        ERROR;

        companion object {
            fun fromIndex(index: Int): State {
                return when (index) {
                    0 -> DATA
                    1 -> NO_DATA
                    else -> ERROR
                }
            }
        }
    }

    companion object {
        private const val EMPTY_STRING = ""
        private const val REQUEST = "search"
        private const val STATE = "state"
    }
}
