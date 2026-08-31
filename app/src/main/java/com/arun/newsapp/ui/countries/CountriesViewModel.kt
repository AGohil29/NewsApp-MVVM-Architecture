package com.arun.newsapp.ui.countries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arun.newsapp.data.model.Country
import com.arun.newsapp.data.repository.CountriesRepository
import com.arun.newsapp.ui.base.UiState
import com.arun.newsapp.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val countriesRepository: CountriesRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Country>>>(UiState.Loading)
    private var fetchCountriesJob: Job? = null

    val uiState: StateFlow<UiState<List<Country>>> = _uiState

    init {
        fetchCountries()
    }

    fun fetchCountries() {
        fetchCountriesJob?.cancel()
        fetchCountriesJob = viewModelScope.launch(dispatcherProvider.main) {
            countriesRepository.getCountries()
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiState.value = UiState.Error(e.toString())
                }
                .collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }
}