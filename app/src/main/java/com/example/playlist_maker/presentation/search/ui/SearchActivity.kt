package com.example.playlist_maker.presentation.search.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
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
import com.example.playlist_maker.presentation.player.ui.PlayerActivity
import com.example.playlist_maker.presentation.search.view_model.SearchViewModel
import com.example.playlist_maker.presentation.search.dto.TrackItem
import com.example.playlist_maker.presentation.search.ui.adapter.SearchAdapter

class SearchActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels { Injector.getViewModelFactory() }
    private val searchAdapter: SearchAdapter
    private val historyAdapter: SearchAdapter
    private val textWatcher: TextWatcher
    private val itemClickThrottler = ClickThrottler(CLICK_DELAY)
    private val searchDebouncer: Debouncer by lazy {
        Debouncer(SEARCH_DELAY) {
            with(viewBinding) {
                val imm = getSystemService(InputMethodManager::class.java)

                imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
                viewModel.loadTracks(searchEditText.text.toString())

                viewModel.actualizeState(viewBinding.searchEditText.hasFocus())
            }
        }
    }

    init {
        searchAdapter = SearchAdapter(onItemClickListener = {
            if (itemClickThrottler.clickThrottle()) {
                viewModel.handleItemClick(it, false)
            }
        })
        historyAdapter = SearchAdapter(onItemClickListener = {
            if (itemClickThrottler.clickThrottle()) {
                viewModel.handleItemClick(it, true)
            }
        })

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewBinding.searchFieldClearButton.isInvisible = s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    viewModel.setSearchRequest(s.toString())
                    searchDebouncer.updateValue()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewModel.init()

        initUi()
        observeData()
    }

    private fun initUi() {
        with(viewBinding) {
            this@SearchActivity.setSupportActionBar(searchToolbar)
            this@SearchActivity.supportActionBar?.title = resources.getText(R.string.search_title)
            searchToolbar.setNavigationOnClickListener {
                this@SearchActivity.onBackPressedDispatcher.onBackPressed()
            }

            val imm = getSystemService(InputMethodManager::class.java)

            searchEditText.setText(viewModel.lastSearchRequest)
            searchEditText.addTextChangedListener(textWatcher)
            searchEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
                    viewModel.loadTracks(searchEditText.text.toString())
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            searchEditText.setOnFocusChangeListener { _, hasFocus ->
                viewModel.actualizeState(hasFocus)
            }

            searchEditText.requestFocus()
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

            refreshButton.setOnClickListener {
                viewModel.loadTracks(viewBinding.searchEditText.text.toString())
            }

            searchFieldClearButton.setOnClickListener {
                searchEditText.removeTextChangedListener(textWatcher)
                searchEditText.setText(EMPTY_STRING)
                searchEditText.clearFocus()
                searchEditText.addTextChangedListener(textWatcher)

                viewModel.setSearchRequest(EMPTY_STRING)
                imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
                searchAdapter.setItems(listOf())
                viewModel.actualizeState(viewBinding.searchEditText.hasFocus())
            }

            searchRecyclerView.adapter = searchAdapter
            searchRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)

            historyRecyclerView.adapter = historyAdapter
            historyRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)

            clearHistoryButton.setOnClickListener {
                viewModel.clearHistory()
                viewModel.actualizeState(viewBinding.searchEditText.hasFocus())
            }
            viewModel.actualizeState(viewBinding.searchEditText.hasFocus())
        }
    }

    private fun observeData() {
        viewModel.currentState.observe(this) {
            applyState(it)
        }

        viewModel.navigationEvent.observe(this) {
            if (it != null) {
                val intent = Intent(this@SearchActivity, PlayerActivity::class.java)
                intent.putExtra(TRACK, it)
                startActivity(intent)
            }
        }
    }

    private fun applyState(state: SearchViewModel.State) {
        with(viewBinding) {
            searchRecyclerView.isGone = true
            noInternetErrorContainer.isGone = true
            searchErrorContainer.isGone = true
            historyContainer.isGone = true
            searchLoaderProgressBar.isGone = true

            when (state) {
                is SearchViewModel.State.Data -> {
                    searchRecyclerView.isVisible = true

                    searchAdapter.setItems(state.list)
                }

                is SearchViewModel.State.NoData -> {
                    searchErrorContainer.isVisible = true
                }

                is SearchViewModel.State.Error -> {
                    noInternetErrorContainer.isVisible = true
                }

                is SearchViewModel.State.History -> {
                    historyContainer.isVisible = true

                    historyAdapter.setItems(state.list)
                }

                is SearchViewModel.State.Loading -> {
                    searchLoaderProgressBar.isVisible = true
                }
            }
        }
    }

    companion object {
        private const val SEARCH_DELAY = 2000L
        private const val CLICK_DELAY = 500L
        private const val EMPTY_STRING = ""
        private const val TRACK = "track"
    }
}