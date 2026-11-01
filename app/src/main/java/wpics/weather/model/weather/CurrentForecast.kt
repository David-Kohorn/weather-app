/**
 * David Kohorn
 */
package wpics.weather.model.weather

data class CurrentForecast(
    val list: List<Snapshot>,
)

/**
 * A single snap of weather
 */
data class Snapshot(
    val dt_txt: String,
    val main: Main,
    val weather: List<Weather>,
)
