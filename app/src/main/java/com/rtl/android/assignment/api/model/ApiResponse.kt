package com.rtl.android.assignment.api.model

import com.squareup.moshi.Json


data class ApiResponse(
    @Json(name = "lat") var lat: Double? = null,
    @Json(name = "lon") var lon: Double? = null,
    @Json(name = "timezone") var timezone: String? = null,
    @Json(name = "timezone_offset") var timezoneOffset: Int? = null,
    @Json(name = "current") var current: Current? = Current(),
    @Json(name = "hourly") var hourly: List<Hourly> = emptyList(),
    @Json(name = "daily") var daily: List<Daily> = emptyList(),
)