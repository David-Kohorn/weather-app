/**
 * David Kohorn
 */
package wpics.weather.viewmodel

import wpics.weather.model.location.Coordinate
import wpics.weather.model.weather.CurrentForecast
import wpics.weather.model.weather.CurrentWeather
import wpics.weather.model.weather.UnitSystem

/**
 * This stores the current UI state for our app's weather information
 */
data class WeatherUIState(
    val currentWeather: CurrentWeather? = null,
    val currentForecast: CurrentForecast? = null,
    val unitSystem: UnitSystem = UnitSystem.METRIC,
    val coordinate: Coordinate? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)