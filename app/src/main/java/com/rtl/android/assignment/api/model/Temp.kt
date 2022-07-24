package com.rtl.android.assignment.api.model

import com.squareup.moshi.Json


data class Temp(
    @Json(name = "day") var day: Double? = null,
    @Json(name = "min") var min: Double? = null,
    @Json(name = "max") var max: Double? = null,
    @Json(name = "night") var night: Double? = null,
    @Json(name = "eve") var eve: Double? = null,
    @Json(name = "morn") var morn: Double? = null
)