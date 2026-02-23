package com.arun.newsapp.ui.countries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arun.newsapp.data.model.Country
import com.arun.newsapp.data.repository.CountriesRepository
import com.arun.newsapp.ui.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CountriesViewModel(private val countriesRepository: CountriesRepository): ViewModel() {

    private val _countries = MutableStateFlow<UiState<List<Country>>>(UiState.Loading)

    val countries: StateFlow<UiState<List<Country>>> = _countries

    init {
        fetchCountries()
    }

    private fun fetchCountries() {
        viewModelScope.launch {
            countriesRepository.getCountries()
                .catch { e ->
                    _countries.value = UiState.Error(e.toString())
                }
                .collect {
                    _countries.value = UiState.Success(it)
                }
        }
    }
}