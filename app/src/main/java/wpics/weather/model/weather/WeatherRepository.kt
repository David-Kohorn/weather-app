/**
 * David Kohorn
 */
package wpics.weather.model.weather

import wpics.weather.model.location.Coordinate

/**
 * Abstraction layer for fetching weather data, handling sanitization, and API calls.
 *
 * @property api The [WeatherAPI] instance used for network requests.
 */
open class WeatherRepository(
    private val api: WeatherAPI = RetrofitProvider.api
) {

    /**
     * Retrieves weather for coordinates.
     *
     * @param coordinate The coordinate object to look for weather.
     * @param unitSystem Units of retrieval
     *
     * @return A CurrentWeather object.
     */
    open suspend fun weatherForCoordinate(coordinate: Coordinate, unitSystem: String = UnitSystem.METRIC.api): CurrentWeather {
        return api.getWeather(lat = coordinate.lat, lon = coordinate.lon, unitSystem)
    }

    /**
     * Retrieves forecast for coordinates.
     *
     * @param coordinate The coordinate object to look for forecast.
     * @param unitSystem Units of retrieval
     *
     * @return A CurrentForecast object.
     */
    open suspend fun forecastForCoordinate(coordinate: Coordinate, unitSystem: String = UnitSystem.METRIC.api): CurrentForecast {
        return api.getForecast(lat = coordinate.lat, lon = coordinate.lon, unitSystem)
    }
}