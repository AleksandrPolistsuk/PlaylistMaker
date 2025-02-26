package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var placeholderNoResults: LinearLayout
    private lateinit var placeholderError: LinearLayout
    private lateinit var btnRetry: Button
    private lateinit var clearButton: ImageView
    private val trackList = ArrayList<Track>()
    private var lastQuery: String = "" // Для повтора запроса

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.hide()

        searchEditText = findViewById(R.id.editTextField)
        clearButton = findViewById(R.id.clearButton)
        recyclerView = findViewById(R.id.recyclerView)
        placeholderNoResults = findViewById(R.id.placeholder_no_results)
        placeholderError = findViewById(R.id.placeholder_error)
        btnRetry = findViewById(R.id.btn_retry)

        trackAdapter = TrackAdapter(trackList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = trackAdapter

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                clearButton.visibility = if (query.isEmpty()) View.GONE else View.VISIBLE
                if (query.isNotEmpty()) {
                    lastQuery = query
                    searchTracks(query)
                } else {
                    clearResults()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        clearButton.setOnClickListener {
            searchEditText.text.clear()
            hideKeyboard()
            clearResults()
        }

        btnRetry.setOnClickListener {
            searchTracks(lastQuery)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainSearch)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun searchTracks(query: String) {
        showLoading()
        RetrofitClient.apiService.searchTracks(query).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                if (response.isSuccessful) {
                    val tracks = response.body()?.results ?: emptyList()
                    updateResults(tracks)
                } else {
                    showErrorPlaceholder()
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                showErrorPlaceholder()
            }
        })
    }

    private fun updateResults(tracks: List<Track>) {
        trackList.clear()
        trackList.addAll(tracks)
        trackAdapter.notifyDataSetChanged()

        recyclerView.visibility = if (tracks.isNotEmpty()) View.VISIBLE else View.GONE
        placeholderNoResults.visibility = if (tracks.isEmpty()) View.VISIBLE else View.GONE
        placeholderError.visibility = View.GONE
    }

    private fun showErrorPlaceholder() {
        recyclerView.visibility = View.GONE
        placeholderNoResults.visibility = View.GONE
        placeholderError.visibility = View.VISIBLE
    }

    private fun clearResults() {
        trackList.clear()
        trackAdapter.notifyDataSetChanged()
        recyclerView.visibility = View.GONE
        placeholderNoResults.visibility = View.GONE
        placeholderError.visibility = View.GONE
    }

    private fun showLoading() {
        recyclerView.visibility = View.GONE
        placeholderNoResults.visibility = View.GONE
        placeholderError.visibility = View.GONE
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(searchEditText.windowToken, 0)
    }
}