/**
 * David Kohorn
 */
package wpics.weather.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import wpics.weather.model.location.Coordinate
import wpics.weather.model.weather.UnitSystem
import wpics.weather.model.weather.WeatherRepository

/**
 * This class is the [ViewModel] class for the weather app that
 * handles the state for all composables.
 */
class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    /**
     * Stores the UI state that will be accessed by the various composable functions.
     */
    private val _uiState = MutableStateFlow(WeatherUIState())
    val uiState: StateFlow<WeatherUIState> = _uiState

    /**
     * Fetches the current weather and forecast for [coordinate] using the currently selected unit system.
     */
    fun fetchData(coordinate: Coordinate) {
        _uiState.value = _uiState.value.copy(
            coordinate = coordinate,
            isLoading = true,
            error = null
        )

        viewModelScope.launch {
            try {
                val units = _uiState.value.unitSystem.api
                val currentWeather = repository.weatherForCoordinate(coordinate, units)
                val currentForecast = repository.forecastForCoordinate(coordinate, units)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = null,
                    currentWeather = currentWeather,
                    currentForecast = currentForecast,
                )
            } catch (e: Exception) {
                Log.e("API_ERROR", "Network call failed", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    currentWeather = null,
                    currentForecast = null,
                    error = "Failed to load weather and forecast"
                )
            }
        }
    }

    /**
     * Stores the selected [unitSystem] and, if a coordinate has already
     * been fetched, fetches new data for it in the new units.
     */
    fun updateUnitSystem(unitSystem: UnitSystem) {
        _uiState.value = _uiState.value.copy(unitSystem = unitSystem)

        val coordinate = _uiState.value.coordinate ?: return
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val currentWeather = repository.weatherForCoordinate(coordinate, unitSystem.api)
                val currentForecast = repository.forecastForCoordinate(coordinate, unitSystem.api)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = null,
                    currentWeather = currentWeather,
                    currentForecast = currentForecast,
                )
            } catch (e: Exception) {
                Log.e("API_ERROR", "Network call failed", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    currentWeather = null,
                    currentForecast = null,
                    error = "Failed to load weather and forecast"
                )
            }
        }
    }
}