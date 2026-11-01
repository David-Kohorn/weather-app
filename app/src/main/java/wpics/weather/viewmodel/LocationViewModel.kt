/**
 * David Kohorn
 */
package wpics.weather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import wpics.weather.model.location.LocationRepository

/**
 * This class is the [ViewModel] class for the weather app that
 * deals with the location
 */
class LocationViewModel(
    private val repository: LocationRepository
) : ViewModel() {

    /**
     * Stores the UI state that will be accessed by the various
     * composable functions.
     */
    private val _uiState = MutableStateFlow(LocationUIState())
    val uiState: StateFlow<LocationUIState> = _uiState

    /**
     * A callback function that handles results from location permission requests.
     *
     * @param granted True if permission was granted, false otherwise.
     */
    fun onPermissionResult(granted: Boolean) {
        _uiState.value = _uiState.value.copy(
            permissionGranted = granted,
            status = if (granted) "Permission granted. Tap Get My Location." else "Permission denied. Discuss privacy + fallback."
        )
    }

    /**
     * A callback function that initiates the process of fetching a location.
     */
    fun fetchLocation() {
        if (!_uiState.value.permissionGranted) return
        _uiState.value = _uiState.value.copy(isLoading = true, status = "Fetching location…")

        viewModelScope.launch {
            val cord = repository.getLastKnownOrCurrentLocation()
            _uiState.value = if (cord == null) {
                _uiState.value.copy(isLoading = false, status = "No location available.")
            } else {
                _uiState.value.copy(
                    isLoading = false, status = "Got coordinates!",
                    coordinate = cord)
            }
        }
    }
}