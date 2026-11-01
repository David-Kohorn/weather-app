/**
 * David Kohorn
 */
package wpics.weather.model.weather

import com.squareup.moshi.Json

data class CurrentWeather (
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val rain: Rain? = null, //Not always returned from API
    val sys: Sys,
    val name: String,
    )

data class Weather (
    val description: String,
    val icon: String,
)

data class Main (
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int,
)

data class Wind (
    val speed: Double
)

data class Rain(
    @Json(name = "1h")
    val mm: Double? = null,
)

data class Sys (
    val sunset: Int,
    val sunrise: Int,
)