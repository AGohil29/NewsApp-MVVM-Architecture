package com.arun.newsapp.ui.sources

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arun.newsapp.data.model.SourceData
import com.arun.newsapp.data.repository.NewsSourcesRepository
import com.arun.newsapp.ui.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class NewsSourcesViewModel(private val newsSourcesRepository: NewsSourcesRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<SourceData>>>(UiState.Loading)

    val uiState: StateFlow<UiState<List<SourceData>>> = _uiState

    init {
        fetchNewsSources()
    }

    private fun fetchNewsSources() {
        viewModelScope.launch {
            newsSourcesRepository.getNewsSources()
                .catch { e ->
                    _uiState.value = UiState.Error(e.toString())
                }.collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }
}