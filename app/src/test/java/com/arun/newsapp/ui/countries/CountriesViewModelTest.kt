package com.arun.newsapp.ui.countries

import app.cash.turbine.test
import com.arun.newsapp.data.model.Country
import com.arun.newsapp.data.repository.CountriesRepository
import com.arun.newsapp.ui.base.UiState
import com.arun.newsapp.utils.DispatcherProvider
import com.arun.newsapp.utils.TestDispatcherProvider
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CountriesViewModelTest {

    @Mock
    private lateinit var countriesRepository: CountriesRepository

    private lateinit var dispatcherProvider: DispatcherProvider

    @Before
    fun setUp() {
        dispatcherProvider = TestDispatcherProvider()
    }

    @Test
    fun fetchCountries_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
        runTest {
            doReturn(flowOf(emptyList<Country>()))
                .`when`(countriesRepository)
                .getCountries()
            val viewModel = CountriesViewModel(countriesRepository, dispatcherProvider)
            viewModel.uiState.test {
                assertEquals(UiState.Success(emptyList<Country>()), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            verify(countriesRepository, times(1))
                .getCountries()
        }
    }

    @Test
    fun fetchCountries_whenRepositoryResponseError_shouldSetErrorUiState() {
        runTest {
            val errorMessage = "Error Message For You"
            doReturn(flow<List<Country>> {
                throw IllegalStateException(errorMessage)
            }).`when`(countriesRepository)
                .getCountries()
            val viewModel = CountriesViewModel(countriesRepository, dispatcherProvider)

            viewModel.uiState.test {
                assertEquals(
                    UiState.Error(IllegalStateException(errorMessage).toString()),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }

            verify(countriesRepository, times(1))
                .getCountries()
        }
    }
}