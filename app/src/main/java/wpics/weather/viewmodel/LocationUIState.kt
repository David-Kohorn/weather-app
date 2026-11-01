/**
 * David Kohorn
 */
package wpics.weather.viewmodel

import wpics.weather.model.location.Coordinate

/**
 * This stores the current UI state for our app's location information
 */
data class LocationUIState(
    val permissionGranted: Boolean = false,
    val isLoading: Boolean = false,
    val coordinate: Coordinate? = null,
    val status: String = "Tap request permission to begin.",
)