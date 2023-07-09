package com.rtl.android.assignment.ui.viewstate


sealed interface NavigationEvents

sealed class CitiesListEvents {
    class OpenDetails(val cityName: String) : CitiesListEvents(), NavigationEvents
    class OpenSearch : CitiesListEvents(), NavigationEvents { // needed so back to back search events can be fired, otherwise the flow thinks its the same event and ignores
        override fun equals(other: Any?): Boolean {
            return this === other
        }

        override fun hashCode(): Int {
            return System.identityHashCode(this)
        }
    }

    data class AddCity(val id: String, val name: String, val latitude: Double, val longitude: Double) :
        CitiesListEvents()

    data class RemoveCity(val name: String) : CitiesListEvents()
}

sealed class SingleCityWeatherEvents {
    object CloseDetails : SingleCityWeatherEvents()

    class Refresh :
        SingleCityWeatherEvents() { // needed so back to back refresh events can be fired, otherwise the flow thinks its the same event and ignores
        override fun equals(other: Any?): Boolean {
            return this === other
        }

        override fun hashCode(): Int {
            return System.identityHashCode(this)
        }
    }
}