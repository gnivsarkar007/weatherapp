package com.rtl.android.assignment.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rtl.android.assignment.repository.MapperInput
import com.rtl.android.assignment.repository.Result
import com.rtl.android.assignment.repository.WeatherInfoToViewStateMapper
import com.rtl.android.assignment.repository.WeatherRepo
import com.rtl.android.assignment.ui.viewstate.CityWeatherDetailsModel
import com.rtl.android.assignment.ui.viewstate.CityWeatherDetailsViewState
import com.rtl.android.assignment.ui.viewstate.SingleCityWeatherEvents
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SingleCityWeatherViewModel @Inject constructor(
    private val repository: WeatherRepo,
    private val mapper: WeatherInfoToViewStateMapper
) : ViewModel() {

    private val mutableFlow = MutableStateFlow(
        CityWeatherDetailsViewState(viewState = CityWeatherDetailsModel.Loading)
    )

    val flow: StateFlow<CityWeatherDetailsViewState> = mutableFlow

    @VisibleForTesting
    var lastLoadedCity: String? = null

    val eventsHandler: EventsHandler<SingleCityWeatherEvents> = object :
        EventsHandler<SingleCityWeatherEvents> {
        override fun onEvent(event: SingleCityWeatherEvents) = when (event) {
            SingleCityWeatherEvents.CloseDetails -> mutableFlow.value =
                CityWeatherDetailsViewState(CityWeatherDetailsModel.Finish)
            is SingleCityWeatherEvents.Load -> openDetails(event.model, event.name)
            is SingleCityWeatherEvents.Refresh -> openDetails(name = lastLoadedCity.orEmpty())
        }
    }

    private fun openDetails(model: CityWeatherDetailsModel? = null, name: String) {
        lastLoadedCity = name
        model?.let {
            mutableFlow.value = CityWeatherDetailsViewState(it)
            return
        }
        viewModelScope.launch {
            mutableFlow.value = CityWeatherDetailsViewState(CityWeatherDetailsModel.Loading)

            mutableFlow.value = when (val data = repository.getWeather(name)) {
                is Result.Error -> {
                    val error = when (data.type) {
                        Result.Error.Type.API -> CityWeatherDetailsModel.Error.Api
                        Result.Error.Type.NETWORK -> CityWeatherDetailsModel.Error.NoNetwork
                        Result.Error.Type.DB -> CityWeatherDetailsModel.Error.Api
                    }
                    CityWeatherDetailsViewState(error)
                }
                is Result.Success -> CityWeatherDetailsViewState(
                    CityWeatherDetailsModel.Success(
                        mapper.map(
                            MapperInput(data.data, name)
                        )
                    )
                )
            }
        }
    }

}
