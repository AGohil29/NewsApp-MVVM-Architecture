package com.arun.newsapp.ui.languages

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
import com.arun.newsapp.data.model.Language
import com.arun.newsapp.databinding.ActivityLanguagesBinding
import com.arun.newsapp.ui.base.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LanguagesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLanguagesBinding

    private lateinit var viewModel: LanguagesViewModel

    @Inject
    lateinit var adapter: LanguagesAdapter

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, LanguagesActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLanguagesBinding.inflate(layoutInflater)
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
        viewModel = ViewModelProvider(this)[LanguagesViewModel::class.java]
    }

    private fun setUpUI() {
        val recyclerView = binding.recyclerViewLanguages
        recyclerView.layoutManager = LinearLayoutManager(this@LanguagesActivity)
        recyclerView.adapter = adapter
        binding.noDataView.buttonTryAgain.setOnClickListener {
            viewModel.fetchLanguages()
        }
    }

    private fun setUpObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        UiState.Loading -> {
                            binding.recyclerViewLanguages.visibility = View.GONE
                            binding.noDataView.root.visibility = View.GONE
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.noDataView.root.visibility = View.GONE
                            binding.recyclerViewLanguages.visibility = View.VISIBLE
                            renderList(it.data)
                        }

                        is UiState.Error -> {
                            binding.recyclerViewLanguages.visibility = View.GONE
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

    private fun renderList(languages: List<Language>) {
        adapter.addData(languages)
        adapter.notifyDataSetChanged()
    }
}
