package com.rtl.android.assignment.viewmodel

import com.rtl.android.assignment.BaseViewModelTest
import com.rtl.android.assignment.cache.database.entity.CityEntity
import com.rtl.android.assignment.repository.CitiesRepo
import com.rtl.android.assignment.repository.Result
import com.rtl.android.assignment.ui.viewstate.CitiesListEvents
import com.rtl.android.assignment.ui.viewstate.CitiesListingModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class CitiesViewModelTest : BaseViewModelTest() {
    private lateinit var viewModel: CitiesViewModel
    private val repository: CitiesRepo = mock()
    private val dataFlow = MutableStateFlow(Result.Success<List<CityEntity>>(entities()))

    @Before
    override fun setup() {
        whenever(repository.getAllCities()).thenReturn(dataFlow)

        super.setup()
        viewModel = CitiesViewModel(repository, dispatcher)
    }

    @Test
    fun `test loading`() = runTest {
        assert(viewModel.flow.value.viewState is CitiesListingModel.Loading)

        dataFlow.emit(Result.Success<List<CityEntity>>(entities()))
        advanceUntilIdle()

        assert(viewModel.flow.value.viewState is CitiesListingModel.Success)


        (viewModel.flow.value.viewState as CitiesListingModel.Success).run {
            with(data) {
                assert(size == 5)
                forEachIndexed { index, cell ->
                    assert(cell.title == dataFlow.value.data[index].name)
                }
            }
        }
    }

    @Test
    fun `test api error flow`() = runTest {
        val dataFlow = MutableStateFlow(Result.Error(Result.Error.Type.API))
        whenever(repository.getAllCities()).thenReturn(dataFlow)

        assert(viewModel.flow.value.viewState is CitiesListingModel.Loading)

        dataFlow.emit(Result.Error(Result.Error.Type.API))
        advanceUntilIdle()

        assert(viewModel.flow.value.viewState is CitiesListingModel.Error)
        assert((viewModel.flow.value.viewState as CitiesListingModel.Error) == CitiesListingModel.Error.Api)

    }

    @Test
    fun `test OpenDetails`() = runTest {
        viewModel.eventsHandler.onEvent(CitiesListEvents.OpenDetails("name"))
        assert((viewModel.navigationFlow.value is CitiesListEvents.OpenDetails))
        assert(
            (viewModel.navigationFlow.value as CitiesListEvents.OpenDetails).cityName.contentEquals(
                "name"
            )
        )
    }

    @Test
    fun `test OpenSearch`() = runTest {
        val event = CitiesListEvents.OpenSearch()
        viewModel.eventsHandler.onEvent(event)
        assert((viewModel.navigationFlow.value === event))
    }

    @Test
    fun `test AddCity`() = runTest {
        viewModel.eventsHandler.onEvent(CitiesListEvents.AddCity("city", 10.0, 20.0))
        advanceUntilIdle()
        verify(repository).addCity("city", 10.0, 20.0)
    }

    @Test
    fun `test RemoveCity`() = runTest {
        viewModel.eventsHandler.onEvent(CitiesListEvents.RemoveCity("city"))
        advanceUntilIdle()
        verify(repository).removeCity("city")
    }

    private fun entities() = mutableListOf<CityEntity>().also {
        for (i in 1..5) {
            it.add(
                CityEntity(
                    "title$i",
                    10.0 + i,
                    20.0 + i
                )
            )
        }
    }

}

