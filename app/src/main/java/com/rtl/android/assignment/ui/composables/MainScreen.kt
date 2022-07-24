package com.rtl.android.assignment.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FabPosition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rtl.android.assignment.R
import com.rtl.android.assignment.app.WHITE
import com.rtl.android.assignment.ui.viewstate.CitiesListEvents
import com.rtl.android.assignment.ui.viewstate.CitiesListViewState
import com.rtl.android.assignment.ui.viewstate.CitiesListingModel
import com.rtl.android.assignment.ui.viewstate.ListingCell
import com.rtl.android.assignment.viewmodel.EventsHandler

@Composable
fun MainScreen(
    state: CitiesListViewState,
    eventsHandler: EventsHandler<CitiesListEvents>
) {
    Scaffold(
        scaffoldState = rememberScaffoldState(),
        topBar = {
            TopAppBar(title = {
                AppBar(false)
            })
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddCityButton { eventsHandler.onEvent(CitiesListEvents.OpenSearch()) }
        },
        content = {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                when (val pageState = state.viewState) {

                    is CitiesListingModel.Error.Api -> ErrorScreen(
                        state = Errors.API,
                        onButtonClick = { }
                    )

                    is CitiesListingModel.Error.NoNetwork -> ErrorScreen(
                        state = Errors.NETWORK,
                        onButtonClick = { }
                    )

                    CitiesListingModel.Loading -> CircularProgressIndicator(
                        color = WHITE
                    )

                    is CitiesListingModel.Success -> CitiesListing(
                        pageState,
                        eventsHandler = eventsHandler
                    )
                }
            }

        },
        backgroundColor = MaterialTheme.colors.background
    )
}

@Composable
private fun CitiesListing(
    state: CitiesListingModel.Success,
    eventsHandler: EventsHandler<CitiesListEvents>
) {
    Box(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxSize(),
    ) {
        if (state.data.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .verticalScroll(enabled = true, state = rememberScrollState())
                    .padding(
                        8.dp
                    )
                    .fillMaxSize()
            ) {
                state.data.forEach { listing ->
                    CityCell(model = listing, eventsHandler)
                }

            }
        } else {
            Text(
                stringResource(R.string.add_cities),
                style = MaterialTheme.typography.h1,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { eventsHandler.onEvent(CitiesListEvents.OpenSearch()) }
            )
        }
    }
}

@Composable
fun CityCell(model: ListingCell, eventsHandler: EventsHandler<CitiesListEvents>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.clickable { eventsHandler.onEvent(CitiesListEvents.OpenDetails(model.title)) }

    ) {
        Text(
            model.title,
            style = MaterialTheme.typography.h3,
            maxLines = 2,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.9f)
        )

        Image(painterResource(id = R.drawable.ic_remove),
              contentDescription = null,
              modifier = Modifier
                  .fillMaxWidth()
                  .wrapContentHeight()
                  .padding(4.dp)
                  .weight(0.10f)
                  .clickable {
                      eventsHandler.onEvent(CitiesListEvents.RemoveCity(model.title))
                  })
    }
    Row(
        Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            .wrapContentHeight()
            .fillMaxWidth()
    ) {


    }
}
