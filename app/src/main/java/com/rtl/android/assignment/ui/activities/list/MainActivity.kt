package com.rtl.android.assignment.ui.activities.list

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.rtl.android.assignment.BuildConfig
import com.rtl.android.assignment.R
import com.rtl.android.assignment.app.AppTheme
import com.rtl.android.assignment.ui.composables.AddCityButton
import com.rtl.android.assignment.ui.composables.AppBar
import com.rtl.android.assignment.ui.composables.DetailsScreen
import com.rtl.android.assignment.ui.composables.MainScreen
import com.rtl.android.assignment.ui.viewstate.CitiesListEvents
import com.rtl.android.assignment.viewmodel.CitiesViewModel
import com.rtl.android.assignment.viewmodel.SingleCityWeatherViewModel
import dagger.hilt.android.AndroidEntryPoint


@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Places.isInitialized().not()) {
            Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)
        }


        setContent {
            AppTheme {

                var addCityEvent: CitiesListEvents.AddCity? by remember {
                    mutableStateOf(null)
                }
                var showBackButton by remember {
                    mutableStateOf(false)
                }

                val autocompletePlacesLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult(),
                    onResult = { result ->
                        when (result.resultCode) {
                            Activity.RESULT_OK -> {
                                result.data?.let { intent ->
                                    Autocomplete.getPlaceFromIntent(intent).run {
                                        val name = this.name.orEmpty()
                                        val lat = latLng?.latitude ?: 0.0
                                        val long = latLng?.longitude ?: 0.0
                                        this.id?.let { id ->
                                            addCityEvent = CitiesListEvents.AddCity(
                                                id,
                                                name,
                                                lat,
                                                long
                                            )
                                        }


                                    }
                                }
                            }

                            AutocompleteActivity.RESULT_ERROR -> {
                                Log.i("TAG",
                                    result.data?.let { Autocomplete.getStatusFromIntent(it).statusMessage }
                                        .orEmpty()
                                )
                            }

                            Activity.RESULT_CANCELED -> {
                                Log.i("TAG", "User canceled autocomplete")
                            }
                        }
                    }
                )
                val navController = rememberNavController()

                Scaffold(
                    scaffoldState = rememberScaffoldState(),
                    topBar = {
                        TopAppBar(
                            title = {
                                if(showBackButton) {
                                    Text(
                                        stringResource(R.string.top_bar_title),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.h2,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                    )
                                }
                            },
                            navigationIcon = {
                                if (showBackButton) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowBack,
                                        contentDescription = null,
                                        modifier = Modifier.clickable { navController.popBackStack(route = "list", inclusive = false) }
                                    )
                                }
                            }
                        )
                    },
                    floatingActionButtonPosition = FabPosition.End,
                    floatingActionButton = {
                        if(showBackButton.not()){
                            val context = LocalContext.current
                            AddCityButton {
                                val fields =
                                    listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
                                val intent = Autocomplete.IntentBuilder(
                                    AutocompleteActivityMode.OVERLAY,
                                    fields
                                ).setTypesFilter(listOf(PlaceTypes.CITIES)).build(context)
                                autocompletePlacesLauncher.launch(intent)
                            }
                        }
                    },
                ) {

                    NavHost(
                        modifier = Modifier.padding(it),
                        navController = navController,
                        startDestination = "list"
                    ) {
                        composable("list") {
                            showBackButton = false
                            val viewModel: CitiesViewModel = hiltViewModel()
                            when (val event = viewModel.navigationFlow.collectAsState(null).value) {
                                is CitiesListEvents.OpenDetails -> LaunchedEffect(
                                    key1 = Unit,
                                    block = {
                                        navController.navigate(route = "detail/${event.cityName}")
                                    }
                                )

                                else -> {}
                            }
                            LaunchedEffect(key1 = addCityEvent) {
                                addCityEvent?.let { event -> viewModel.eventsHandler.onEvent(event) }
                            }
                            BackHandler {
                                navController.popBackStack("list", inclusive = true)
                            }
                            MainScreen(
                                state = viewModel.flow.collectAsState().value,
                                openSearch = { viewModel.eventsHandler.onEvent(CitiesListEvents.OpenSearch()) },
                                onCityClicked = { cityName ->
                                    viewModel.eventsHandler.onEvent(
                                        CitiesListEvents.OpenDetails(
                                            cityName
                                        )
                                    )
                                },
                                onCityRemoveClicked = { cityName ->
                                    viewModel.eventsHandler.onEvent(CitiesListEvents.RemoveCity(cityName))
                                }
                            )
                        }
                        composable(
                            "detail/{$INTENT_KEY_CITY_NAME}",
                            arguments = listOf(navArgument(INTENT_KEY_CITY_NAME) {
                                type = NavType.StringType
                            })
                        ) {
                            showBackButton = true

                            val viewModel: SingleCityWeatherViewModel = hiltViewModel()
                            BackHandler {
                                navController.popBackStack(route = "list", inclusive = false)
                            }
                            DetailsScreen(
                                state = viewModel.flow.collectAsState().value.viewState,
                                eventsHandler = viewModel.eventsHandler
                            ) {
                                navController.popBackStack(route = "list", inclusive = false)
                            }
                        }

                    }
                }
            }
        }

    }


    override fun onDestroy() {
        if (Places.isInitialized()) {
            Places.deinitialize()
        }
        super.onDestroy()
    }

    companion object {
        const val INTENT_KEY_CITY_NAME = "cityName"
    }

}

