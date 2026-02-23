package com.arun.newsapp.ui.countries

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
import com.arun.newsapp.data.model.Country
import com.arun.newsapp.databinding.ActivityCountriesBinding
import com.arun.newsapp.di.component.DaggerActivityComponent
import com.arun.newsapp.di.module.ActivityModule
import com.arun.newsapp.ui.base.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

class CountriesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCountriesBinding

    @Inject
    lateinit var viewModel: CountriesViewModel

    @Inject
    lateinit var adapter: CountriesAdapter

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, CountriesActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCountriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpUI()
        setUpObserver()
    }

    private fun injectDependencies() {
        DaggerActivityComponent.builder()
            .applicationComponent((application as NewsApp).applicationComponent)
            .activityModule(ActivityModule(this))
            .build()
            .inject(this)
    }

    private fun setUpUI() {
        val recyclerView = binding.recyclerViewCountries
        recyclerView.layoutManager = LinearLayoutManager(this@CountriesActivity)
        recyclerView.adapter = adapter
    }

    private fun setUpObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.countries.collect {
                    when (it) {
                        UiState.Loading -> {
                            binding.recyclerViewCountries.visibility = View.GONE
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.recyclerViewCountries.visibility = View.VISIBLE
                            renderList(it.data)
                        }
                        is UiState.Error -> {
                            binding.recyclerViewCountries.visibility = View.GONE
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@CountriesActivity, it.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun renderList(countries: List<Country>) {
        adapter.addData(countries)
        adapter.notifyDataSetChanged()
    }
}