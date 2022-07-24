package com.rtl.android.assignment.api.model

import com.squareup.moshi.Json


data class Hourly(
    @Json(name = "dt") val dt: Int? = null,
    @Json(name = "temp") val temp: Double? = null,
    @Json(name = "feels_like") val feelsLike: Double? = null,
    @Json(name = "pressure") val pressure: Int? = null,
    @Json(name = "humidity") val humidity: Int? = null,
    @Json(name = "dew_point") val dewPoint: Double? = null,
    @Json(name = "uvi") val uvi: Double? = null,
    @Json(name = "clouds") val clouds: Int? = null,
    @Json(name = "visibility") val visibility: Int? = null,
    @Json(name = "wind_speed") val windSpeed: Double? = null,
    @Json(name = "wind_deg") val windDeg: Int? = null,
    @Json(name = "wind_gust") val windGust: Double? = null,
    @Json(name = "weather") val weather: List<Weather> = emptyList(),
    @Json(name = "pop") var pop: Double? = null
)