package com.rtl.android.assignment.ui.activities.details

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.rtl.android.assignment.app.AppTheme
import com.rtl.android.assignment.app.MainApplication
import com.rtl.android.assignment.ui.composables.DetailsScreen
import com.rtl.android.assignment.ui.viewstate.CityWeatherDetailsModel
import com.rtl.android.assignment.ui.viewstate.SingleCityWeatherEvents
import com.rtl.android.assignment.viewmodel.SingleCityWeatherViewModel
import javax.inject.Inject

class DetailsActivity : ComponentActivity() {
    @Inject
    lateinit var viewModel: SingleCityWeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MainApplication).appComponent.inject(this)

        val onFinish = { finish() }
        setContent {
            AppTheme {
                val activityState by viewModel.flow.collectAsState()
                DetailsScreen(
                    activityState.viewState,
                    viewModel.eventsHandler,
                    onFinish
                )
            }
        }
        val name = intent.extras?.getString(INTENT_KEY_CITY_NAME) ?: ""

        viewModel.eventsHandler.onEvent(
            SingleCityWeatherEvents.Load(
                (savedInstanceState?.get(
                    BUNDLE_KEY_APP_STATE
                ) as? CityWeatherDetailsModel), name
            )
        )
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(BUNDLE_KEY_APP_STATE, viewModel.flow.value.viewState)
    }

    companion object {
        const val BUNDLE_KEY_APP_STATE = "appState"
        const val INTENT_KEY_CITY_NAME = "cityName"
    }
}