package com.arun.newsapp.data.repository

import app.cash.turbine.test
import com.arun.newsapp.data.model.Country
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CountriesRepositoryTest {

    private lateinit var countriesRepository: CountriesRepository

    @Before
    fun setUp() {
        countriesRepository = CountriesRepository()
    }

    @Test
    fun getNewsSources_whenConstantResponseSuccess_shouldReturnSuccess() {
        runTest {
            val countries = mutableListOf<Country>()
            countries.add(Country("in", "India"))

            countriesRepository.getCountries().test {
                assertEquals(countries, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }
}