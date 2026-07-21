package com.arun.newsapp.ui.countries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arun.newsapp.data.model.Country
import com.arun.newsapp.data.repository.CountriesRepository
import com.arun.newsapp.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val countriesRepository: CountriesRepository,
) : ViewModel() {

    private val _countries = MutableStateFlow<UiState<List<Country>>>(UiState.Loading)
    private var fetchCountriesJob: Job? = null

    val countries: StateFlow<UiState<List<Country>>> = _countries

    init {
        fetchCountries()
    }

    fun retryFetchCountries() {
        _countries.value = UiState.Loading
        fetchCountries()
    }

    private fun fetchCountries() {
        fetchCountriesJob?.cancel()
        fetchCountriesJob = viewModelScope.launch {
            countriesRepository.getCountries()
                .catch { e ->
                    _countries.value = UiState.Error(e.message ?: e.toString())
                }
                .collect {
                    _countries.value = UiState.Success(it)
                }
        }
    }
}