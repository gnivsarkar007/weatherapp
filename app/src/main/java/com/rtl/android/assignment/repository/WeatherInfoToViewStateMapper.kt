package com.rtl.android.assignment.repository

import com.rtl.android.assignment.api.model.ApiResponse
import com.rtl.android.assignment.ui.viewstate.CityWeatherModel
import com.rtl.android.assignment.ui.viewstate.ForeCast
import com.rtl.android.assignment.ui.viewstate.Header
import java.text.DecimalFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import java.util.SimpleTimeZone
import javax.inject.Inject

data class MapperInput(val response: ApiResponse, val cityName: String)
class WeatherInfoToViewStateMapper @Inject constructor() {

    private val format = DecimalFormat("##.##")

    fun map(input: MapperInput): CityWeatherModel {
        val blankTemp = BLANK.toDegrees()
        val offset = input.response.timezoneOffset ?: 0
        val zoneId = input.response.timezone.orEmpty()
        val currentTemp = input.response.current?.temp?.toDegrees() ?: blankTemp
        val feelsLike: String = input.response.current?.feelsLike?.toDegrees() ?: blankTemp
        val humidity: String = (input.response.current?.humidity?.toString() ?: "-").toPercentage()
        val windInfo =
            "${(((input.response.current?.windSpeed ?: 0.0) * 3.6).toInt().toString())} km/h"
        val lastUpdated = mapToTime(
            LocalDateTime.ofInstant(
                Instant.ofEpochSecond(
                    (input.response.current?.dt?.toLong() ?: 0L)
                ), SimpleTimeZone(offset, zoneId).toZoneId()
            )
        )
        val header = Header(
            input.cityName,
            currentTemp,
            feelsLike,
            humidity,
            windInfo,
            lastUpdated,
            mapToTime(LocalDateTime.now())
        )
        val hourlyForecast = input.response.hourly.map {
            val time = it.dt?.let { time ->
                mapToTime(
                    LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(time.toLong()),
                        SimpleTimeZone(offset, zoneId).toZoneId()
                    )
                )
            }.orEmpty()
            val temp = it.temp?.toString()?.toDegrees() ?: blankTemp
            val rain = it.pop.toPercentage()
            ForeCast(time, temp, rain)
        }
        val daily = input.response.daily.map {
            val time = it.dt?.let { time ->
                mapToDate(
                    LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(time.toLong()),
                        SimpleTimeZone(offset, zoneId).toZoneId()
                    )
                )
            }.orEmpty()
            val temp =
                it.temp?.let { "H:${it.max.toDegrees()}, L:${it.min.toDegrees()}" } ?: blankTemp
            val rain = it.pop.toPercentage()
            ForeCast(time, temp, rain)
        }
        return CityWeatherModel(header, hourlyForecast, daily)
    }

    private fun mapToTime(dateTime: LocalDateTime): String =
        DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(dateTime)

    private fun mapToDate(dateTime: LocalDateTime): String =
        DateTimeFormatter.ofPattern("dd MMM", Locale.getDefault()).format(dateTime)

    private fun Double?.toPercentage() = ((this ?: 0.0) * 100).toInt().toString().toPercentage()
    private fun Double?.toDegrees() = format.format((this ?: 0.0)).toDegrees()


    private fun String.toDegrees() = "$this$DEGREES_CELSIUS"
    private fun String.toPercentage() = "$this$PERCENTAGE"

    companion object {
        const val DEGREES_CELSIUS = "\u2103 "
        const val PERCENTAGE = "\u0025"
        const val BLANK = "-"
    }
}