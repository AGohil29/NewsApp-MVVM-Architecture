package com.arun.newsapp.ui.countries

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.arun.newsapp.R
import com.arun.newsapp.data.model.Country
import com.arun.newsapp.databinding.ActivityCountriesBinding
import com.arun.newsapp.ui.base.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CountriesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCountriesBinding

    private lateinit var viewModel: CountriesViewModel

    @Inject
    lateinit var adapter: CountriesAdapter

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, CountriesActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCountriesBinding.inflate(layoutInflater)
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
        viewModel = ViewModelProvider(this)[CountriesViewModel::class.java]
    }

    private fun setUpUI() {
        val recyclerView = binding.recyclerViewCountries
        recyclerView.layoutManager = LinearLayoutManager(this@CountriesActivity)
        recyclerView.adapter = adapter
        binding.noDataView.buttonTryAgain.setOnClickListener {
            viewModel.retryFetchCountries()
        }
    }

    private fun setUpObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.countries.collect {
                    when (it) {
                        UiState.Loading -> {
                            binding.recyclerViewCountries.visibility = View.GONE
                            binding.noDataView.root.visibility = View.GONE
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.noDataView.root.visibility = View.GONE
                            binding.recyclerViewCountries.visibility = View.VISIBLE
                            renderList(it.data)
                        }

                        is UiState.Error -> {
                            binding.recyclerViewCountries.visibility = View.GONE
                            binding.progressBar.visibility = View.GONE
                            binding.noDataView.textViewNoDataMessage.text =
                                it.message.ifBlank { getString(R.string.no_data_error_message) }
                            binding.noDataView.root.visibility = View.VISIBLE
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