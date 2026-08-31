package com.arun.newsapp.ui.sources

import app.cash.turbine.test
import com.arun.newsapp.data.model.SourceData
import com.arun.newsapp.data.repository.NewsSourcesRepository
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
class NewsSourcesViewModelTest {

    @Mock
    private lateinit var newsSourcesRepository: NewsSourcesRepository

    private lateinit var dispatcherProvider: DispatcherProvider

    @Before
    fun setUp() {
        dispatcherProvider = TestDispatcherProvider()
    }

    @Test
    fun fetchNewsSources_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
        runTest {
            doReturn(flowOf(emptyList<SourceData>()))
                .`when`(newsSourcesRepository)
                .getNewsSources()
            val viewModel = NewsSourcesViewModel(newsSourcesRepository, dispatcherProvider)
            viewModel.uiState.test {
                assertEquals(UiState.Success(emptyList<SourceData>()), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            verify(newsSourcesRepository, times(1))
                .getNewsSources()
        }
    }

    @Test
    fun fetchNewsSources_whenRepositoryResponseError_shouldSetErrorUiState() {
        runTest {
            val errorMessage = "Error Message For You"
            doReturn(flow<List<SourceData>> {
                throw IllegalStateException(errorMessage)
            }).`when`(newsSourcesRepository)
                .getNewsSources()

            val viewModel = NewsSourcesViewModel(newsSourcesRepository, dispatcherProvider)
            viewModel.uiState.test {
                assertEquals(
                    UiState.Error(IllegalStateException(errorMessage).toString()),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }

            verify(newsSourcesRepository, times(1))
                .getNewsSources()
        }
    }
}