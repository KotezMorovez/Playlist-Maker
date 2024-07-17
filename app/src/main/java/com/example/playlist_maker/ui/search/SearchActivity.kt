package com.example.playlist_maker.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
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
import com.example.playlist_maker.ui.PlayerActivity
import com.example.playlist_maker.ui.search.adapter.SearchAdapter
import com.example.playlist_maker.ui.search.adapter.TrackItem
import com.example.playlist_maker.ui.search.common.HistoryManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale


class SearchActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivitySearchBinding
    private var searchRequest = EMPTY_STRING
    private var state = State.HISTORY
    private val searchRepository = SearchRepositoryImpl.getInstance()
    private var loadTracksJob: Job? = null
    private val searchAdapter: SearchAdapter
    private val historyAdapter : SearchAdapter
    private val textWatcher: TextWatcher

    init {
        searchAdapter = SearchAdapter(onItemClickListener = { item ->
            val list = HistoryManager.updateList(item)
            this@SearchActivity.updateHistoryAdapterList(list)

            val intent = Intent(this@SearchActivity, PlayerActivity::class.java)
            intent.putExtra(TRACK, item)
            startActivity(intent)
        })

        historyAdapter = SearchAdapter(onItemClickListener = { item ->
            val list = HistoryManager.updateList(item)
            this@SearchActivity.updateHistoryAdapterList(list)

            val intent = Intent(this@SearchActivity, PlayerActivity::class.java)
            intent.putExtra(TRACK, item)
            startActivity(intent)
        })

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewBinding.searchFieldClearButton.isInvisible = s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                searchRequest = s.toString()
                trySetHistoryState()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        HistoryManager.initHistoryStorage(this)
        historyAdapter.setItems(HistoryManager.getHistory())

        initUi()
    }

    private fun initUi() {
        with(viewBinding) {
            this@SearchActivity.setSupportActionBar(searchToolbar)
            this@SearchActivity.supportActionBar?.title = resources.getText(R.string.search_title)
            searchToolbar.setNavigationOnClickListener {
                this@SearchActivity.onBackPressedDispatcher.onBackPressed()
            }

            val imm = getSystemService(InputMethodManager::class.java)

            searchEditText.addTextChangedListener(textWatcher)
            searchEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
                    loadTracks(searchEditText.text.toString())
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            searchEditText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    trySetHistoryState()
                }
            }

            searchEditText.requestFocus()
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

            searchFieldClearButton.setOnClickListener {
                searchEditText.setText(EMPTY_STRING)
                searchRequest = EMPTY_STRING
                searchEditText.clearFocus()
                imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
                searchAdapter.setItems(listOf())
                trySetHistoryState()
            }

            searchRecyclerView.adapter = searchAdapter
            searchRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)

            historyRecyclerView.adapter = historyAdapter
            historyRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)

            clearHistoryButton.setOnClickListener {
                HistoryManager.clearHistory()
                updateHistoryAdapterList(listOf())
                trySetHistoryState()
            }

            trySetHistoryState()
        }
    }

    private fun loadTracks(text: String) {
        loadTracksJob?.cancel()

        loadTracksJob = CoroutineScope(Dispatchers.IO).launch {
            val result = searchRepository.getTracks(
                SearchRequest(
                    text = text
                )
            )

            if (result.isSuccess) {
                val response = result.getOrNull()
                if (response?.resultCount != 0) {
                    val tracks = response?.tracks?.map {
                        val date = SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss'Z'",
                            Locale.getDefault()
                        ).parse(it.releaseDate) ?: LocalDate.now()

                        TrackItem(
                            trackId = it.trackId,
                            trackName = it.trackName,
                            artistName = it.artistName,
                            trackTime = SimpleDateFormat(
                                "mm:ss",
                                Locale.getDefault()
                            ).format(it.trackTimeMillis),
                            collectionName = it.collectionName,
                            releaseDate = SimpleDateFormat(
                                "yyyy",
                                Locale.getDefault()
                            ).format(date),
                            primaryGenreName = it.primaryGenreName,
                            country = it.country,
                            artworkUrl100 = it.artworkUrl100
                        )
                    } ?: listOf()

                    withContext(Dispatchers.Main) {
                        state = State.DATA
                        applyState(state)
                        searchAdapter.setItems(tracks)
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

    private fun updateHistoryAdapterList(list: List<TrackItem>) {
        historyAdapter.setItems(list)
    }

    private fun trySetHistoryState() {
        state = if (
            searchRequest.isEmpty()
            && viewBinding.searchEditText.hasFocus()
            && !HistoryManager.isHistoryEmpty()
        ) {
            State.HISTORY
        } else {
            State.DATA
        }

        applyState(state)
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
            historyContainer.isGone = true

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

                State.HISTORY -> {
                    historyContainer.isVisible = true
                }
            }
        }
    }

    companion object {
        private const val REQUEST = "search"
        private const val STATE = "state"
        private const val EMPTY_STRING = ""
        private const val TRACK = "track"
    }

    enum class State {
        DATA,
        NO_DATA,
        ERROR,
        HISTORY;

        companion object {
            fun fromIndex(index: Int): State {
                return when (index) {
                    0 -> DATA
                    1 -> NO_DATA
                    2 -> ERROR
                    else -> HISTORY
                }
            }
        }
    }
}