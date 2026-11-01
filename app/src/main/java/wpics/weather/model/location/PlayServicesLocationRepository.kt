/**
 * David Kohorn
 */
package wpics.weather.model.location

import android.Manifest
import android.content.Context
import android.location.Address
import android.util.Log
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume

/**
 * An implementation of [LocationRepository] to get location with Google Play Location Services API
 */
class PlayServicesLocationRepository(
    private val context: Context
) : LocationRepository {

    // Make sure this is part of the Manifest
    @RequiresPermission(
        allOf =
            [Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION]
    )
    override suspend fun getLastKnownOrCurrentLocation(): Coordinate? {
        // Location services
        val client = LocationServices.getFusedLocationProviderClient(context)

        // Attempt to retrieve location
        try {
            // Create a suspendable coroutine that fetches the last location if it is not available
            return suspendCancellableCoroutine { continuation ->
                // Store the client's last location
                client.lastLocation.addOnSuccessListener { location ->
                    // We have a last location already
                    if (location != null) {
                        continuation.resume(Coordinate(location.latitude, location.longitude))
                    } else {
                        // Handle case where GPS is off
                        val cts = CancellationTokenSource() // allows the request to be canceled if it takes too long
                        client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.token)
                            // Successful!
                            .addOnSuccessListener { cur ->
                                continuation.resume(cur?.let {
                                    Coordinate(
                                        it.latitude,
                                        it.longitude
                                    )
                                })
                            }
                            // Failure
                            .addOnFailureListener {
                                Log.e("WeatherApp", "Location is null. GPS may be disabled.")
                            }
                    }
                }.addOnFailureListener { e ->
                    Log.e("WeatherApp", "Location fetch failed: ${e.message}")
                }
            }
        } catch (e: SecurityException) {
            Log.e("WeatherApp", "Permission error: ${e.message}")
            return null
        }
    }
}