package com.arun.newsapp.ui.topheadline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arun.newsapp.data.model.Article
import com.arun.newsapp.data.repository.TopHeadlineRepository
import com.arun.newsapp.ui.base.UiState
import com.arun.newsapp.utils.AppConstant.COUNTRY
import com.arun.newsapp.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopHeadlineViewModel @Inject constructor(
    private val topHeadlineRepository: TopHeadlineRepository,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Article>>>(UiState.Loading)

    val uiState: StateFlow<UiState<List<Article>>> = _uiState

    init {
        fetchNews()
    }

    fun fetchNews() {
        _uiState.value = UiState.Loading
        viewModelScope.launch(dispatcherProvider.main) {
            topHeadlineRepository.getTopHeadlines(COUNTRY)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiState.value = UiState.Error(e.toString())
                }.collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }
}