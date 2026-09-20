package com.arun.newsapp.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.arun.newsapp.R
import com.arun.newsapp.data.model.Article
import com.arun.newsapp.databinding.ActivitySearchBinding
import com.arun.newsapp.ui.base.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel

    @Inject
    lateinit var adapter: SearchAdapter

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, SearchActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpViewModel()
        setUpUI()
        setUpObserver()
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
    }

    private fun setUpUI() {
        val recyclerView = binding.recyclerViewSearch
        recyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.searchNews(newText)
                return true
            }
        })
    }

    private fun setUpObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    Log.d("SearchActivity", "UI State: $it, Thread: ${Thread.currentThread().name}")
                    when (it) {
                        UiState.Loading -> {
                            Log.d("SearchActivity", "Loading state")
                            binding.progressBar.visibility = View.VISIBLE
                            binding.recyclerViewSearch.visibility = View.GONE
                        }

                        is UiState.Success -> {
                            Log.d("SearchActivity", "Success state with ${it.data.size} items")
                            binding.recyclerViewSearch.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                            renderList(it.data)
                        }

                        is UiState.Error -> {
                            Log.d("SearchActivity", "Error state: ${it.message}")
                            binding.progressBar.visibility = View.GONE
                            binding.recyclerViewSearch.visibility = View.GONE
                            Toast.makeText(this@SearchActivity, it.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun renderList(articleList: List<Article>) {
        Log.d(
            "SearchActivity",
            "renderList called with ${articleList.size} items, adapter count: ${adapter.itemCount}"
        )
        adapter.updateData(articleList)
        Log.d("SearchActivity", "After updateData, adapter count: ${adapter.itemCount}")
    }
}
