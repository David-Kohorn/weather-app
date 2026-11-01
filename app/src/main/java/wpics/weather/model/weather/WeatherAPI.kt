/**
 * David Kohorn
 */
package wpics.weather.model.weather

import retrofit2.http.GET
import retrofit2.http.Query
import wpics.weather.BuildConfig

/**
 * An interface for the Weather API.
 */
interface WeatherAPI {
    @GET("weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("appid") appid: String = BuildConfig.WEATHER_KEY,
    ): CurrentWeather

    @GET("forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("appid") appid: String = BuildConfig.WEATHER_KEY,
    ): CurrentForecast
}



