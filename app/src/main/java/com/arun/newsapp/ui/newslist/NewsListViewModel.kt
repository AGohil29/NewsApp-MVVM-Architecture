package com.arun.newsapp.ui.newslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arun.newsapp.data.model.Article
import com.arun.newsapp.data.repository.NewsListRepository
import com.arun.newsapp.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val newsListRepository: NewsListRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<Article>>>(UiState.Loading)

    val uiState: StateFlow<UiState<List<Article>>> = _uiState

    private var fetchNewsBySourcesJob: Job? = null
    private var fetchNewsByCountryJob: Job? = null

    fun fetchNewsBySources(source: String) {
        fetchNewsBySourcesJob?.cancel()
        _uiState.value = UiState.Loading
        fetchNewsBySourcesJob = viewModelScope.launch {
            newsListRepository.getNewsBySources(source)
                .catch { e ->
                    _uiState.value = UiState.Error(e.toString())
                }.collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }

    fun fetchNewsByCountry(country: String) {
        fetchNewsByCountryJob?.cancel()
        _uiState.value = UiState.Loading
        fetchNewsByCountryJob = viewModelScope.launch {
            newsListRepository.getNewsByCountry(country)
                .catch { e ->
                    _uiState.value = UiState.Error(e.toString())
                }.collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }
}