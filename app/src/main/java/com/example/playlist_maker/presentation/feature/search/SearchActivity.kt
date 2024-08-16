package com.example.playlist_maker.presentation.feature.search

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
import com.example.playlist_maker.utils.Debouncer
import com.example.playlist_maker.utils.ClickThrottler
import com.example.playlist_maker.databinding.ActivitySearchBinding
import com.example.playlist_maker.di.Injector
import com.example.playlist_maker.domain.model.request.SearchRequest
import com.example.playlist_maker.presentation.feature.player.PlayerActivity
import com.example.playlist_maker.presentation.feature.search.adapter.SearchAdapter
import com.example.playlist_maker.presentation.models.TrackItem
import com.example.playlist_maker.domain.model.Track
import com.example.playlist_maker.presentation.mapper.toUI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivitySearchBinding
    private var searchRequest = EMPTY_STRING
    private var state = State.HISTORY
    private var loadTracksJob: Job? = null
    private val searchAdapter: SearchAdapter
    private val historyAdapter: SearchAdapter
    private val textWatcher: TextWatcher
    private val searchInteractor = Injector.getSearchUseCase()
    private val historyInteractor = Injector.getHistoryInteractor()
    private val itemClickThrottler = ClickThrottler(CLICK_DELAY)
    private var historyDomainList: List<Track> = listOf()
    private var searchResultDomainList: List<Track> = listOf()
    private val searchDebouncer: Debouncer by lazy {
        Debouncer(SEARCH_DELAY) {
            with(viewBinding) {
                val imm = getSystemService(InputMethodManager::class.java)

                imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
                loadTracks(searchEditText.text.toString())

                trySetHistoryState()
            }
        }
    }

    init {
        searchAdapter = SearchAdapter(onItemClickListener = {
            if (itemClickThrottler.clickThrottle()) {
                updateHistory(it, searchResultDomainList)
                moveToPlayerActivity(it)
            }
        })
        historyAdapter = SearchAdapter(onItemClickListener = {
            if (itemClickThrottler.clickThrottle()) {
                updateHistory(it, historyDomainList)
                moveToPlayerActivity(it)
            }
        })

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewBinding.searchFieldClearButton.isInvisible = s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    searchRequest = s.toString()
                    searchDebouncer.updateValue()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        historyDomainList = historyInteractor.getHistory()
        historyAdapter.setItems(historyDomainList.map { it.toUI() })

        initUi()
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
                searchEditText.removeTextChangedListener(textWatcher)
                searchEditText.setText(EMPTY_STRING)
                searchEditText.clearFocus()
                searchEditText.addTextChangedListener(textWatcher)

                searchRequest = EMPTY_STRING
                imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
                searchAdapter.setItems(listOf())
                trySetHistoryState()
            }

            searchRecyclerView.adapter = searchAdapter
            searchRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)

            historyRecyclerView.adapter = historyAdapter
            historyRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)

            clearHistoryButton.setOnClickListener {
                historyInteractor.clearHistory()
                updateHistoryAdapterList(listOf())
                trySetHistoryState()
            }

            trySetHistoryState()
        }
    }

    private fun loadTracks(text: String) {
        loadTracksJob?.cancel()

        loadTracksJob = CoroutineScope(Dispatchers.IO).launch {
            val result = searchInteractor.getTracks(
                SearchRequest(
                    text = text
                )
            )

            if (result.isSuccess) {
                val response = result.getOrNull()
                if (response?.resultCount != 0) {
                    searchResultDomainList = response?.tracks ?: listOf()
                    val tracks = searchResultDomainList.map { it.toUI() }

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
            && !historyInteractor.isHistoryEmpty()
        ) {
            State.HISTORY
        } else if (searchRequest.isNotEmpty()) {
            State.LOADING
        } else {
            State.DATA
        }

        applyState(state)
    }

    private fun applyState(state: State) {
        with(viewBinding) {
            searchRecyclerView.isGone = true
            noInternetErrorContainer.isGone = true
            searchErrorContainer.isGone = true
            historyContainer.isGone = true
            searchLoaderProgressBar.isGone = true

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

                State.LOADING -> {
                    searchLoaderProgressBar.isVisible = true
                }
            }
        }
    }

    private fun updateHistory(item: TrackItem, tracksList: List<Track>) {
        val track = tracksList.firstOrNull { it.trackId == item.trackId }
        if (track != null) {
            historyDomainList = historyInteractor.updateHistory(track)
            this@SearchActivity.updateHistoryAdapterList(historyDomainList.map { it.toUI() })
        }
    }

    private fun moveToPlayerActivity(item: TrackItem) {
        val intent = Intent(this@SearchActivity, PlayerActivity::class.java)
        intent.putExtra(TRACK, item)
        startActivity(intent)
    }

    companion object {
        private const val SEARCH_DELAY = 2000L
        private const val CLICK_DELAY = 500L
        private const val REQUEST = "search"
        private const val STATE = "state"
        private const val EMPTY_STRING = ""
        private const val TRACK = "track"
    }

    enum class State {
        DATA,
        NO_DATA,
        ERROR,
        HISTORY,
        LOADING;

        companion object {
            fun fromIndex(index: Int): State {
                return when (index) {
                    0 -> DATA
                    1 -> NO_DATA
                    2 -> ERROR
                    3 -> HISTORY
                    else -> LOADING
                }
            }
        }
    }
}