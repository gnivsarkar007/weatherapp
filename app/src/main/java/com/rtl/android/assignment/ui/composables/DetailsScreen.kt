package com.rtl.android.assignment.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rtl.android.assignment.R
import com.rtl.android.assignment.app.WHITE
import com.rtl.android.assignment.ui.viewstate.CityWeatherDetailsModel
import com.rtl.android.assignment.ui.viewstate.ForeCast
import com.rtl.android.assignment.ui.viewstate.SingleCityWeatherEvents
import com.rtl.android.assignment.viewmodel.EventsHandler

val whiteRoundedBorder: () -> Modifier =
    { Modifier.border(border = BorderStroke(1.dp, WHITE), shape = RoundedCornerShape(8.dp)) }

@Composable
fun DetailsScreen(
    state: CityWeatherDetailsModel,
    eventsHandler: EventsHandler<SingleCityWeatherEvents>,
    onFinish: () -> Unit
) {
    when (state) {
        is CityWeatherDetailsModel.Success -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
                contentAlignment = Alignment.TopCenter
            ) {

                Box(contentAlignment = Alignment.BottomCenter) {
                    Column {
                        Row(
                            modifier = Modifier
                                .height(56.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                                contentDescription = null,
                                modifier = Modifier
                                    .clickable { onFinish() }
                            )
                            AppBar(true)
                        }
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .verticalScroll(enabled = true, state = rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Spacer(modifier = Modifier.height(1.dp))
                            Text(
                                text = state.data.header.cityName,
                                textAlign = TextAlign.Start,
                                style = MaterialTheme.typography.h1
                            )
                            Row {
                                Text(
                                    text = "Updated at: ${state.data.header.updatedAtTime}, ${state.data.header.cityName} time." +
                                            "\nYour local time is ${state.data.header.localTime}",
                                    textAlign = TextAlign.Start,
                                    style = MaterialTheme.typography.body1
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.ic_refresh),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .clickable { eventsHandler.onEvent(SingleCityWeatherEvents.Refresh()) }
                                )
                            }

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Current temperature is ${state.data.header.currentTemp}",
                                    textAlign = TextAlign.Start,
                                    style = MaterialTheme.typography.h3
                                )
                                Text(
                                    text = "Feels more like ${state.data.header.feelsLike}",
                                    textAlign = TextAlign.Start,
                                    style = MaterialTheme.typography.h3
                                )
                            }

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                RowElement(
                                    icon = R.drawable.ic_warped,
                                    text = "Humidity today is ${state.data.header.humidity}"
                                )
                                RowElement(
                                    icon = R.drawable.ic_stars,
                                    text = "Wind speed ${state.data.header.windInfo}"
                                )
                            }
                            Column(
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = whiteRoundedBorder().padding(8.dp)
                            ) {
                                Text(
                                    text = "Hourly forecast for today",
                                    style = MaterialTheme.typography.h2,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Row(
                                    modifier = Modifier.horizontalScroll(
                                        rememberScrollState(),
                                        enabled = true
                                    ), horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    state.data.hourlyForecastSection.forEach {
                                        it.HourlyForecast()
                                    }
                                }
                            }

                            Column(
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = whiteRoundedBorder().padding(8.dp)
                            ) {
                                Text(
                                    text = "Daily forecast for this week",
                                    style = MaterialTheme.typography.h2,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    state.data.dailyForeCast.forEach {
                                        it.DailyForecast()
                                    }
                                }
                            }
                        }
                    }
                }


            }
        }

        is CityWeatherDetailsModel.Finish -> onFinish()

        is CityWeatherDetailsModel.Error.Api, CityWeatherDetailsModel.Error.Db -> ErrorScreen(
            state = Errors.API,
            onButtonClick = { eventsHandler.onEvent(SingleCityWeatherEvents.Refresh()) }
        )

        is CityWeatherDetailsModel.Error.NoNetwork -> ErrorScreen(
            state = Errors.NETWORK,
            onButtonClick = { }
        )

        CityWeatherDetailsModel.Loading -> Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
        ) {
            CircularProgressIndicator(
                color = WHITE
            )
        }
    }
}

@Composable
fun ForeCast.HourlyForecast() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .border(
                border = BorderStroke(1.dp, WHITE),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(4.dp)
    ) {
        Text(
            text = time,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = temperature,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Image(painter = painterResource(id = R.drawable.ic_cloud_rain), contentDescription = "")
            Text(
                text = rainPercentage,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ForeCast.DailyForecast() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = whiteRoundedBorder()
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = time,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(0.25f)
                .fillMaxWidth()
        )
        Text(
            text = temperature,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(0.5f)
                .fillMaxWidth()
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .weight(0.25f)
                .fillMaxWidth()
        ) {
            Image(painter = painterResource(id = R.drawable.ic_cloud_rain), contentDescription = "")
            Text(
                text = rainPercentage,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun RowElement(
    icon: Int,
    text: String
) {
    Row(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = icon), contentDescription = "")
        Text(text = text, style = MaterialTheme.typography.body1)
    }
}
