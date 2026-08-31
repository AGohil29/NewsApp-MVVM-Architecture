package com.arun.newsapp.data.repository

import app.cash.turbine.test
import com.arun.newsapp.data.api.NetworkService
import com.arun.newsapp.data.model.SourceData
import com.arun.newsapp.data.model.SourcesResponse
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NewsSourcesRepositoryTest {
    @Mock
    private lateinit var networkService: NetworkService

    private lateinit var newsSourcesRepository: NewsSourcesRepository

    @Before
    fun setUp() {
        newsSourcesRepository = NewsSourcesRepository(networkService)
    }

    @Test
    fun getNewsSources_whenNetworkServiceResponseSuccess_shouldReturnSuccess() {
        runTest {
            val sourceData = SourceData(
                category = "Category",
                country = "Country",
                description = "Description",
                id = "id",
                language = "Language",
                name = "Name",
                url = "url"
            )

            val sources = mutableListOf<SourceData>()
            sources.add(sourceData)

            val sourceResponse = SourcesResponse(
                status = "ok",
                sources = sources
            )

            doReturn(sourceResponse)
                .`when`(networkService)
                .getTopHeadlinesSources()

            newsSourcesRepository.getNewsSources().test {
                assertEquals(sourceResponse.sources, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            verify(networkService, times(1))
                .getTopHeadlinesSources()
        }
    }

    @Test
    fun getNewsSources_whenNetworkServiceResponseError_shouldReturnError() {
        runTest {
            val errorMessage = "Error Message For You"

            doThrow(RuntimeException(errorMessage))
                .`when`(networkService)
                .getTopHeadlinesSources()

            newsSourcesRepository.getNewsSources().test {
                assertEquals(errorMessage, awaitError().message)
                cancelAndIgnoreRemainingEvents()
            }

            verify(networkService, times(1))
                .getTopHeadlinesSources()
        }
    }
}