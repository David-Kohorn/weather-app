/**
 * David Kohorn
 */
package wpics.weather.model.location

interface LocationRepository {

    /**
     * Non-blocking retrieval of location
     *
     * @return A [Coordinate] for latitude and longitude
     */
    suspend fun getLastKnownOrCurrentLocation(): Coordinate?
}