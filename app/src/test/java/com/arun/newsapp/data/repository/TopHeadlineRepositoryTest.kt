package com.arun.newsapp.data.repository

import app.cash.turbine.test
import com.arun.newsapp.data.api.NetworkService
import com.arun.newsapp.data.model.Article
import com.arun.newsapp.data.model.Source
import com.arun.newsapp.data.model.TopHeadlinesResponse
import com.arun.newsapp.utils.AppConstant
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
class TopHeadlineRepositoryTest {
    @Mock
    private lateinit var networkService: NetworkService

    private lateinit var topHeadlineRepository: TopHeadlineRepository

    @Before
    fun setUp() {
        topHeadlineRepository = TopHeadlineRepository(networkService)
    }

    @Test
    fun getTopHeadlines_whenNetworkServiceResponseSuccess_shouldReturnSuccess() {
        runTest {
            val source = Source(id = "sourceId", name = "sourceName")
            val article = Article(
                title = "title",
                description = "description",
                url = "url",
                imageUrl = "urlToImage",
                source = source
            )

            val articles = mutableListOf<Article>()
            articles.add(article)

            val topHeadlinesResponse = TopHeadlinesResponse(
                status = "ok", totalResults = 1, articles = articles, "200", message = "success"
            )

            doReturn(topHeadlinesResponse)
                .`when`(networkService)
                .getTopHeadlines(AppConstant.COUNTRY)

            topHeadlineRepository.getTopHeadlines(AppConstant.COUNTRY).test {
                assertEquals(topHeadlinesResponse.articles, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            verify(networkService, times(1))
                .getTopHeadlines(AppConstant.COUNTRY)
        }
    }

    @Test
    fun getTopHeadlines_whenNetworkServiceResponseError_shouldReturnError() {
        runTest {
            val errorMessage = "Error Message For You"

            doThrow(RuntimeException(errorMessage))
                .`when`(networkService)
                .getTopHeadlines(AppConstant.COUNTRY)

            topHeadlineRepository.getTopHeadlines(AppConstant.COUNTRY).test {
                assertEquals(errorMessage, awaitError().message)
                cancelAndIgnoreRemainingEvents()
            }

            verify(networkService, times(1))
                .getTopHeadlines(AppConstant.COUNTRY)
        }
    }
}