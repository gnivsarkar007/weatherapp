package com.rtl.android.assignment.ui.composables

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rtl.android.assignment.R
import com.rtl.android.assignment.app.WHITE
import com.rtl.android.assignment.ui.viewstate.CitiesListViewState
import com.rtl.android.assignment.ui.viewstate.CitiesListingModel
import com.rtl.android.assignment.ui.viewstate.ListingCell

@ExperimentalFoundationApi
@Composable
fun MainScreen(
    state: CitiesListViewState,
    onCityClicked: (String) -> Unit,
    openSearch: () -> Unit,
    onCityRemoveClicked: (String) -> Unit
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Crossfade(targetState = state.viewState){ pageState ->
            when (pageState) {

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
                    onCityClicked = onCityClicked,
                    openSearch = openSearch,
                    onCityRemoveClicked = onCityRemoveClicked
                )
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun CitiesListing(
    state: CitiesListingModel.Success,
    onCityClicked: (String) -> Unit,
    openSearch: () -> Unit,
    onCityRemoveClicked: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxSize(),
    ) {
        if (state.data.isNotEmpty()) {
            LazyColumn(
                state = rememberLazyListState(),
                modifier = Modifier
                    .padding(
                        8.dp
                    )
                    .fillMaxSize()
            ) {
                items(state.data, key = { it.title }) { listing ->
                    CityCell(
                        modifier = Modifier.animateItemPlacement(),
                        model = listing,
                        onCityRemoveClicked = { onCityRemoveClicked(it) },
                        onCityClicked = { onCityClicked(it) })
                }
            }
        } else {
            Text(
                stringResource(R.string.add_cities),
                style = MaterialTheme.typography.h1,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { openSearch() }
            )
        }
    }
}

@Composable
fun CityCell(
    modifier: Modifier = Modifier,
    model: ListingCell,
    onCityClicked: (String) -> Unit,
    onCityRemoveClicked: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier

    ) {
        Text(
            model.title,
            style = MaterialTheme.typography.h3,
            maxLines = 2,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.9f)
                .clickable { onCityClicked(model.title) }
        )

        Image(painterResource(id = R.drawable.ic_remove),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(4.dp)
                .weight(0.10f)
                .clickable {
                    onCityRemoveClicked(model.title)
                })
    }
}
