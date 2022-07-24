package com.rtl.android.assignment.api.model

import com.squareup.moshi.Json


data class Alerts(
    @Json(name = "sender_name") var senderName: String? = null,
    @Json(name = "event") var event: String? = null,
    @Json(name = "start") var start: Int? = null,
    @Json(name = "end") var end: Int? = null,
    @Json(name = "description") var description: String? = null,
    @Json(name = "tags") var tags: List<String> = emptyList()
)