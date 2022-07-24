package com.rtl.android.assignment.api.model

import com.squareup.moshi.Json


data class Weather(
    @Json(name = "id") var id: Int? = null,
    @Json(name = "main") var main: String? = null,
    @Json(name = "description") var description: String? = null,
    @Json(name = "icon") var icon: String? = null
)