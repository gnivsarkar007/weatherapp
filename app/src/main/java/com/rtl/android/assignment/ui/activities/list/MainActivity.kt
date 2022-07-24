package com.rtl.android.assignment.ui.activities.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.rtl.android.assignment.BuildConfig
import com.rtl.android.assignment.app.AppTheme
import com.rtl.android.assignment.app.MainApplication
import com.rtl.android.assignment.ui.activities.details.DetailsActivity
import com.rtl.android.assignment.ui.activities.details.DetailsActivity.Companion.INTENT_KEY_CITY_NAME
import com.rtl.android.assignment.ui.composables.MainScreen
import com.rtl.android.assignment.ui.viewstate.CitiesListEvents
import com.rtl.android.assignment.viewmodel.CitiesViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModel: CitiesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MainApplication).appComponent.inject(this)
        if (Places.isInitialized().not()) {
            Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)
        }

        lifecycleScope.launch {
            viewModel.navigationFlow.collect { event ->
                event?.let {
                    when (it) {
                        is CitiesListEvents.OpenDetails -> {
                            Intent(this@MainActivity, DetailsActivity::class.java).apply {
                                putExtra(INTENT_KEY_CITY_NAME, it.cityName)
                                this@MainActivity.startActivity(this)
                            }
                        }
                        is CitiesListEvents.OpenSearch -> {
                            val fields =
                                listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
                            val intent = Autocomplete.IntentBuilder(
                                AutocompleteActivityMode.OVERLAY,
                                fields
                            ).setTypeFilter(TypeFilter.CITIES).build(this@MainActivity)
                            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
                        }
                    }
                }
            }
        }
        setContent {
            AppTheme {
                val activityState by viewModel.flow.collectAsState()
                MainScreen(state = activityState, eventsHandler = viewModel.eventsHandler)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        Autocomplete.getPlaceFromIntent(data).run {
                            val name = this.name.orEmpty()
                            val lat = latLng?.latitude ?: 0.0
                            val long = latLng?.longitude ?: 0.0
                            viewModel.eventsHandler.onEvent(
                                CitiesListEvents.AddCity(
                                    name,
                                    lat,
                                    long
                                )
                            )
                        }
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {

                }
                Activity.RESULT_CANCELED -> {
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        if (Places.isInitialized()) {
            Places.deinitialize()
        }
        super.onDestroy()
    }

    companion object {
        const val BUNDLE_KEY_APP_STATE = "appState"
        private const val AUTOCOMPLETE_REQUEST_CODE = 1
    }

}

