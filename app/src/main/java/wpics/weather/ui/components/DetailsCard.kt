/**
 * David Kohorn
 */
package wpics.weather.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import wpics.weather.model.weather.CurrentWeather
import wpics.weather.model.weather.UnitSystem
import wpics.weather.ui.theme.OnWeatherText

@Composable
fun DetailsCard(weather: CurrentWeather, units: UnitSystem) {
    val rainMm = weather.rain?.mm ?: 0.0
    val windUnit = if (units == UnitSystem.IMPERIAL) "mph" else "m/s"

    val details = listOf(
        "Humidity" to "${weather.main.humidity}%",
        "Precipitation (1h)" to "%.1f mm".format(rainMm),
        "Pressure" to "${weather.main.pressure} hPa",
        "Wind" to "%.1f %s".format(weather.wind.speed.toDouble(), windUnit),
        "Sunrise" to formatTime(weather.sys.sunrise.toLong()),
        "Sunset" to formatTime(weather.sys.sunset.toLong())
    )

    InfoCard("Details") {
        details.chunked(2).forEach { pair ->
            Row(Modifier.fillMaxWidth()) {
                pair.forEach { (label, value) ->
                    Column(Modifier.weight(1f)) {
                        Text(
                            label,
                            style = MaterialTheme.typography.bodyMedium,
                            color = OnWeatherText.copy(alpha = 0.8f)
                        )
                        Text(value, style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}
