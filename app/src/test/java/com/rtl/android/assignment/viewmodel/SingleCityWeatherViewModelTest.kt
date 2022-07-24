package com.rtl.android.assignment.viewmodel

import com.rtl.android.assignment.BaseViewModelTest
import com.rtl.android.assignment.cache.database.entity.CityEntity
import com.rtl.android.assignment.repository.MapperInput
import com.rtl.android.assignment.repository.Result
import com.rtl.android.assignment.repository.WeatherInfoToViewStateMapper
import com.rtl.android.assignment.repository.WeatherRepo
import com.rtl.android.assignment.ui.viewstate.CityWeatherDetailsModel
import com.rtl.android.assignment.ui.viewstate.SingleCityWeatherEvents
import com.rtl.android.assignment.util.FileReader
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class SingleCityWeatherViewModelTest : BaseViewModelTest() {
    lateinit var viewModel: SingleCityWeatherViewModel

    private val repository: WeatherRepo = mock()
    private val mapper = WeatherInfoToViewStateMapper()


    override fun setup() {
        super.setup()
        viewModel = SingleCityWeatherViewModel(repository, mapper)
    }

    @Test
    fun `test load success`() = runTest {
        val response = FileReader.apiResponse()
        val expectedEntity = CityEntity(
            "title",
            10.0,
            20.0
        )
        val expectedMappedState = mapper.map(MapperInput(response!!, "title"))
        val expectedResult = Result.Success(expectedEntity)
        val dataFlow = MutableStateFlow(expectedResult)
        whenever(repository.getWeather(expectedEntity.name)).thenReturn(Result.Success(response))

        viewModel.eventsHandler.onEvent(SingleCityWeatherEvents.Load(name = expectedEntity.name))

        assert(viewModel.flow.value.viewState is CityWeatherDetailsModel.Loading)
        dataFlow.emit(Result.Success(expectedEntity))

        advanceUntilIdle()

        assert(viewModel.flow.value.viewState is CityWeatherDetailsModel.Success)
        ((viewModel.flow.value.viewState as CityWeatherDetailsModel.Success).data).run {
            assert(header == expectedMappedState.header)
            assert(hourlyForecastSection == expectedMappedState.hourlyForecastSection)
            assert(dailyForeCast == expectedMappedState.dailyForeCast)
        }
    }

    @Test
    fun `test load fail`() = runTest {
        val expectedResult = Result.Error(Result.Error.Type.API)
        val dataFlow = MutableStateFlow(expectedResult)
        whenever(repository.getWeather(any())).thenReturn(expectedResult)

        viewModel.eventsHandler.onEvent(SingleCityWeatherEvents.Load(name = "title"))

        assert(viewModel.flow.value.viewState is CityWeatherDetailsModel.Loading)
        dataFlow.emit(expectedResult)

        advanceUntilIdle()

        assert(viewModel.flow.value.viewState is CityWeatherDetailsModel.Error.Api)

    }

    @Test
    fun `test CloseDetails flow`() {
        viewModel.eventsHandler.onEvent(SingleCityWeatherEvents.CloseDetails)
        assert(viewModel.flow.value.viewState is CityWeatherDetailsModel.Finish)
    }

    @Test
    fun `test refresh flow`() = runTest {
        val response = FileReader.apiResponse()
        viewModel.lastLoadedCity = "title"
        whenever(repository.getWeather("title")).thenReturn(Result.Success(response!!))

        viewModel.eventsHandler.onEvent(SingleCityWeatherEvents.Refresh())
        advanceUntilIdle()
        verify(repository).getWeather("title")
    }


}



