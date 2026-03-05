package com.arun.newsapp.ui.sources

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.arun.newsapp.NewsApp
import com.arun.newsapp.R
import com.arun.newsapp.data.model.SourceData
import com.arun.newsapp.databinding.ActivityNewsSourcesBinding
import com.arun.newsapp.di.component.DaggerActivityComponent
import com.arun.newsapp.di.module.ActivityModule
import com.arun.newsapp.ui.base.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsSourcesActivity : AppCompatActivity() {
    @Inject
    lateinit var newsSourcesViewModel: NewsSourcesViewModel

    @Inject
    lateinit var newsSourcesAdapter: NewsSourcesAdapter

    private lateinit var binding: ActivityNewsSourcesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        binding = ActivityNewsSourcesBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setUpUI()
        setupObserver()
    }

    private fun setUpUI() {
        val recyclerView = binding.recyclerViewSources
        recyclerView.layoutManager = LinearLayoutManager(this@NewsSourcesActivity)
        recyclerView.adapter = newsSourcesAdapter
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                newsSourcesViewModel.uiState.collect {
                    when (it) {
                        UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.recyclerViewSources.visibility = View.GONE
                        }

                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.recyclerViewSources.visibility = View.VISIBLE
                            renderList(it.data)
                        }

                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@NewsSourcesActivity, it.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun renderList(data: List<SourceData>) {
        newsSourcesAdapter.addData(data)
        newsSourcesAdapter.notifyDataSetChanged()
    }

    private fun injectDependencies() {
        DaggerActivityComponent.builder()
            .applicationComponent((application as NewsApp).applicationComponent)
            .activityModule(ActivityModule(this))
            .build()
            .inject(this)
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, NewsSourcesActivity::class.java))
        }
    }
}