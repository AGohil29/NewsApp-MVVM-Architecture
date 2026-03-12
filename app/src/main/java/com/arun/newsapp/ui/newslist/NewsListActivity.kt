package com.arun.newsapp.ui.newslist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.arun.newsapp.NewsApp
import com.arun.newsapp.data.model.Article
import com.arun.newsapp.databinding.ActivityNewsListBinding
import com.arun.newsapp.di.component.DaggerActivityComponent
import com.arun.newsapp.di.module.ActivityModule
import com.arun.newsapp.ui.base.UiState
import com.arun.newsapp.ui.topheadline.TopHeadlineAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsListBinding

    @Inject
    lateinit var viewModel: NewsListViewModel

    @Inject
    lateinit var adapter: TopHeadlineAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        binding = ActivityNewsListBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        setupUI()
        setupObserver()
        fetchNews()
    }

    private fun setupUI() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@NewsListActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this.context,
                    (layoutManager as LinearLayoutManager).orientation
                )
            )
            adapter = this@NewsListActivity.adapter
        }
    }

    private fun fetchNews() {
        val newsBy = intent.getStringExtra(NEWS_BY_KEY)
        val newsByValue = intent.getStringExtra(NEWS_BY_VALUE_KEY) ?: ""

        when (newsBy) {
            NEWS_BY_COUNTRY -> viewModel.fetchNewsByCountry(newsByValue)
            NEWS_BY_SOURCE -> viewModel.fetchNewsBySources(newsByValue)
            // Language support can be added here if implemented in the repository
        }
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.recyclerView.visibility = View.GONE
                        }

                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            renderList(state.data)
                            binding.recyclerView.visibility = View.VISIBLE
                        }

                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@NewsListActivity, state.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun renderList(articleList: List<Article>) {
        adapter.updateData(articleList)
        adapter.notifyDataSetChanged()
    }

    private fun injectDependencies() {
        DaggerActivityComponent.builder()
            .applicationComponent((application as NewsApp).applicationComponent)
            .activityModule(ActivityModule(this))
            .build()
            .inject(this)
    }

    companion object {
        const val NEWS_BY_KEY = "news_by"
        const val NEWS_BY_VALUE_KEY = "news_by_value"
        const val NEWS_BY_SOURCE = "sources"
        const val NEWS_BY_COUNTRY = "country"
        const val NEWS_BY_LANGUAGE = "language"

        fun start(context: Context, newsBy: String, newsByValue: String) {
            val intent = Intent(context, NewsListActivity::class.java).apply {
                putExtra(NEWS_BY_KEY, newsBy)
                putExtra(NEWS_BY_VALUE_KEY, newsByValue)
            }
            context.startActivity(intent)
        }
    }
}