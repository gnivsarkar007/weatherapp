package com.rtl.android.assignment.api.model

import com.squareup.moshi.Json


data class Daily(
    @Json(name = "dt") var dt: Int? = null,
    @Json(name = "sunrise") var sunrise: Int? = null,
    @Json(name = "sunset") var sunset: Int? = null,
    @Json(name = "moonrise") var moonrise: Int? = null,
    @Json(name = "moonset") var moonset: Int? = null,
    @Json(name = "moon_phase") var moonPhase: Double? = null,
    @Json(name = "temp") var temp: Temp? = Temp(),
    @Json(name = "feels_like") var feelsLike: FeelsLike? = FeelsLike(),
    @Json(name = "pressure") var pressure: Int? = null,
    @Json(name = "humidity") var humidity: Int? = null,
    @Json(name = "dew_point") var dewPoint: Double? = null,
    @Json(name = "wind_speed") var windSpeed: Double? = null,
    @Json(name = "wind_deg") var windDeg: Int? = null,
    @Json(name = "wind_gust") var windGust: Double? = null,
    @Json(name = "weather") var weather: List<Weather> = emptyList(),
    @Json(name = "clouds") var clouds: Int? = null,
    @Json(name = "pop") var pop: Double? = null,
    @Json(name = "rain") var rain: Double? = null,
    @Json(name = "uvi") var uvi: Double? = null
)