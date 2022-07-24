package com.rtl.android.assignment.ui.viewstate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


sealed class CitiesListingModel {
    object Loading : CitiesListingModel()
    data class Success(
        val data: List<ListingCell>,
    ) : CitiesListingModel()

    sealed class Error : CitiesListingModel() {
        object NoNetwork : Error()
        object Api : Error()
    }

}

data class ListingCell(val title: String)


sealed class CityWeatherDetailsModel : Parcelable {
    @Parcelize
    object Loading : CityWeatherDetailsModel(), Parcelable

    @Parcelize
    data class Success(val data: CityWeatherModel) : CityWeatherDetailsModel(), Parcelable

    @Parcelize
    object Finish : CityWeatherDetailsModel(), Parcelable

    sealed class Error : CityWeatherDetailsModel(), Parcelable {
        @Parcelize
        object NoNetwork : Error(), Parcelable
        @Parcelize
        object Api : Error(), Parcelable
        @Parcelize
        object Db : Error(), Parcelable
    }

}

@Parcelize
data class CityWeatherModel(
    val header: Header,
    val hourlyForecastSection: List<ForeCast> = emptyList(),
    val dailyForeCast: List<ForeCast> = emptyList()
) : Parcelable

@Parcelize
data class Header(
    val cityName: String,
    val currentTemp: String,
    val feelsLike: String,
    val humidity: String,
    val windInfo: String,
    val updatedAtTime: String,
    val localTime: String
) : Parcelable

@Parcelize
data class ForeCast(
    val time: String,
    val temperature: String,
    val rainPercentage: String,
) : Parcelable

data class CitiesListViewState(val viewState: CitiesListingModel)
data class CityWeatherDetailsViewState(val viewState: CityWeatherDetailsModel)