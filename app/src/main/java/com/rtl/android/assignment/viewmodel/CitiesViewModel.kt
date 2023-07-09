package com.rtl.android.assignment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rtl.android.assignment.cache.database.entity.CityEntity
import com.rtl.android.assignment.repository.CitiesRepo
import com.rtl.android.assignment.repository.Result
import com.rtl.android.assignment.ui.viewstate.CitiesListEvents
import com.rtl.android.assignment.ui.viewstate.CitiesListViewState
import com.rtl.android.assignment.ui.viewstate.CitiesListingModel
import com.rtl.android.assignment.ui.viewstate.ListingCell
import com.rtl.android.assignment.ui.viewstate.NavigationEvents
import com.rtl.android.assignment.util.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val repository: CitiesRepo,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val mutableFlow = MutableStateFlow(
        CitiesListViewState(viewState = CitiesListingModel.Loading)
    )

    val flow: StateFlow<CitiesListViewState> = mutableFlow
    val eventsHandler: EventsHandler<CitiesListEvents> = object :
        EventsHandler<CitiesListEvents> {
        override fun onEvent(event: CitiesListEvents) {
            this@CitiesViewModel.onEvent(event = event)
        }
    }

    private val mutableNavFlow = Channel<NavigationEvents?>(Channel.BUFFERED)
    val navigationFlow = mutableNavFlow.receiveAsFlow()

    init {
        getCities()
    }

    private fun onEvent(event: CitiesListEvents) = when (event) {
        is CitiesListEvents.OpenDetails -> {
            viewModelScope.launch { mutableNavFlow.send(event) }
        }

        is CitiesListEvents.AddCity -> event.run { addCity(id, name, latitude, longitude) }
        is CitiesListEvents.OpenSearch -> viewModelScope.launch { mutableNavFlow.send(event) }
        is CitiesListEvents.RemoveCity -> event.run { removeCity(name) }
    }

    private fun removeCity(name: String) {
        viewModelScope.launch(dispatcher) {
            repository.removeCity(name)
        }
    }

    private fun addCity(id: String, name: String, latitude: Double, longitude: Double) {
        viewModelScope.launch(dispatcher) {
            repository.addCity(CityEntity(id, name, latitude, longitude))
        }
    }

    private fun getCities() {
        viewModelScope.launch(dispatcher) {
            mutableFlow.value = CitiesListViewState(CitiesListingModel.Loading)
            repository.getAllCities().collectLatest { result ->
                when (result) {
                    is Result.Error -> {
                        mutableFlow.value = when (result.type) {
                            Result.Error.Type.NETWORK -> CitiesListViewState(CitiesListingModel.Error.NoNetwork)
                            else -> CitiesListViewState(CitiesListingModel.Error.Api)
                        }
                    }

                    is Result.Success -> {
                        val mapped = result.data.map { ListingCell(it.name) }
                        mutableFlow.value = CitiesListViewState(
                            CitiesListingModel.Success(
                                mapped
                            )
                        )
                    }
                }
            }

        }
    }

}
